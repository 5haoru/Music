package com.example.mymusic.view

import android.widget.Toast
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mymusic.presenter.RankContract
import com.example.mymusic.presenter.RankPresenter

/**
 * 榜单详情页面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RankTab(
    rankId: String,
    onBackClick: () -> Unit = {},
    onNavigateToPlay: (String) -> Unit = {}
) {
    val context = LocalContext.current

    // 状态
    var rankDetail by remember { mutableStateOf<RankContract.RankDetail?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isDescriptionExpanded by remember { mutableStateOf(false) }

    // Presenter
    val presenter = remember {
        RankPresenter(
            object : RankContract.View {
                override fun showRankDetail(detail: RankContract.RankDetail) {
                    rankDetail = detail
                }

                override fun updateFollowStatus(isFollowing: Boolean) {
                    rankDetail = rankDetail?.copy(isFollowing = isFollowing)
                }

                override fun updateLikeStatus(isLiked: Boolean, likeCount: Long) {
                    rankDetail = rankDetail?.copy(likeCount = likeCount)
                }

                override fun updateSongFavoriteStatus(songId: String, isFavorite: Boolean) {
                    rankDetail = rankDetail?.let { detail ->
                        val updatedSongs = detail.songs.map { item ->
                            if (item.song.songId == songId) {
                                item.copy(isFavorite = isFavorite)
                            } else {
                                item
                            }
                        }
                        detail.copy(songs = updatedSongs)
                    }
                }

                override fun showSuccess(message: String) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }

                override fun navigateToPlay(songId: String) {
                    onNavigateToPlay(songId)
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
    LaunchedEffect(rankId) {
        presenter.loadRankDetail(rankId)
    }

    Scaffold(
        topBar = {
            RankTopBar(onBackClick = onBackClick)
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
            rankDetail?.let { detail ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    // 榜单头部信息
                    item {
                        RankHeader(
                            detail = detail,
                            onFollowClick = { presenter.onFollowClick() }
                        )
                    }

                    // 描述区域
                    item {
                        RankDescription(
                            description = detail.description,
                            isExpanded = isDescriptionExpanded,
                            onExpandClick = { isDescriptionExpanded = !isDescriptionExpanded }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // 互动按钮行
                    item {
                        InteractionButtonsRow(
                            shareCount = detail.shareCount,
                            commentCount = detail.commentCount,
                            likeCount = detail.likeCount,
                            onShareClick = { presenter.onShareClick() },
                            onCommentClick = { presenter.onCommentClick() },
                            onLikeClick = { presenter.onLikeClick() }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // 播放控制栏
                    item {
                        PlayControlBar(
                            songCount = detail.songs.size,
                            onPlayAllClick = { presenter.onPlayAllClick() }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    // 歌曲列表
                    items(detail.songs) { songItem ->
                        RankSongItem(
                            songItem = songItem,
                            onSongClick = { presenter.onSongClick(songItem.song.songId) },
                            onFavoriteClick = { presenter.onFavoriteClick(songItem.song.songId) },
                            onMoreClick = { presenter.onSongMoreClick(songItem.song.songId) }
                        )
                    }

                    // 底部留白（为底部播放器留空间）
                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }
    }
}

/**
 * 榜单顶部栏
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RankTopBar(
    onBackClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "歌单",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
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
            IconButton(onClick = { /* TODO: 搜索 */ }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "搜索"
                )
            }
            IconButton(onClick = { /* TODO: 更多 */ }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "更多"
                )
            }
        }
    )
}

/**
 * 榜单头部信息区
 */
@Composable
private fun RankHeader(
    detail: RankContract.RankDetail,
    onFollowClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 左侧封面
        Box(
            modifier = Modifier.size(140.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("file:///android_asset/${detail.coverUrl}")
                    .crossfade(true)
                    .build(),
                contentDescription = detail.rankName,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            // 播放次数
            Row(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp)
                    .background(
                        color = Color.Black.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 6.dp, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = formatPlayCount(detail.playCount),
                    color = Color.White,
                    fontSize = 11.sp
                )
            }
        }

        // 右侧信息
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 榜单名称
            Text(
                text = detail.rankName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            // 官方认证
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color.Red,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = detail.officialName,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                OutlinedButton(
                    onClick = onFollowClick,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.height(26.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                ) {
                    Text(
                        text = if (detail.isFollowing) "已关注" else "+ 关注",
                        fontSize = 11.sp
                    )
                }
            }

            // 更新时间
            Text(
                text = detail.updateDate,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * 格式化播放次数
 */
private fun formatPlayCount(count: Long): String {
    return when {
        count >= 100000000 -> "${count / 100000000}亿"
        count >= 10000 -> "${count / 10000}万"
        else -> "$count"
    }
}

/**
 * 榜单描述区域
 */
@Composable
private fun RankDescription(
    description: String,
    isExpanded: Boolean,
    onExpandClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onExpandClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = description,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = if (isExpanded) Int.MAX_VALUE else 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowRight,
            contentDescription = if (isExpanded) "收起" else "展开",
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * 互动按钮行
 */
@Composable
private fun InteractionButtonsRow(
    shareCount: Int,
    commentCount: Int,
    likeCount: Long,
    onShareClick: () -> Unit,
    onCommentClick: () -> Unit,
    onLikeClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 分享按钮
        InteractionButton(
            icon = Icons.Default.Share,
            text = formatCount(shareCount),
            onClick = onShareClick,
            modifier = Modifier.weight(1f),
            backgroundColor = MaterialTheme.colorScheme.surfaceVariant
        )

        // 评论按钮
        InteractionButton(
            icon = Icons.Default.Send,
            text = formatCount(commentCount),
            onClick = onCommentClick,
            modifier = Modifier.weight(1f),
            backgroundColor = MaterialTheme.colorScheme.surfaceVariant
        )

        // 点赞按钮（金色背景）
        InteractionButton(
            icon = Icons.Default.ThumbUp,
            text = formatLikeCount(likeCount),
            onClick = onLikeClick,
            modifier = Modifier.weight(1f),
            backgroundColor = Color(0xFFFFB74D)
        )
    }
}

/**
 * 互动按钮
 */
@Composable
private fun InteractionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = backgroundColor,
        modifier = modifier.height(44.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = text,
                fontSize = 14.sp
            )
        }
    }
}

