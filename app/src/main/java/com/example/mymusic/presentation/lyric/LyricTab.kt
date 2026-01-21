package com.example.mymusic.presentation.lyric

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.mymusic.data.Song
import com.example.mymusic.data.repository.RepositoryProvider
import com.example.mymusic.presentation.lyric.components.LyricBottomSection
import com.example.mymusic.presentation.lyric.components.LyricDisplaySection
import com.example.mymusic.presentation.lyric.components.LyricTopSection
import com.example.mymusic.utils.AutoTestHelper
import kotlinx.coroutines.launch

/**
 * 歌词页面Tab
 * 从播放页面点击封面进入
 */
@Composable
fun LyricTab(
    songId: String,
    onBackClick: () -> Unit = {},
    onNavigateToSongProfile: (String) -> Unit = {}
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
        RepositoryProvider.initialize(context)
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

                override fun navigateToSongProfile(songId: String) {
                    onNavigateToSongProfile(songId)
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

                override fun showSuccess(message: String) {
                    android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT).show()
                }
            },
            RepositoryProvider.getSongRepository(),
            RepositoryProvider.getLyricRepository(),
            RepositoryProvider.getPlaylistRepository(),
            RepositoryProvider.getCollectionRepository()
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
                    // 顶部信息栏
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

                    // 底部播放控制栏
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
