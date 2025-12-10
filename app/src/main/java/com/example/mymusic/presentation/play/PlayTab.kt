package com.example.mymusic.presentation.play

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.example.mymusic.data.repository.RepositoryProvider
import com.example.mymusic.presentation.collectsong.CollectSongTab
import com.example.mymusic.presentation.play.components.*
import com.example.mymusic.presentation.playcustomize.PlayCustomizeTab
import com.example.mymusic.presentation.player.PlayerTab
import com.example.mymusic.presentation.share.ShareTab
import com.example.mymusic.presentation.songprofile.SongProfileTab

/**
 * 播放页面Tab
 */
@Composable
fun PlayTab(
    initialSongId: String? = null,
    onBackToRecommend: () -> Unit = {},
    onNavigateToComment: (String) -> Unit = {},
    onNavigateToLyric: (String) -> Unit = {}
) {
    val context = LocalContext.current

    // 状态
    var currentSong by remember { mutableStateOf<Song?>(null) }
    var isPlaying by remember { mutableStateOf(false) }
    var isFavorite by remember { mutableStateOf(false) }
    var progress by remember { mutableStateOf(0f) }
    var currentTime by remember { mutableStateOf("00:00") }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var playMode by remember { mutableStateOf(PlayMode.SEQUENCE) }
    var showShare by remember { mutableStateOf(false) } // 分享弹窗状态
    var showPlayCustomize by remember { mutableStateOf(false) } // 播放定制弹窗状态
    var showPlayerStyle by remember { mutableStateOf(false) } // 播放器样式页面状态
    var showSongProfile by remember { mutableStateOf(false) } // 歌曲档案页面状态
    var showCollectSong by remember { mutableStateOf(false) } // 收藏到歌单页面状态

    // Presenter
    val presenter = remember {
        RepositoryProvider.initialize(context)
        PlayPresenter(
            object : PlayContract.View {
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

                override fun updatePlayMode(mode: PlayMode) {
                    playMode = mode
                }

                override fun navigateToComment(songId: String) {
                    onNavigateToComment(songId)
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
            RepositoryProvider.getSongRepository(),
            RepositoryProvider.getPlaylistRepository(),
            RepositoryProvider.getCollectionRepository()
        )
    }

    // 加载数据
    LaunchedEffect(Unit) {
        if (initialSongId != null) {
            presenter.loadSongById(initialSongId)
        } else {
            presenter.loadData()
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
                    PlayTopBar(
                        onBackClick = onBackToRecommend,
                        onShareClick = { showShare = true } // 显示分享弹窗
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
                                .clip(RoundedCornerShape(16.dp))
                                .clickable {
                                    // 点击封面进入歌词页面
                                    onNavigateToLyric(song.songId)
                                },
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        // 歌曲信息
                        PlaySongInfoSection(
                            song = song,
                            isFavorite = isFavorite,
                            onFavoriteClick = { presenter.onFavoriteClick() },
                            onCommentClick = { presenter.onCommentClick() }
                        )
                    }

                    // 进度条和控制按钮区域
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 16.dp)
                    ) {
                        // 进度条
                        PlayProgressSection(
                            song = song,
                            progress = progress,
                            currentTime = currentTime,
                            onProgressChange = { presenter.onProgressChange(it) }
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // 播放控制按钮
                        PlayControlSection(
                            isPlaying = isPlaying,
                            playMode = playMode,
                            onPreviousClick = { presenter.onPreviousClick() },
                            onPlayPauseClick = { presenter.onPlayPauseClick() },
                            onNextClick = { presenter.onNextClick() },
                            onPlayModeClick = { presenter.onPlayModeClick() }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // 底部功能按钮
                        PlayBottomFunctionBar(
                            onCommentClick = { presenter.onCommentClick() },
                            onMoreClick = { showPlayCustomize = true }
                        )
                    }
                }
            }
        }

        // 分享弹窗（作为overlay层叠加显示）
        if (showShare && currentSong != null) {
            ShareTab(
                song = currentSong!!,
                onCloseClick = { showShare = false }
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