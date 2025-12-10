package com.example.mymusic.presentation.singer

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mymusic.data.Artist
import com.example.mymusic.data.MusicVideo
import com.example.mymusic.data.SongDetail
import com.example.mymusic.data.repository.RepositoryProvider
import com.example.mymusic.presentation.album.AlbumTab
import com.example.mymusic.presentation.albumlist.AlbumListContent
import com.example.mymusic.presentation.singer.components.*

/**
 * 歌手详情页面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingerTab(
    artistId: String,
    onBackClick: () -> Unit,
    onNavigateToPlay: (String) -> Unit = {},
    onNavigateToAlbum: (String) -> Unit = {},
    onNavigateToMVPlayer: (String) -> Unit = {}
) {
    val context = LocalContext.current

    // 状态
    var artist by remember { mutableStateOf<Artist?>(null) }
    var songs by remember { mutableStateOf<List<SongDetail>>(emptyList()) }
    var mvs by remember { mutableStateOf<List<MusicVideo>>(emptyList()) }
    var isFollowing by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf("单曲") }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showAlbumDetail by remember { mutableStateOf(false) }
    var selectedAlbumId by remember { mutableStateOf("") }

    // Presenter
    val presenter = remember {
        RepositoryProvider.initialize(context)
        SingerPresenter(
            object : SingerContract.View {
                override fun showSingerInfo(artistData: Artist) {
                    artist = artistData
                }

                override fun showSongs(songList: List<SongDetail>) {
                    songs = songList
                }

                override fun showMVs(mvList: List<MusicVideo>) {
                    mvs = mvList
                }

                override fun updateFollowStatus(following: Boolean) {
                    isFollowing = following
                }

                override fun navigateBack() {
                    onBackClick()
                }

                override fun navigateToPlay(songId: String) {
                    onNavigateToPlay(songId)
                }

                override fun navigateToMVPlayer(mvId: String) {
                    onNavigateToMVPlayer(mvId)
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
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            },
            RepositoryProvider.getArtistRepository(),
            RepositoryProvider.getSongRepository(),
            RepositoryProvider.getMusicVideoRepository(),
            RepositoryProvider.getArtistFollowRecordRepository()
        )
    }

    // 加载数据
    LaunchedEffect(artistId) {
        presenter.loadSingerData(artistId)
    }

    // 更新Composer对象供AI歌单区域使用
    LaunchedEffect(artist) {
        Composer.Artist = artist
    }

    // 如果显示专辑详情，则渲染AlbumTab
    if (showAlbumDetail && selectedAlbumId.isNotEmpty()) {
        AlbumTab(
            albumId = selectedAlbumId,
            onBackClick = {
                showAlbumDetail = false
                selectedAlbumId = ""
            },
            onNavigateToPlay = onNavigateToPlay,
            onNavigateToSinger = { artistId ->
                // 如果点击歌手信息，返回当前歌手页面
                showAlbumDetail = false
                selectedAlbumId = ""
            }
        )
        return
    }

    Scaffold(
        topBar = {
            SingerTopBar(
                searchQuery = artist?.artistName ?: "",
                onBackClick = { presenter.onBackClick() }
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Tab切换栏
                SingerTabRow(
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it }
                )

                // 内容区域
                when (selectedTab) {
                    "单曲" -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(vertical = 16.dp)
                        ) {
                            // 歌手信息区
                            artist?.let { artistData ->
                                item {
                                    SingerInfoSection(
                                        artist = artistData,
                                        isFollowing = isFollowing,
                                        onFollowClick = { presenter.onFollowClick() }
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                }
                            }

                            // AI歌单推荐区（占位符）
                            item {
                                artist?.let {
                                    AIPlaylistSection(it)
                                    Spacer(modifier = Modifier.height(16.dp))
                                }
                            }

                            // 单曲列表标题
                            item {
                                SongListHeader(
                                    songCount = songs.size,
                                    onPlayAllClick = {
                                        if (songs.isNotEmpty()) {
                                            presenter.onSongClick(songs[0].songId)
                                        }
                                    }
                                )
                            }

                            // 单曲列表
                            items(songs) { song ->
                                SongListItem(
                                    song = song,
                                    onClick = { presenter.onSongClick(song.songId) },
                                    onMoreClick = { /* TODO: 更多操作 */ }
                                )
                            }

                            // 底部留白
                            item {
                                Spacer(modifier = Modifier.height(80.dp))
                            }
                        }
                    }
                    "专辑" -> {
                        artist?.let { artistData ->
                            AlbumListContent(
                                artistId = artistData.artistId,
                                onAlbumClick = { albumId ->
                                    selectedAlbumId = albumId
                                    showAlbumDetail = true
                                }
                            )
                        }
                    }
                    "MV" -> {
                        if (mvs.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "暂无MV",
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                items(mvs) { mv ->
                                    MVListItem(
                                        mv = mv,
                                        onClick = { presenter.onMVClick(mv.mvId) }
                                    )
                                }
                                // 底部留白
                                item {
                                    Spacer(modifier = Modifier.height(64.dp))
                                }
                            }
                        }
                    }
                    else -> {
                        // 其他Tab显示占位内容
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${selectedTab}功能开发中",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

// Composer对象用于在AI歌单区域访问当前artist
private object Composer {
    var Artist: Artist? = null
}
