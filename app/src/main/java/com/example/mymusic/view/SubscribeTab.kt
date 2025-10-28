package com.example.mymusic.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mymusic.data.FollowItem
import com.example.mymusic.presenter.SubscribeContract
import com.example.mymusic.presenter.SubscribePresenter

/**
 * 关注页面Tab（二级页面）
 * 从我的页面的"关注"入口进入
 */
@Composable
fun SubscribeTab(
    onBackClick: () -> Unit = {}
) {
    val context = LocalContext.current

    // 状态
    var followItems by remember { mutableStateOf<List<FollowItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var selectedTab by remember { mutableStateOf(1) } // 0:推荐, 1:关注, 2:粉丝
    var selectedFilter by remember { mutableStateOf("all") } // "all", "artist", "user"
    var showAggregatedUpdate by remember { mutableStateOf(false) }
    var aggregatedArtistNames by remember { mutableStateOf<List<String>>(emptyList()) }
    var aggregatedCount by remember { mutableStateOf(0) }
    var showUnfollowDialog by remember { mutableStateOf(false) }
    var selectedFollowItem by remember { mutableStateOf<FollowItem?>(null) }

    // Presenter
    val presenter = remember {
        SubscribePresenter(
            object : SubscribeContract.View {
                override fun showFollowItems(items: List<FollowItem>) {
                    followItems = items
                }

                override fun updateFilteredItems(items: List<FollowItem>) {
                    followItems = items
                }

                override fun showAggregatedUpdate(artistNames: List<String>, count: Int) {
                    showAggregatedUpdate = true
                    aggregatedArtistNames = artistNames
                    aggregatedCount = count
                }

                override fun showLoading() {
                    isLoading = true
                }

                override fun hideLoading() {
                    isLoading = false
                }

                override fun showError(message: String) {
                    errorMessage = message
                    isLoading = false
                }
            },
            context
        )
    }

    // 加载数据
    LaunchedEffect(Unit) {
        presenter.loadData()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // 顶部导航栏
        SubscribeTopBar(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it },
            onBackClick = onBackClick,
            onSearchClick = { presenter.onSearchClick() }
        )

        // 筛选栏
        FilterBar(
            selectedFilter = selectedFilter,
            onFilterSelected = {
                selectedFilter = it
                presenter.filterByType(it)
            },
            onSortClick = { presenter.sortByTime() }
        )

        // 内容区域
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (errorMessage != null) {
                Text(
                    text = errorMessage ?: "",
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.error
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    // 聚合更新横幅
                    if (showAggregatedUpdate) {
                        item {
                            AggregatedUpdateBanner(
                                artistNames = aggregatedArtistNames,
                                count = aggregatedCount,
                                onClick = { presenter.onAggregatedUpdateClick() }
                            )
                        }
                    }

                    // 关注列表
                    items(followItems) { item ->
                        FollowItemCard(
                            item = item,
                            onClick = { presenter.onFollowItemClick(item) },
                            onMoreClick = {
                                selectedFollowItem = item
                                showUnfollowDialog = true
                                presenter.onFollowItemMoreClick(item)
                            }
                        )
                    }

                    // 底部推荐区域
                    item {
                        RecommendationSection()
                    }
                }
            }
        }

        // 取消关注对话框
        if (showUnfollowDialog && selectedFollowItem != null) {
            UnfollowDialog(
                item = selectedFollowItem!!,
                onDismiss = {
                    showUnfollowDialog = false
                    selectedFollowItem = null
                },
                onConfirm = {
                    presenter.unfollowItem(selectedFollowItem!!)
                    showUnfollowDialog = false
                    selectedFollowItem = null
                }
            )
        }
    }
}

/**
 * 顶部导航栏
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SubscribeTopBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    onBackClick: () -> Unit,
    onSearchClick: () -> Unit
) {
    Column {
        TopAppBar(
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 三栏标签
                    TabRow(
                        tabs = listOf("推荐", "关注", "粉丝"),
                        selectedTab = selectedTab,
                        onTabSelected = onTabSelected
                    )
                }
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "返回"
                    )
                }
            },
            actions = {
                IconButton(onClick = onSearchClick) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "搜索"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )
    }
}

/**
 * 标签行组件
 */
