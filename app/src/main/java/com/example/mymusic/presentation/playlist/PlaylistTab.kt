package com.example.mymusic.presentation.playlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.mymusic.data.Playlist
import com.example.mymusic.data.Song
import com.example.mymusic.data.repository.RepositoryProvider
import com.example.mymusic.presentation.playlist.components.*
import com.example.mymusic.presentation.songdel.SongDelTab
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
    onNavigateToSongDel: (String, String) -> Unit = { _, _ -> },
    onNavigateToUnderDevelopment: (String) -> Unit = {}
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
        RepositoryProvider.initialize(context)
        PlaylistPresenter(
            object : PlaylistContract.View {
                override fun showSongs(songList: List<Song>) {
                    songs = songList
                }

                override fun navigateToPlay(song: Song) {
                    onNavigateToPlay(song)
                }

                override fun navigateToUnderDevelopment(feature: String) {
                    onNavigateToUnderDevelopment(feature)
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

                override fun showSuccess(message: String) {
                    android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT).show()
                }
            },
            RepositoryProvider.getSongRepository(),
            RepositoryProvider.getCollectionRepository()
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