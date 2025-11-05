package com.example.mymusic.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mymusic.data.Song
import com.example.mymusic.presenter.LyricContract
import com.example.mymusic.presenter.LyricPresenter
import com.example.mymusic.utils.AutoTestHelper
import kotlinx.coroutines.launch

/**
 * 歌词页面Tab
 * 从播放页面点击封面进入
 */
@Composable
fun LyricTab(
    songId: String,
    onBackClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    // 状态
    var currentSong by remember { mutableStateOf<Song?>(null) }
    var lyrics by remember { mutableStateOf<List<String>>(emptyList()) }
    var currentLyricIndex by remember { mutableStateOf(0) }
    var isPlaying by remember { mutableStateOf(true) }
    var isFavorite by remember { mutableStateOf(false) }
    var progress by remember { mutableStateOf(0f) }
    var currentTime by remember { mutableStateOf("00:00") }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Presenter
    val presenter = remember {
        LyricPresenter(
            object : LyricContract.View {
                override fun showSong(song: Song) {
                    currentSong = song
                }

                override fun showLyrics(lyricsList: List<String>) {
                    lyrics = lyricsList
                }

                override fun updateCurrentLyricIndex(index: Int) {
                    currentLyricIndex = index
                    // 自动滚动到当前歌词
                    coroutineScope.launch {
                        listState.animateScrollToItem(index)
                    }
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

                override fun closeLyricPage() {
                    onBackClick()
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
    LaunchedEffect(songId) {
        presenter.loadData(songId)
    }

    // 更新AutoTest状态，记录当前正在查看歌词
    LaunchedEffect(Unit) {
        AutoTestHelper.updateCurrentPage("lyrics")
        AutoTestHelper.updateShowLyrics(true)
    }

    // 退出时清除歌词状态
    DisposableEffect(Unit) {
        onDispose {
            AutoTestHelper.updateShowLyrics(false)
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
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                ) {
                    // 顶部信息区
                    LyricTopSection(
                        song = song,
                        onBackClick = { presenter.onBackClick() },
                        onRefreshClick = { presenter.onRefreshClick() },
                        onFollowClick = { presenter.onFollowClick() },
                        onEncyclopediaClick = { presenter.onEncyclopediaClick() }
                    )

                    // 歌词展示区（中部，占据大部分空间）
                    LyricDisplaySection(
                        lyrics = lyrics,
                        currentIndex = currentLyricIndex,
                        listState = listState,
                        modifier = Modifier.weight(1f)
                    )

                    // 底部播放控制区
                    LyricBottomSection(
                        song = song,
                        isPlaying = isPlaying,
                        isFavorite = isFavorite,
                        progress = progress,
                        currentTime = currentTime,
                        onPlayPauseClick = { presenter.onPlayPauseClick() },
                        onPreviousClick = { presenter.onPreviousClick() },
                        onNextClick = { presenter.onNextClick() },
                        onFavoriteClick = { presenter.onFavoriteClick() },
                        onMoreClick = { presenter.onMoreClick() },
                        onCommentClick = { presenter.onCommentClick() },
                        onProgressChange = { presenter.onProgressChange(it) }
                    )
                }
            }
        }
    }
}

/**
 * 顶部信息区域
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LyricTopSection(
    song: Song,
    onBackClick: () -> Unit,
    onRefreshClick: () -> Unit,
    onFollowClick: () -> Unit,
    onEncyclopediaClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // 标题栏
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "返回"
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Color(0xFFFFD700)
                ) {
                    Text(
                        text = "VIP",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = song.songName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            IconButton(onClick = onRefreshClick) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "刷新"
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 歌手信息和关注按钮
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = song.artist,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedButton(
                onClick = onFollowClick,
                modifier = Modifier.height(28.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
            ) {
                Text(
                    text = "关注",
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 歌曲百科入口
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onEncyclopediaClick)
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "歌曲百科",
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "歌曲百科",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 制作信息
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            CreatorInfoRow(label = "作词:", creator = song.artist)
            Spacer(modifier = Modifier.height(4.dp))
            CreatorInfoRow(label = "作曲:", creator = song.artist)
            Spacer(modifier = Modifier.height(4.dp))
            CreatorInfoRow(label = "编曲:", creator = "郑伟 / 张宝宇")
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "制作人:        赵英俊",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * 制作人员信息行
 */
@Composable
private fun CreatorInfoRow(label: String, creator: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(4.dp))
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = creator,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

/**
 * 歌词展示区域（中部）
 */
@Composable
private fun LyricDisplaySection(
    lyrics: List<String>,
    currentIndex: Int,
    listState: androidx.compose.foundation.lazy.LazyListState,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 添加顶部空间
        item {
            Spacer(modifier = Modifier.height(100.dp))
        }

        itemsIndexed(lyrics) { index, lyric ->
            val isCurrent = index == currentIndex
            val alpha = when {
                isCurrent -> 1f
                index == currentIndex - 1 || index == currentIndex + 1 -> 0.6f
                else -> 0.3f
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = if (isCurrent) Arrangement.SpaceBetween else Arrangement.Center
            ) {
                // 当前歌词左侧显示时间
                if (isCurrent) {
                    Text(
                        text = "00:${String.format("%02d", index * 5)}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.alpha(0.5f)
                    )
                }

                // 歌词文本
                Text(
                    text = lyric,
                    fontSize = if (isCurrent) 18.sp else 14.sp,
                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .alpha(alpha)
                        .weight(1f),
                    color = MaterialTheme.colorScheme.onSurface
                )

                // 当前歌词右侧显示播放图标
                if (isCurrent) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "播放",
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                }
            }
        }

        // 添加底部空间
        item {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

/**
 * 底部播放控制区域
 */
@Composable
private fun LyricBottomSection(
    song: Song,
    isPlaying: Boolean,
    isFavorite: Boolean,
    progress: Float,
    currentTime: String,
    onPlayPauseClick: () -> Unit,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onMoreClick: () -> Unit,
    onCommentClick: () -> Unit,
    onProgressChange: (Float) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        // 互动按钮行
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 更多选项
            IconButton(onClick = onMoreClick) {
                Icon(
                    imageVector = Icons.Default.MoreHoriz,
                    contentDescription = "更多",
                    modifier = Modifier.size(24.dp)
                )
            }

            // 收藏（红心）
            IconButton(onClick = onFavoriteClick) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = "收藏",
                    modifier = Modifier.size(28.dp),
                    tint = if (isFavorite) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // 麦克风
            IconButton(onClick = { /* TODO: 打开K歌模式 */ }) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = "K歌",
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 进度条
        Column {
            Slider(
                value = progress,
                onValueChange = onProgressChange,
                modifier = Modifier.fillMaxWidth()
            )

            val totalDuration = song.duration
            val totalMinutes = (totalDuration / 1000) / 60
            val totalSeconds = (totalDuration / 1000) % 60
            val totalTimeString = String.format("%02d:%02d", totalMinutes, totalSeconds)

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

        Spacer(modifier = Modifier.height(16.dp))

        // 播放控制栏
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 播放模式（循环）
            IconButton(onClick = { /* TODO: 切换播放模式 */ }) {
                Icon(
                    imageVector = Icons.Default.Repeat,
                    contentDescription = "循环",
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

        Spacer(modifier = Modifier.height(16.dp))

        // 次要功能栏
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = onCommentClick) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Comment,
                        contentDescription = "评论",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            IconButton(onClick = { /* TODO: 音效 */ }) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.GraphicEq,
                        contentDescription = "音效",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            IconButton(onClick = { /* TODO: 详情 */ }) {
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
}
