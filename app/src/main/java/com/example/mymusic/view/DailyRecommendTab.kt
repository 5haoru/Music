package com.example.mymusic.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mymusic.data.Song
import com.example.mymusic.presenter.DailyRecommendContract
import com.example.mymusic.presenter.DailyRecommendPresenter
import com.example.mymusic.ui.components.DailySongItem
import java.text.SimpleDateFormat
import java.util.*

/**
 * 每日推荐页面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyRecommendTab(
    onBackClick: () -> Unit = {},
    onNavigateToPlay: (String) -> Unit = {}
) {
    val context = LocalContext.current

    // 状态
    var dailySongs by remember { mutableStateOf<List<Song>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var selectedMode by remember { mutableStateOf(0) } // 0: 默认推荐, 1: 风格推荐

    // Presenter
    val presenter = remember {
        DailyRecommendPresenter(
            object : DailyRecommendContract.View {
                override fun showDailyRecommendedSongs(songs: List<Song>) {
                    dailySongs = songs
                }

                override fun navigateToPlay(songId: String) {
                    onNavigateToPlay(songId)
                }

                override fun playAllSongs(songs: List<Song>) {
                    // TODO: 实现播放全部功能
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
        presenter.loadDailyRecommendData()
    }

    Scaffold(
        topBar = {
            DailyRecommendTopBar(
                selectedMode = selectedMode,
                onModeChange = { selectedMode = it },
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (errorMessage != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(text = errorMessage ?: "")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // 日期和功能区
                item {
                    DailyHeaderSection(
                        onHistoryClick = { presenter.onHistoryClick() },
                        onFortuneClick = { presenter.onTodayFortuneClick() }
                    )
                }

                // 播放全部按钮区域
                item {
                    PlayAllSection(
                        songCount = dailySongs.size,
                        onPlayAllClick = { presenter.onPlayAllClick() }
                    )
                }

                // 歌曲列表
                itemsIndexed(dailySongs) { index, song ->
                    DailySongItem(
                        index = index,
                        song = song,
                        onSongClick = { presenter.onSongClick(song.songId, index + 1) },
                        onPlayClick = { presenter.onSongClick(song.songId, index + 1) }
                    )
                }

                // 底部留白
                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}

/**
 * 每日推荐顶部栏
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DailyRecommendTopBar(
    selectedMode: Int,
    onModeChange: (Int) -> Unit,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        // 第一行：返回按钮、模式切换、更多按钮
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 返回按钮
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "返回"
                )
            }

            // 模式切换按钮
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ) {
                Row(
                    modifier = Modifier.padding(4.dp)
                ) {
                    ModeSwitchButton(
                        text = "默认推荐",
                        isSelected = selectedMode == 0,
                        onClick = { onModeChange(0) }
                    )
                    ModeSwitchButton(
                        text = "风格推荐",
                        isSelected = selectedMode == 1,
                        onClick = { onModeChange(1) }
                    )
                }
            }

            // 更多按钮
            IconButton(onClick = { /* TODO */ }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "更多"
                )
            }
        }
    }
}

/**
 * 模式切换按钮
 */
@Composable
private fun ModeSwitchButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) MaterialTheme.colorScheme.surface else Color.Transparent,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

/**
 * 日期和功能区域
 */
@Composable
private fun DailyHeaderSection(
    onHistoryClick: () -> Unit,
    onFortuneClick: () -> Unit
) {
    // 获取当前日期
    val calendar = Calendar.getInstance()
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val month = calendar.get(Calendar.MONTH) + 1

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 左侧：日期和查看今日运势
            Column {
                Row(
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = "$day",
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "/$month",
                        fontSize = 24.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 查看今日运势
                Row(
                    modifier = Modifier
                        .clickable(onClick = onFortuneClick)
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "查看今日运势",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            }

            // 右侧：历史日推
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    .clickable(onClick = onHistoryClick)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "历史日推",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(4.dp))
                // VIP 标识
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(
                            color = Color(0xFFFFB74D).copy(alpha = 0.3f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "V",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFB74D)
                    )
                }
            }
        }
    }
}

/**
 * 播放全部区域
 */
@Composable
private fun PlayAllSection(
    songCount: Int,
    onPlayAllClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 播放全部按钮
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(24.dp))
                .clickable(onClick = onPlayAllClick)
                .padding(horizontal = 4.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 播放图标（红色圆形背景）
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        color = Color(0xFFEF5350),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "播放",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "播放全部",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = "($songCount)",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }

        // 右侧图标
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(onClick = { /* TODO: 历史记录 */ }) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "历史记录",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            IconButton(onClick = { /* TODO: 排序 */ }) {
                Icon(
                    imageVector = Icons.Default.List,
                    contentDescription = "排序",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}
