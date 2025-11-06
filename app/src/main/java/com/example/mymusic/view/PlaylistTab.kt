package com.example.mymusic.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.mymusic.data.Playlist
import com.example.mymusic.data.Song
import com.example.mymusic.presenter.PlaylistContract
import com.example.mymusic.presenter.PlaylistPresenter
import com.example.mymusic.utils.AutoTestHelper

/**
 * 歌单详情页面Tab（二级页面）
 * 从我的页面点击歌单进入
 */
@Composable
fun PlaylistTab(
    playlist: Playlist,
    onBackClick: () -> Unit = {},
    onNavigateToPlay: (Song) -> Unit = {},
    onNavigateToSetting: (Playlist) -> Unit = {},
    onNavigateToSongDel: (String, String) -> Unit = { _, _ -> }
) {
    val context = LocalContext.current

    // 状态
    var songs by remember { mutableStateOf<List<Song>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // 歌曲删除导航状态
    var showSongDelTab by remember { mutableStateOf(false) }
    var selectedSongId by remember { mutableStateOf<String?>(null) }
    var needReload by remember { mutableStateOf(false) }

    // Presenter
    val presenter = remember {
        PlaylistPresenter(
            object : PlaylistContract.View {
                override fun showSongs(songList: List<Song>) {
                    songs = songList
                }

                override fun navigateToPlay(song: Song) {
                    onNavigateToPlay(song)
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
                    android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT).show()
                }
            },
            context
        )
    }

    // 加载数据
    LaunchedEffect(playlist.playlistId) {
        presenter.loadPlaylistSongs(playlist)
    }

    // 更新AutoTest状态，记录当前查看的歌单
    LaunchedEffect(playlist.playlistId) {
        // 确保歌单在AutoTest中被跟踪（对于默认歌单如"我喜欢的音乐"）
        AutoTestHelper.ensurePlaylistTracked(
            playlistId = playlist.playlistId,
            playlistName = playlist.playlistName,
            songIds = playlist.songIds
        )
        // 记录当前查看的歌单
        AutoTestHelper.updateCurrentViewingPlaylist(playlist.playlistId)
    }

    // 退出时清除当前查看的歌单
    DisposableEffect(Unit) {
        onDispose {
            AutoTestHelper.updateCurrentViewingPlaylist(null)
        }
    }

    // 监听needReload，重新加载歌单数据
    LaunchedEffect(needReload) {
        if (needReload) {
            presenter.loadPlaylistSongs(playlist)
            needReload = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // 歌曲删除页面
        if (showSongDelTab && selectedSongId != null) {
            SongDelTab(
                songId = selectedSongId!!,
                playlistId = playlist.playlistId,
                onBackClick = {
                    showSongDelTab = false
                    selectedSongId = null
                    needReload = true // 返回时重新加载歌单
                }
            )
        } else if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // 顶部栏
                PlaylistTopBar(
                    onBackClick = onBackClick,
                    onMoreClick = { onNavigateToSetting(playlist) }
                )

                // 内容区域
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    // 歌单信息区
                    item {
                        PlaylistHeader(
                            playlist = playlist,
                            playCount = 1540 // 硬编码播放次数
                        )
                    }

                    // 操作按钮行
                    item {
                        ActionButtonsRow(
                            onShareClick = { presenter.onShareClick() },
                            onCommentClick = { presenter.onCommentClick() },
                            onCollectClick = { presenter.onCollectClick() }
                        )
                    }

                    // 播放全部区域
                    item {
                        PlayAllSection(
                            songCount = songs.size,
                            totalDuration = calculateTotalDuration(songs),
                            onPlayAllClick = { presenter.onPlayAllClick() }
                        )
                    }

                    // 继续播放提示（可选）
                    item {
                        if (songs.isNotEmpty()) {
                            ContinuePlayingBanner(
                                songName = songs.firstOrNull()?.songName ?: ""
                            )
                        }
                    }

                    // 歌曲列表
                    items(songs) { song ->
                        SongListItem(
                            song = song,
                            onClick = { presenter.onSongClick(song) },
                            onMoreClick = {
                                selectedSongId = song.songId
                                showSongDelTab = true
                            }
                        )
                    }
                }
            }
        }
    }
}

