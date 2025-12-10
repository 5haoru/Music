package com.example.mymusic.presentation.searchresult

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mymusic.data.Artist
import com.example.mymusic.data.MusicVideo
import com.example.mymusic.data.SongDetail
import com.example.mymusic.presentation.searchresult.components.*

/**
 * 搜索结果页面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResultTab(
    searchQuery: String,
    onBackClick: () -> Unit,
    onNavigateToPlay: (String) -> Unit = {},
    onNavigateToSinger: (String) -> Unit = {}
) {
    val context = LocalContext.current

    // 状态
    var artist by remember { mutableStateOf<Artist?>(null) }
    var musicVideo by remember { mutableStateOf<MusicVideo?>(null) }
    var songResults by remember { mutableStateOf<List<SongDetail>>(emptyList()) }
    var selectedCategory by remember { mutableStateOf("综合") }
    var isFollowing by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Presenter
    val presenter = remember {
        SearchResultPresenter(
            object : SearchResultContract.View {
                override fun showArtist(artistData: Artist?) {
                    artist = artistData
                }

                override fun showMusicVideo(mv: MusicVideo?) {
                    musicVideo = mv
                }

                override fun showSongResults(songs: List<SongDetail>) {
                    songResults = songs
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

                override fun navigateBack() {
                    onBackClick()
                }

                override fun updateFollowStatus(following: Boolean) {
                    isFollowing = following
                }

                override fun navigateToPlay(songId: String) {
                    onNavigateToPlay(songId)
                }

                override fun navigateToSinger(artistId: String) {
                    onNavigateToSinger(artistId)
                }
            },
            context
        )
    }

    // 加载搜索结果
    LaunchedEffect(searchQuery) {
        presenter.loadSearchResults(searchQuery)
    }

    Scaffold(
        topBar = {
            SearchResultTopBar(
                searchQuery = searchQuery,
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
                // 分类标签行
                CategoryTabsRow(
                    selectedCategory = selectedCategory,
                    onCategorySelected = {
                        selectedCategory = it
                        presenter.onCategorySelected(it)
                    }
                )

                // 滚动内容
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    // 歌手信息卡片
                    artist?.let { artistData ->
                        item {
                            ArtistCard(
                                artist = artistData,
                                isFollowing = isFollowing,
                                onFollowClick = { presenter.onFollowArtist(artistData.artistId) },
                                onArtistClick = { presenter.onArtistClick(artistData.artistId) }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }

                    // MV区域
                    musicVideo?.let { mv ->
                        item {
                            MVSection(
                                mv = mv,
                                onMVClick = { presenter.onMVClick(mv.mvId) }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }

                    // 单曲列表
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "单曲",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary)
                                    .padding(6.dp),
                                tint = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    items(songResults) { songDetail ->
                        SongDetailItem(
                            songDetail = songDetail,
                            onSongClick = { presenter.onSongClick(songDetail.songId) }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }
    }
}