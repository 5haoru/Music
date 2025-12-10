package com.example.mymusic.presentation.recommend

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mymusic.data.Playlist
import com.example.mymusic.data.Song
import com.example.mymusic.presentation.dailyrecommend.DailyRecommendTab
import com.example.mymusic.presentation.listenrecognize.ListenRecognizeTab
import com.example.mymusic.presentation.playlist.PlaylistTab
import com.example.mymusic.presentation.rank.RankListTab
import com.example.mymusic.presentation.recommend.RecommendContract
import com.example.mymusic.presentation.recommend.RecommendPresenter
import com.example.mymusic.presentation.search.SearchTab
import com.example.mymusic.data.repository.RankRepository
import com.example.mymusic.ui.components.*

/**
 * 推荐页面Tab
 */
@Composable
fun RecommendTab(
    onNavigateToListenRecognize: () -> Unit = {},
    onNavigateToSearchResult: (String) -> Unit = {},
    onNavigateToPlay: (String) -> Unit = {}
) {
    val context = LocalContext.current

    // 导航状�?
    var showDailyRecommendDetail by remember { mutableStateOf(false) }
    var showRankListDetail by remember { mutableStateOf(false) }
    var showSearchPage by remember { mutableStateOf(false) }
    var showPlaylistDetail by remember { mutableStateOf(false) }
    var selectedPlaylistForDetail by remember { mutableStateOf<Playlist?>(null) }

    if (showDailyRecommendDetail) {
        DailyRecommendTab(
            onBackClick = { showDailyRecommendDetail = false },
            onNavigateToPlay = onNavigateToPlay,
            onNavigateToListenRecognize = onNavigateToListenRecognize,
            onNavigateToSearchResult = onNavigateToSearchResult
        )
        return
    }

    if (showRankListDetail) {
        RankListTab(
            onBackClick = { showRankListDetail = false },
            onNavigateToPlay = onNavigateToPlay
        )
        return
    }

    if (showSearchPage) {
        SearchTab(
            onBackClick = { showSearchPage = false },
            onNavigateToSearchResult = onNavigateToSearchResult
        )
        return
    }

    if (showPlaylistDetail && selectedPlaylistForDetail != null) {
        PlaylistTab(
            playlist = selectedPlaylistForDetail!!,
            onBackClick = {
                showPlaylistDetail = false
                selectedPlaylistForDetail = null
            },
            onNavigateToPlay = { /* TODO: 导航到播放页面 */ },
            onNavigateToSetting = { /* TODO: 导航到设置页面 */ },
            onNavigateToSongDel = { _, _ -> /* TODO: 导航到删除页面 */ }
        )
        return
    }

    // 状态
    var recommendedSongs by remember { mutableStateOf<List<Song>>(emptyList()) }
    var dailyPlaylists by remember { mutableStateOf<List<Playlist>>(emptyList()) }
    var featuredPlaylists by remember { mutableStateOf<List<Playlist>>(emptyList()) }
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

                override fun showFeaturedPlaylists(playlists: List<Playlist>) {
                    featuredPlaylists = playlists
                }

                override fun showRankingPlaylists(playlists: List<Playlist>) {
                    rankingPlaylists = playlists
                }

                override fun playSong(song: Song) {
                    // TODO: 实现播放功能
                }

                override fun openPlaylist(playlist: Playlist) {
                    if (playlist.playlistId == "playlist_001") {
                        showDailyRecommendDetail = true
                    } else {
                        selectedPlaylistForDetail = playlist
                        showPlaylistDetail = true
                    }
                }

                override fun openSearch() {
                    showSearchPage = true
                }

                override fun openListenRecognize() {
                    onNavigateToListenRecognize()
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
            context
        )
    }

    LaunchedEffect(Unit) {
        presenter.loadData()
    }

    Scaffold(
        topBar = {
            RecommendTopBar(
                onSearchClick = { presenter.onSearchClick() },
                onMicClick = { presenter.onMicClick() }
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
                                    else -> playlist.playlistName // Use the name from data
                                },
                                onClick = { presenter.onPlaylistClick(playlist) }
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

                // 精选歌单区域
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    SectionTitle(title = "精选歌单")
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(featuredPlaylists) { playlist ->
                            PlaylistCard(
                                playlist = playlist,
                                onClick = { presenter.onPlaylistClick(playlist) }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }

                // 排行榜区域
                item {
                    SectionTitleWithAction(
                        title = "排行榜",
                        onActionClick = { showRankListDetail = true }
                    )
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(rankingPlaylists) { playlist ->
                            PlaylistCard(
                                playlist = playlist, // Directly use the playlist from presenter
                                onClick = { presenter.onPlaylistClick(playlist) },
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

@Composable
private fun SectionTitleWithAction(
    title: String,
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onActionClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = "查看更多",
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}