/**
 * 顶部栏
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlaylistTopBar(
    onBackClick: () -> Unit,
    onMoreClick: () -> Unit = {}
) {
    TopAppBar(
        title = { },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "返回",
                    tint = Color.White
                )
            }
        },
        actions = {
            IconButton(onClick = { /* TODO: 搜索 */ }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "搜索",
                    tint = Color.White
                )
            }
            IconButton(onClick = onMoreClick) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "更多",
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Black.copy(alpha = 0.3f)
        )
    )
}

/**
 * 歌单信息头部
 */
@Composable
private fun PlaylistHeader(playlist: Playlist, playCount: Int) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 歌单封面
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data("file:///android_asset/${playlist.coverUrl}")
                .crossfade(true)
                .build(),
            contentDescription = "歌单封面",
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        // 歌单信息
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 歌单标题
            Text(
                text = playlist.playlistName,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            // 创建者和播放次数
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 创建者头像（小圆形）
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                )

                Text(
                    text = "箱子里的稻草人",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = "|",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = "${playCount}次播放",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * 操作按钮行
 */
@Composable
private fun ActionButtonsRow(
    onShareClick: () -> Unit,
    onCommentClick: () -> Unit,
    onCollectClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 分享按钮
        ActionButton(
            icon = Icons.Default.Share,
            text = "分享",
            onClick = onShareClick,
            modifier = Modifier.weight(1f)
        )

        // 评论按钮
        ActionButton(
            icon = Icons.Default.Comment,
            text = "评论",
            onClick = onCommentClick,
            modifier = Modifier.weight(1f)
        )

        // 收藏按钮
        ActionButton(
            icon = Icons.Default.Add,
            text = "收藏",
            onClick = onCollectClick,
            modifier = Modifier.weight(1f)
        )
    }
}

/**
 * 操作按钮
 */
@Composable
private fun ActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                modifier = Modifier.size(18.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = text,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * 播放全部区域
 */
@Composable
private fun PlayAllSection(
    songCount: Int,
    totalDuration: String,
    onPlayAllClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onPlayAllClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 播放按钮
        Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = "播放",
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(Color(0xFFFFB74D)) // 金色/橙色
                .padding(12.dp),
            tint = Color.White
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "播放全部",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "${songCount}首歌曲 · $totalDuration",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // 右侧图标
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Download,
                contentDescription = "下载",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Icon(
                imageVector = Icons.Default.List,
                contentDescription = "列表",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * 继续播放横幅
 */
@Composable
private fun ContinuePlayingBanner(songName: String) {
    if (songName.isNotEmpty()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "播放",
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "继续播放：$songName",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            IconButton(onClick = { /* TODO: 关闭 */ }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "关闭",
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * 歌曲列表项
 */
@Composable
private fun SongListItem(
    song: Song,
    onClick: () -> Unit,
    onMoreClick: () -> Unit = {}
) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 封面缩略图
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data("file:///android_asset/${song.coverUrl}")
                .crossfade(true)
                .build(),
            contentDescription = "歌曲封面",
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(4.dp)),
            contentScale = ContentScale.Crop
        )

        // 歌曲信息
        Column(
            modifier = Modifier.weight(1f)
        ) {
            // 歌名和标识
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = song.songName,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )

                // VIP标识（根据歌曲ID简单判断）
                if (song.songId.hashCode() % 3 == 0) {
                    Surface(
                        shape = RoundedCornerShape(3.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            Color(0xFFFFD700)
                        ),
                        color = Color.Transparent
                    ) {
                        Text(
                            text = "VIP",
                            fontSize = 10.sp,
                            color = Color(0xFFFFD700),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                }

                // 音质标识（部分歌曲显示）
                if (song.songId.hashCode() % 2 == 0) {
                    Surface(
                        shape = RoundedCornerShape(3.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            Color(0xFFFFD700)
                        ),
                        color = Color.Transparent
                    ) {
                        Text(
                            text = "超清母带",
                            fontSize = 10.sp,
                            color = Color(0xFFFFD700),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // 歌手和专辑
            Text(
                text = "${song.artist} - ${song.album}",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
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
 * 计算总时长
 */
private fun calculateTotalDuration(songs: List<Song>): String {
    val totalSeconds = songs.sumOf { it.duration }
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60

    return when {
        hours > 0 -> "大于${hours}小时"
        minutes > 0 -> "${minutes}分钟"
        else -> "少于1分钟"
    }
}
