package com.example.mymusic.presentation.stroll

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mymusic.data.Song
import com.example.mymusic.presentation.stroll.StrollContract
import com.example.mymusic.presentation.stroll.StrollPresenter
import com.example.mymusic.presentation.stroll.components.*
import com.example.mymusic.presentation.comment.CommentTab
import com.example.mymusic.presentation.share.ShareTab
import com.example.mymusic.presentation.lyric.LyricTab
import com.example.mymusic.presentation.playcustomize.PlayCustomizeTab
import com.example.mymusic.presentation.player.PlayerTab
import com.example.mymusic.presentation.songprofile.SongProfileTab
import com.example.mymusic.presentation.collectsong.CollectSongTab
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 漫游页面Tab
 */
@Composable
fun StrollTab(
    currentMode: String = "伤感",
    onBackToRecommend: () -> Unit = {},
    onNavigateToModeSelection: () -> Unit = {},
    onNavigateToUnderDevelopment: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // 状《
    var currentSong by remember { mutableStateOf<Song?>(null) }
    var isPlaying by remember { mutableStateOf(false) }
    var isFavorite by remember { mutableStateOf(false) }
    var progress by remember { mutableStateOf(0f) }
    var currentTime by remember { mutableStateOf("00:00") }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showComment by remember { mutableStateOf(false) } // 评论页面状《
    var showShare by remember { mutableStateOf(false) } // 分享弹窗状《
    var showLyric by remember { mutableStateOf(false) } // 歌词页面状《
    var showPlayCustomize by remember { mutableStateOf(false) } // 播放定制弹窗状《
    var showPlayerStyle by remember { mutableStateOf(false) } // 播放器样式页面状《
    var showSongProfile by remember { mutableStateOf(false) } // 歌曲档案页面状《
    var showCollectSong by remember { mutableStateOf(false) } // 收藏到歌单页面状《

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

                override fun navigateToComment(songId: String) {
                    showComment = true
                }

                override fun showSuccess(message: String) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
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
                    delay(1000) // 每秒更新一《
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
                delay(1000) // 每秒更新一《
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
                    // 顶部《
                    StrollTopBar(
                        currentMode = currentMode,
                        onBackClick = onBackToRecommend,
                        onShareClick = { showShare = true },
                        onTitleClick = onNavigateToModeSelection
                    )

                    // 中央播放器区《
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
                        // 进度《
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
                            onMoreClick = { showPlayCustomize = true }
                        )
                    }
                }
            }
        }

        // 评论页面（作为全屏页面显示）
        if (showComment && currentSong != null) {
            CommentTab(
                songId = currentSong!!.songId,
                onBackClick = { showComment = false }
            )
        }

        // 分享弹窗（作为overlay层叠加显示）
        if (showShare && currentSong != null) {
            ShareTab(
                song = currentSong!!,
                onCloseClick = { showShare = false },
                onNavigateToUnderDevelopment = { feature ->
                    showShare = false
                    onNavigateToUnderDevelopment(feature)
                }
            )
        }

        // 歌词页面（作为全屏页面显示）
        if (showLyric && currentSong != null) {
            LyricTab(
                songId = currentSong!!.songId,
                onBackClick = { showLyric = false },
                onNavigateToSongProfile = {
                    showLyric = false
                    showSongProfile = true
                }
            )
        }

        // 播放定制弹窗（作为overlay层叠加显示）
        if (showPlayCustomize && currentSong != null) {
            PlayCustomizeTab(
                song = currentSong!!,
                onCloseClick = { showPlayCustomize = false },
                onShareClick = { showShare = true },
                onNavigateToPlayerStyle = {
                    showPlayCustomize = false
                    showPlayerStyle = true
                },
                onNavigateToSongProfile = {
                    showPlayCustomize = false
                    showSongProfile = true
                },
                onNavigateToCollectSong = {
                    showPlayCustomize = false
                    showCollectSong = true
                }
            )
        }

        // 播放器样式选择页面（作为全屏页面显示）
        if (showPlayerStyle) {
            PlayerTab(
                onBackClick = { showPlayerStyle = false }
            )
        }

        // 歌曲档案页面（作为全屏页面显示）
        if (showSongProfile && currentSong != null) {
            SongProfileTab(
                songId = currentSong!!.songId,
                onBackClick = { showSongProfile = false }
            )
        }

        // 收藏到歌单页面（作为全屏页面显示）
        if (showCollectSong && currentSong != null) {
            CollectSongTab(
                songId = currentSong!!.songId,
                onBackClick = { showCollectSong = false },
                onNavigateToCreatePlaylist = {
                    // 跳转到创建歌单页面（暂未实现）
                }
            )
        }
    }
}
