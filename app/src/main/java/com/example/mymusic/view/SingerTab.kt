package com.example.mymusic.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.mymusic.presenter.SingerContract
import com.example.mymusic.presenter.SingerPresenter

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
            },
            context
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
                TabRow(
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

                            // AI歌单推荐区（占位）
                            item {
                                AIPlaylistSection()
                                Spacer(modifier = Modifier.height(16.dp))
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
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * 顶部搜索栏
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SingerTopBar(
    searchQuery: String,
    onBackClick: () -> Unit
) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 搜索框
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
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
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                // 清除按钮
                IconButton(onClick = { /* TODO */ }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "清除"
                    )
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "返回"
                )
            }
        }
    )
}

/**
 * Tab切换栏
 */
@Composable
private fun TabRow(
    selectedTab: String,
    onTabSelected: (String) -> Unit
) {
    val tabs = listOf("综合", "单曲", "歌单", "播客", "专辑", "歌手", "MV")

    ScrollableTabRow(
        selectedTabIndex = tabs.indexOf(selectedTab).coerceAtLeast(0),
        edgePadding = 0.dp,
        indicator = {},
        divider = {}
    ) {
        tabs.forEach { tab ->
            Tab(
                selected = selectedTab == tab,
                onClick = { onTabSelected(tab) },
                text = {
                    Text(
                        text = tab,
                        fontSize = 15.sp,
                        fontWeight = if (selectedTab == tab) FontWeight.Bold else FontWeight.Normal
                    )
                }
            )
        }
    }
}

/**
 * 歌手信息区域
 */
@Composable
private fun SingerInfoSection(
    artist: Artist,
    isFollowing: Boolean,
    onFollowClick: () -> Unit
) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 歌手头像
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data("file:///android_asset/${artist.avatarUrl}")
                .crossfade(true)
                .build(),
            contentDescription = "歌手头像",
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        // 歌手信息
        Column(
            modifier = Modifier.weight(1f)
        ) {
            // 歌手名称
            Text(
                text = "歌手：${artist.artistName} (${artist.description.take(10)}...)",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            // 数据统计
            Text(
                text = "${artist.songCount}首 · ${artist.fans / 10000}万粉丝",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // 乐迷团按钮
        OutlinedButton(
            onClick = onFollowClick,
            modifier = Modifier.height(36.dp),
            shape = RoundedCornerShape(18.dp),
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (isFollowing) MaterialTheme.colorScheme.primary
                else Color(0xFF4CAF50)
            ),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (isFollowing) Icons.Default.Check else Icons.Default.Favorite,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = if (isFollowing) MaterialTheme.colorScheme.primary
                    else Color(0xFF4CAF50)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (isFollowing) "已加入" else "乐迷团",
                    fontSize = 13.sp,
                    color = if (isFollowing) MaterialTheme.colorScheme.primary
                    else Color(0xFF4CAF50)
                )
            }
        }
    }
}

/**
 * AI歌单推荐区（占位）
 */
@Composable
private fun AIPlaylistSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(80.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // AI图标占位
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(24.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "AI",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "AI歌单：「${Composer.Artist?.artistName}」全生涯精选",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "为您智能推荐：${Composer.Artist?.artistName}相关歌曲",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * 单曲列表标题
 */
@Composable
private fun SongListHeader(
    songCount: Int,
    onPlayAllClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "单曲",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.width(8.dp))

        // 播放全部按钮
        IconButton(
            onClick = onPlayAllClick,
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "播放全部",
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFB74D))
                    .padding(6.dp),
                tint = Color.White
            )
        }
    }
}

/**
 * 单曲列表项
 */
@Composable
private fun SongListItem(
    song: SongDetail,
    onClick: () -> Unit,
    onMoreClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // 第一行：歌名 + 版本说明 + 播放/更多按钮
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 歌名和版本
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = song.songName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    if (song.version != null) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = song.version,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            // 播放按钮
            IconButton(onClick = onClick) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "播放",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // 更多按钮
            IconButton(onClick = onMoreClick) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "更多",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // 第二行：标签
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // 权限标签（VIP、原唱等）
            song.permissionTags.forEach { tag ->
                Surface(
                    shape = RoundedCornerShape(3.dp),
                    color = when (tag) {
                        "VIP" -> Color(0xFFEF5350)
                        else -> Color(0xFF757575)
                    }
                ) {
                    Text(
                        text = tag,
                        fontSize = 10.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            }

            // 音质标签
            song.qualityTags.forEach { tag ->
                Surface(
                    shape = RoundedCornerShape(3.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        Color(0xFFFFD700)
                    ),
                    color = Color.Transparent
                ) {
                    Text(
                        text = tag,
                        fontSize = 10.sp,
                        color = Color(0xFFFFD700),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            }
        }

        // 第三行：歌手-专辑
        Text(
            text = "${song.artist} - ${song.album}",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        // 第四行：热评或个性化文案
        if (song.hotComment != null || song.collectionInfo != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (song.hotComment != null) {
                    Text(
                        text = song.hotComment,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                }
                if (song.collectionInfo != null) {
                    Text(
                        text = song.collectionInfo,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        // 第五行：评论数等互动数据
        if (song.commentCount != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${song.commentCount} >",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * MV列表项
 */
@Composable
private fun MVListItem(
    mv: MusicVideo,
    onClick: () -> Unit
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // MV封面
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data("file:///android_asset/${mv.coverUrl}")
                        .crossfade(true)
                        .build(),
                    contentDescription = mv.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // 播放按钮覆盖层
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "播放",
                        modifier = Modifier.size(48.dp),
                        tint = Color.White
                    )
                }

                // 播放次数和时长
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomEnd)
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${mv.playCount / 10000}万次播放",
                        fontSize = 12.sp,
                        color = Color.White,
                        modifier = Modifier
                            .background(
                                color = Color.Black.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                    Text(
                        text = mv.duration,
                        fontSize = 12.sp,
                        color = Color.White,
                        modifier = Modifier
                            .background(
                                color = Color.Black.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            // MV信息
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    text = mv.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = mv.artist,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

// Composer对象用于在AI歌单区域访问当前artist
private object Composer {
    var Artist: Artist? = null
}
