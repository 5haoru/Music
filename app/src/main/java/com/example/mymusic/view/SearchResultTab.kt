package com.example.mymusic.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mymusic.data.Artist
import com.example.mymusic.data.MusicVideo
import com.example.mymusic.data.SongDetail
import com.example.mymusic.presenter.SearchResultContract
import com.example.mymusic.presenter.SearchResultPresenter

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
                // 分类标签栏
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

/**
 * 搜索结果顶部栏
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchResultTopBar(
    searchQuery: String,
    onBackClick: () -> Unit
) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "搜索",
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = searchQuery,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "返回"
                )
            }
        },
        actions = {
            IconButton(onClick = { /* 清除搜索 */ }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "清除"
                )
            }
        }
    )
}

/**
 * 分类标签行
 */
@Composable
private fun CategoryTabsRow(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    val categories = listOf("综合", "单曲", "歌单", "播客", "专辑", "歌手", "笔记")

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(categories) { category ->
            Text(
                text = category,
                modifier = Modifier.clickable { onCategorySelected(category) },
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (category == selectedCategory) FontWeight.Bold else FontWeight.Normal,
                color = if (category == selectedCategory)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * 歌手信息卡片
 */
@Composable
private fun ArtistCard(
    artist: Artist,
    isFollowing: Boolean,
    onFollowClick: () -> Unit,
    onArtistClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onArtistClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 歌手头像
        Box(
            modifier = Modifier.size(60.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("file:///android_asset/${artist.avatarUrl}")
                    .crossfade(true)
                    .build(),
                contentDescription = artist.artistName,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            // V标识
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "认证",
                modifier = Modifier
                    .size(18.dp)
                    .align(Alignment.BottomEnd)
                    .background(Color.White, CircleShape),
                tint = Color.Red
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // 歌手信息
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "歌手: ${artist.artistName} (Joker Xue)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${artist.songCount}首 · ${artist.fans / 10000.0}万粉丝",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // 关注按钮
        OutlinedButton(
            onClick = onFollowClick,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.height(36.dp)
        ) {
            Text(
                text = if (isFollowing) "已关注" else "+ 关注",
                fontSize = 14.sp
            )
        }
    }
}

/**
 * MV区域
 */
@Composable
private fun MVSection(
    mv: MusicVideo,
    onMVClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "MV: ${mv.title}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onMVClick),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // MV封面
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("file:///android_asset/${mv.coverUrl}")
                        .crossfade(true)
                        .build(),
                    contentDescription = mv.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                // 播放按钮
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "播放",
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.Center)
                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                        .padding(8.dp),
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // MV信息
            Column {
                Text(
                    text = mv.artist,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${mv.duration}, 播放:${mv.playCount / 10000.0}万",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * 歌曲详细信息项
 */
@Composable
private fun SongDetailItem(
    songDetail: SongDetail,
    onSongClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSongClick)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // 歌曲名和版本
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = Color.Red
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = songDetail.songName + (songDetail.version ?: ""),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "播放",
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "更多",
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 标签行
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 音质和权限标签
            (songDetail.qualityTags + songDetail.permissionTags).forEach { tag ->
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = if (tag == "超清母带") Color(0xFFFFE0B2) else Color(0xFFE0E0E0),
                    modifier = Modifier.height(18.dp)
                ) {
                    Text(
                        text = tag,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        fontSize = 10.sp,
                        color = if (tag == "VIP") Color(0xFFD32F2F) else Color.Black
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
            }

            Text(
                text = "${songDetail.artist} - ${songDetail.album}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // 热评或收藏信息
        if (songDetail.hotComment != null || songDetail.collectionInfo != null) {
            Spacer(modifier = Modifier.height(6.dp))
            songDetail.hotComment?.let {
                Text(
                    text = "热评：\"$it\"",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            songDetail.collectionInfo?.let {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp
                    )
                    if (songDetail.commentCount != null) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = songDetail.commentCount ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}