/**
 * 格式化数量
 */
private fun formatCount(count: Int): String {
    return if (count >= 10000) {
        "${count / 10000}万"
    } else {
        "$count"
    }
}

/**
 * 格式化点赞数
 */
private fun formatLikeCount(count: Long): String {
    return when {
        count >= 100000 -> String.format("%.1f万", count / 10000.0)
        count >= 10000 -> "${count / 10000}万"
        else -> "$count"
    }
}

/**
 * 播放控制栏
 */
@Composable
private fun PlayControlBar(
    songCount: Int,
    onPlayAllClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // 播放全部
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable(onClick = onPlayAllClick)
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "播放全部",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFB74D))
                    .padding(8.dp),
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "播放全部",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "($songCount)",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // 右侧按钮
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            IconButton(onClick = { /* TODO: 下载 */ }) {
                Icon(
                    imageVector = Icons.Default.ArrowCircleDown,
                    contentDescription = "下载"
                )
            }
            IconButton(onClick = { /* TODO: 列表 */ }) {
                Icon(
                    imageVector = Icons.Default.List,
                    contentDescription = "列表"
                )
            }
        }
    }
}

/**
 * 榜单歌曲项
 */
@Composable
private fun RankSongItem(
    songItem: RankContract.RankSongItem,
    onSongClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onMoreClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSongClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 排名
        Box(
            modifier = Modifier.width(36.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${songItem.rank}",
                fontSize = 16.sp,
                fontWeight = if (songItem.rank <= 3) FontWeight.Bold else FontWeight.Normal,
                color = if (songItem.rank <= 3) Color(0xFFFF6B6B) else MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(modifier = Modifier.width(4.dp))

        // 排名变化指示器
        Box(
            modifier = Modifier.width(24.dp),
            contentAlignment = Alignment.Center
        ) {
            when (songItem.rankChange) {
                RankContract.RankChange.NEW -> {
                    Text(
                        text = "NEW",
                        fontSize = 9.sp,
                        color = Color.Red,
                        modifier = Modifier
                            .background(
                                color = Color.Red.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(3.dp)
                            )
                            .padding(horizontal = 3.dp, vertical = 1.dp)
                    )
                }
                RankContract.RankChange.UP -> {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "▲",
                            fontSize = 10.sp,
                            color = Color.Red
                        )
                        if (songItem.changeValue > 0) {
                            Text(
                                text = "${songItem.changeValue}",
                                fontSize = 9.sp,
                                color = Color.Red
                            )
                        }
                    }
                }
                RankContract.RankChange.DOWN -> {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "▼",
                            fontSize = 10.sp,
                            color = Color(0xFF4CAF50)
                        )
                        if (songItem.changeValue > 0) {
                            Text(
                                text = "${songItem.changeValue}",
                                fontSize = 9.sp,
                                color = Color(0xFF4CAF50)
                            )
                        }
                    }
                }
                RankContract.RankChange.STABLE -> {
                    // 不显示任何内容
                }
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // 歌曲信息
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // 收藏图标
                if (songItem.isFavorite) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = Color.Red,
                        modifier = Modifier.size(16.dp)
                    )
                }

                // 音质标签
                if (songItem.hasQualityTag) {
                    Surface(
                        shape = RoundedCornerShape(3.dp),
                        color = Color(0xFFFFE0B2)
                    ) {
                        Text(
                            text = "超清母带",
                            fontSize = 9.sp,
                            color = Color(0xFFD84315),
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                }

                // 歌曲名
                Text(
                    text = songItem.song.songName,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // NEW标签
                if (songItem.isNew) {
                    Text(
                        text = "NEW",
                        fontSize = 9.sp,
                        color = Color.Red,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // 歌手信息
            Text(
                text = songItem.song.artist,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // 播放按钮
        IconButton(onClick = onSongClick) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "播放",
                modifier = Modifier.size(24.dp)
            )
        }

        // 更多按钮
        IconButton(onClick = onMoreClick) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "更多",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
