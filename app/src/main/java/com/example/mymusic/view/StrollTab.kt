package com.example.mymusic.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mymusic.data.Song
import com.example.mymusic.presenter.StrollContract
import com.example.mymusic.presenter.StrollPresenter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 漫游页面Tab
 */
@Composable
fun StrollTab(onBackToRecommend: () -> Unit = {}) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // 状态
    var currentSong by remember { mutableStateOf<Song?>(null) }
    var isPlaying by remember { mutableStateOf(false) }
    var isFavorite by remember { mutableStateOf(false) }
    var progress by remember { mutableStateOf(0f) }
    var currentTime by remember { mutableStateOf("00:00") }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Presenter
    val presenter = remember {
        StrollPresenter(
            object : StrollContract.View {
                override fun showSong(song: Song) {
                    currentSong = song
                }

                override fun updatePlayState(playing: Boolean) {
                    isPlaying = playing
                }

                override fun updateFavoriteState(favorite: Boolean) {
                    isFavorite = favorite
                }

                override fun updateProgress(prog: Float, time: String) {
                    progress = prog
                    currentTime = time
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

    // 设置进度更新回调
    DisposableEffect(Unit) {
        presenter.setProgressUpdateCallback {
            coroutineScope.launch {
                while (isPlaying) {
                    delay(1000) // 每秒更新一次
                    presenter.updateProgressAutomatically()
                }
            }
        }
        onDispose {
            // 清理资源
        }
    }

    // 加载数据
    LaunchedEffect(Unit) {
        presenter.loadData()
    }

    // 监听播放状态，自动更新进度
    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            while (isPlaying) {
                delay(1000) // 每秒更新一次
                presenter.updateProgressAutomatically()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
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
            currentSong?.let { song ->
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // 顶部栏
                    StrollTopBar(
                        onBackClick = onBackToRecommend,
                        onShareClick = { /* TODO: 分享功能 */ }
                    )

                    // 中央播放器区域
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        // 专辑封面
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data("file:///android_asset/${song.coverUrl}")
                                .crossfade(true)
                                .build(),
                            contentDescription = song.songName,
                            modifier = Modifier
                                .size(280.dp)
                                .clip(RoundedCornerShape(16.dp)),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        // 歌曲信息
                        SongInfoSection(
                            song = song,
                            isFavorite = isFavorite,
                            onFavoriteClick = { presenter.onFavoriteClick() }
                        )
                    }

                    // 进度条和控制按钮区域
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 16.dp)
                    ) {
                        // 进度条
                        ProgressSection(
                            song = song,
                            progress = progress,
                            currentTime = currentTime,
                            onProgressChange = { presenter.onProgressChange(it) }
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // 播放控制按钮
                        PlayControlSection(
                            isPlaying = isPlaying,
                            onPreviousClick = { presenter.onPreviousClick() },
                            onPlayPauseClick = { presenter.onPlayPauseClick() },
                            onNextClick = { presenter.onNextClick() }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // 底部功能按钮
                        BottomFunctionBar(
                            onCommentClick = { presenter.onCommentClick() },
                            onMoreClick = { presenter.onMoreClick() }
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
private fun StrollTopBar(
    onBackClick: () -> Unit,
    onShareClick: () -> Unit
) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "私人漫游 · 伤感",
                    style = MaterialTheme.typography.titleMedium
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "展开",
                    modifier = Modifier.size(20.dp)
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "返回"
                )
            }
        },
        actions = {
            IconButton(onClick = onShareClick) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "分享"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )
}

/**
 * 歌曲信息区域
 */
@Composable
private fun SongInfoSection(
    song: Song,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 左侧：歌曲信息
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start
        ) {
            // 歌曲名
            Text(
                text = song.songName,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            // 艺术家和关注按钮
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = song.artist,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedButton(
                    onClick = { /* TODO: 关注 */ },
                    modifier = Modifier.height(28.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                ) {
                    Text(
                        text = "关注",
                        fontSize = 12.sp
                    )
                }
            }
        }

        // 右侧：收藏和评论统计
        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 收藏（爱心）
            IconButton(onClick = onFavoriteClick) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "收藏",
                        modifier = Modifier.size(28.dp),
                        tint = if (isFavorite) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "999+",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 10.sp
                    )
                }
            }

            // 评论
            IconButton(onClick = { /* TODO: 评论 */ }) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.ChatBubbleOutline,
                        contentDescription = "评论",
                        modifier = Modifier.size(28.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "399",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}

/**
 * 进度条区域
 */
@Composable
private fun ProgressSection(
    song: Song,
    progress: Float,
    currentTime: String,
    onProgressChange: (Float) -> Unit
) {
    val totalDuration = song.duration
    val totalMinutes = (totalDuration / 1000) / 60
    val totalSeconds = (totalDuration / 1000) % 60
    val totalTimeString = String.format("%02d:%02d", totalMinutes, totalSeconds)

    Column {
        Slider(
            value = progress,
            onValueChange = onProgressChange,
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = currentTime,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = "极高",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = totalTimeString,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * 播放控制区域
 */
@Composable
private fun PlayControlSection(
    isPlaying: Boolean,
    onPreviousClick: () -> Unit,
    onPlayPauseClick: () -> Unit,
    onNextClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 无限循环图标（漫游模式）
        IconButton(onClick = { /* TODO: 切换播放模式 */ }) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "漫游模式",
                modifier = Modifier.size(28.dp)
            )
        }

        // 上一曲
        IconButton(onClick = onPreviousClick) {
            Icon(
                imageVector = Icons.Default.SkipPrevious,
                contentDescription = "上一曲",
                modifier = Modifier.size(40.dp)
            )
        }

        // 播放/暂停
        FilledIconButton(
            onClick = onPlayPauseClick,
            modifier = Modifier.size(64.dp),
            shape = CircleShape,
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(
                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = if (isPlaying) "暂停" else "播放",
                modifier = Modifier.size(36.dp),
                tint = Color.White
            )
        }

        // 下一曲
        IconButton(onClick = onNextClick) {
            Icon(
                imageVector = Icons.Default.SkipNext,
                contentDescription = "下一曲",
                modifier = Modifier.size(40.dp)
            )
        }

        // 播放列表
        IconButton(onClick = { /* TODO: 打开播放列表 */ }) {
            Icon(
                imageVector = Icons.Default.PlaylistPlay,
                contentDescription = "播放列表",
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

/**
 * 底部功能栏
 */
@Composable
private fun BottomFunctionBar(
    onCommentClick: () -> Unit,
    onMoreClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        IconButton(onClick = { /* TODO: 音效 */ }) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.GraphicEq,
                    contentDescription = "音效",
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        IconButton(onClick = onCommentClick) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Comment,
                    contentDescription = "评论",
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        IconButton(onClick = { /* TODO: 歌曲详情 */ }) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "详情",
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        IconButton(onClick = onMoreClick) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.MoreHoriz,
                    contentDescription = "更多",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