@Composable
private fun TabRow(
    tabs: List<String>,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        tabs.forEachIndexed { index, title ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { onTabSelected(index) }
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                    color = if (selectedTab == index) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                if (selectedTab == index) {
                    Box(
                        modifier = Modifier
                            .width(24.dp)
                            .height(3.dp)
                            .background(
                                MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(1.5.dp)
                            )
                    )
                }
            }
        }
    }
}

/**
 * 筛选栏
 */
@Composable
private fun FilterBar(
    selectedFilter: String,
    onFilterSelected: (String) -> Unit,
    onSortClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 排序按钮
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { onSortClick() }
        ) {
            Text(
                text = "按关注时间排序",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // 筛选标签
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FilterChip(
                label = "全部",
                selected = selectedFilter == "all",
                onClick = { onFilterSelected("all") }
            )
            FilterChip(
                label = "歌手",
                selected = selectedFilter == "artist",
                onClick = { onFilterSelected("artist") }
            )
            FilterChip(
                label = "用户",
                selected = selectedFilter == "user",
                onClick = { onFilterSelected("user") }
            )
        }
    }
}

/**
 * 筛选标签芯片
 */
@Composable
private fun FilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Text(
        text = label,
        fontSize = 14.sp,
        color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.clickable { onClick() }
    )
}

/**
 * 聚合更新横幅
 */
@Composable
private fun AggregatedUpdateBanner(
    artistNames: List<String>,
    count: Int,
    onClick: () -> Unit
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // 多个头像叠加效果
                Box(
                    modifier = Modifier.size(48.dp)
                ) {
                    // 简化：只显示第一个头像
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data("file:///android_asset/avatar/2.png")
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // 文本信息
                Text(
                    text = "${artistNames.firstOrNull() ?: ""}等${count}位艺人有新的动...",
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * 关注项卡片
 */
@Composable
private fun FollowItemCard(
    item: FollowItem,
    onClick: () -> Unit,
    onMoreClick: () -> Unit
) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 头像（带V标识）
        Box {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data("file:///android_asset/${item.avatarUrl}")
                    .crossfade(true)
                    .build(),
                contentDescription = item.name,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            // V标识
            if (item.vipType != null) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(
                            when (item.vipType) {
                                "svip" -> Color(0xFFD4AF37)
                                "vip" -> Color(0xFF1E90FF)
                                else -> Color.Gray
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "V",
                        fontSize = 8.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // 信息区域
        Column(
            modifier = Modifier.weight(1f)
        ) {
            // 名称 + VIP徽章
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                // VIP徽章
                if (item.vipType != null) {
                    Spacer(modifier = Modifier.width(6.dp))
                    VipBadge(vipType = item.vipType)
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // 副标题
            if (item.subtitle != null) {
                Text(
                    text = item.subtitle,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(2.dp))
            }

            // 动态信息
            if (item.activityText != null && item.timestamp != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${item.timestamp}，${item.activityText}",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // 更多按钮
        IconButton(onClick = onMoreClick) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "更多",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * VIP徽章组件
 */
@Composable
private fun VipBadge(vipType: String) {
    Surface(
        shape = RoundedCornerShape(4.dp),
        color = when (vipType) {
            "svip" -> Color(0xFFD4AF37)
            "black_svip" -> Color.Black
            "vip" -> Color(0xFF1E90FF)
            else -> Color.Gray
        }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                modifier = Modifier.size(10.dp),
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = when (vipType) {
                    "svip" -> "SVIP"
                    "black_svip" -> "黑胶SVIP"
                    "vip" -> "VIP"
                    else -> "VIP"
                },
                fontSize = 9.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * 底部推荐区域
 */
@Composable
private fun RecommendationSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "为你推荐",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Mini播放器样式的推荐卡片
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 封面
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
                )

                Spacer(modifier = Modifier.width(12.dp))

                // 信息
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "のドア/Stay With Me (シング...",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // 播放按钮
                IconButton(onClick = { /* TODO */ }) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "播放",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                // 更多按钮
                IconButton(onClick = { /* TODO */ }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "更多",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

/**
 * 取消关注对话框
 */
@Composable
private fun UnfollowDialog(
    item: FollowItem,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp,
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "取消关注后对方将从你的关注列表中移除",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        },
        confirmButton = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onConfirm() }
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.PersonRemove,
                    contentDescription = "取消关注",
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "取消关注",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        dismissButton = {}
    )
}
