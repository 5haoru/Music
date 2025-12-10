package com.example.mymusic.presentation.album

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.mymusic.data.Album
import com.example.mymusic.data.Song
import com.example.mymusic.data.repository.AlbumRepository
import com.example.mymusic.data.repository.SongRepository
import com.example.mymusic.presentation.album.components.*

/**
 * 专辑详情页面
 */
@Composable
fun AlbumTab(
    albumId: String,
    onBackClick: () -> Unit,
    onNavigateToPlay: (String) -> Unit = {},
    onNavigateToSinger: (String) -> Unit = {}
) {
    val context = LocalContext.current

    // 状态
    var album by remember { mutableStateOf<Album?>(null) }
    var songs by remember { mutableStateOf<List<Song>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isDescriptionExpanded by remember { mutableStateOf(false) }

    // Presenter
    val presenter = remember {
        val albumRepository = AlbumRepository(context)
        val songRepository = SongRepository(context)
        AlbumPresenter(
            object : AlbumContract.View {
                override fun showAlbumInfo(albumData: Album) {
                    album = albumData
                }

                override fun showSongs(songList: List<Song>) {
                    songs = songList
                }

                override fun navigateBack() {
                    onBackClick()
                }

                override fun navigateToPlay(songId: String) {
                    onNavigateToPlay(songId)
                }

                override fun navigateToSinger(artistId: String) {
                    onNavigateToSinger(artistId)
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
            albumRepository,
            songRepository
        )
    }

    // 加载数据
    LaunchedEffect(albumId) {
        presenter.loadAlbumDetail(albumId)
    }

    Scaffold(
        topBar = {
            AlbumTopBar(
                onBackClick = { presenter.onBackClick() },
                onMoreClick = { /* TODO */ }
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
                    .padding(paddingValues),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                // 专辑信息区
                album?.let { albumData ->
                    item {
                        AlbumInfoSection(
                            album = albumData,
                            isDescriptionExpanded = isDescriptionExpanded,
                            onToggleDescription = { isDescriptionExpanded = !isDescriptionExpanded },
                            onArtistClick = { presenter.onArtistClick() }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // 互动按钮行
                    item {
                        ActionButtonsRow(
                            collectCount = albumData.collectCount,
                            commentCount = albumData.commentCount,
                            shareCount = albumData.shareCount,
                            onCollectClick = { presenter.onCollectClick() },
                            onCommentClick = { presenter.onCommentClick() },
                            onShareClick = { presenter.onShareClick() }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // 热卖横幅
                    item {
                        HotSaleBanner(artist = albumData.artist)
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // 播放全部区域
                    item {
                        PlayAllSection(
                            songCount = albumData.songCount,
                            onPlayAllClick = { presenter.onPlayAllClick() }
                        )
                    }
                }

                // 歌曲列表
                itemsIndexed(songs) { index, song ->
                    AlbumSongItem(
                        index = index + 1,
                        song = song,
                        onClick = { presenter.onSongClick(song.songId) },
                        onMoreClick = { /* TODO */ }
                    )
                }

                // 底部留白
                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}