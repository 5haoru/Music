package com.example.mymusic.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.mymusic.data.Playlist
import com.example.mymusic.data.Song
import com.example.mymusic.presenter.RecommendContract
import com.example.mymusic.presenter.RecommendPresenter
import com.example.mymusic.ui.components.*

/**
 * 推荐页面Tab
 */
@Composable
fun RecommendTab() {
    val context = LocalContext.current

    // 状态
    var recommendedSongs by remember { mutableStateOf<List<Song>>(emptyList()) }
    var dailyPlaylists by remember { mutableStateOf<List<Playlist>>(emptyList()) }
    var rankingPlaylists by remember { mutableStateOf<List<Playlist>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Presenter
    val presenter = remember {
        RecommendPresenter(
            object : RecommendContract.View {
                override fun showRecommendedSongs(songs: List<Song>) {
                    recommendedSongs = songs
                }

                override fun showDailyRecommendPlaylists(playlists: List<Playlist>) {
                    dailyPlaylists = playlists
                }

                override fun showRankingPlaylists(playlists: List<Playlist>) {
                    rankingPlaylists = playlists
                }

                override fun playSong(song: Song) {
                    // TODO: 实现播放功能
                }

                override fun openPlaylist(playlist: Playlist) {
                    // TODO: 打开歌单详情
                }

                override fun openSearch() {
                    // TODO: 打开搜索页面
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

    Scaffold(
        topBar = {
            RecommendTopBar(
                onSearchClick = { presenter.onSearchClick() }
            )
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (errorMessage != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text(text = errorMessage ?: "")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // 每日推荐区域
                item {
                    SectionTitle(title = "每日推荐")
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(dailyPlaylists) { playlist ->
                            LargePlaylistCard(
                                playlist = playlist,
                                title = when (playlist.playlistId) {
                                    "playlist_001" -> "每日推荐"
                                    "playlist_002" -> "热歌榜"
                                    else -> "歌单推荐"
                                },
                                onClick = { presenter.onPlaylistClick(playlist.playlistId) }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }

                // 推荐歌曲区域
                item {
                    SectionTitle(title = "根据你喜爱的歌曲推荐")
                }
                items(recommendedSongs) { song ->
                    SongItem(
                        song = song,
                        onSongClick = { presenter.onSongClick(song.songId) },
                        onPlayClick = { presenter.onSongClick(song.songId) }
                    )
                }

                // 歌单推荐区域
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    SectionTitle(title = "精选歌单")
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(dailyPlaylists.drop(3).take(4)) { playlist ->
                            PlaylistCard(
                                playlist = playlist,
                                onClick = { presenter.onPlaylistClick(playlist.playlistId) }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }

                // 排行榜区域
                item {
                    SectionTitle(title = "排行榜")
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(rankingPlaylists) { playlist ->
                            PlaylistCard(
                                playlist = playlist,
                                onClick = { presenter.onPlaylistClick(playlist.playlistId) },
                                showDescription = false
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(80.dp)) // 为底部导航栏留空间
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        modifier = modifier.padding(horizontal = 16.dp, vertical = 12.dp)
    )
}
