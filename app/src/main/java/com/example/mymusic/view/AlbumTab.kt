package com.example.mymusic.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.example.mymusic.data.Album
import com.example.mymusic.data.Song
import com.example.mymusic.presenter.AlbumContract
import com.example.mymusic.presenter.AlbumPresenter

/**
 * 专辑详情页面
 */
@OptIn(ExperimentalMaterial3Api::class)
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
            context
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

/**
 * 顶部栏
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AlbumTopBar(
    onBackClick: () -> Unit,
    onMoreClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "专辑",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
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
            IconButton(onClick = onMoreClick) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "更多"
                )
            }
        }
    )
}

/**
 * 专辑信息区域
 */
@Composable
private fun AlbumInfoSection(
    album: Album,
    isDescriptionExpanded: Boolean,
    onToggleDescription: () -> Unit,
    onArtistClick: () -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 专辑封面
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data("file:///android_asset/${album.coverUrl}")
                    .crossfade(true)
                    .build(),
                contentDescription = "专辑封面",
                modifier = Modifier
                    .size(150.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            // 专辑信息
            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.Top)
            ) {
                // 专辑名
                Text(
                    text = album.albumName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 歌手信息
                Row(
                    modifier = Modifier.clickable(onClick = onArtistClick),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "歌手：${album.artist}",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 发行时间
                Text(
                    text = "发行时间: ${album.releaseDate}",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 专辑描述
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onToggleDescription)
        ) {
            Text(
                text = album.description,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = if (isDescriptionExpanded) Int.MAX_VALUE else 2,
                overflow = TextOverflow.Ellipsis
            )
            if (!isDescriptionExpanded) {
                Text(
                    text = "展开 ▼",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

/**
 * 互动按钮行
 */
@Composable
private fun ActionButtonsRow(
    collectCount: Int,
    commentCount: Int,
    shareCount: Int,
    onCollectClick: () -> Unit,
    onCommentClick: () -> Unit,
    onShareClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 收藏按钮
        ActionButton(
            icon = Icons.Default.Add,
            text = formatCount(collectCount),
            onClick = onCollectClick,
            modifier = Modifier.weight(1f)
        )

        // 评论按钮
        ActionButton(
            icon = Icons.Default.Comment,
            text = formatCount(commentCount),
            onClick = onCommentClick,
            modifier = Modifier.weight(1f)
        )

        // 分享按钮
        ActionButton(
            icon = Icons.Default.Share,
            text = formatCount(shareCount),
            onClick = onShareClick,
            modifier = Modifier.weight(1f)
        )
    }
}

/**
 * 互动按钮
 */
@Composable
private fun ActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(40.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = text,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * 热卖横幅
 */
@Composable
private fun HotSaleBanner(artist: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(48.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.ShoppingCart,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = Color(0xFFFFB74D)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "${artist}实体专辑热卖中",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = "立即支持 >",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

/**
 * 播放全部区域
 */
@Composable
private fun PlayAllSection(
    songCount: Int,
    onPlayAllClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 播放全部按钮
        IconButton(
            onClick = onPlayAllClick,
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "播放全部",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFB74D))
                    .padding(12.dp),
                tint = Color.White
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "播放全部",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "($songCount)",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // 购物车图标
        IconButton(onClick = { /* TODO */ }) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = "购物车",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // 下载图标
        IconButton(onClick = { /* TODO */ }) {
            Icon(
                imageVector = Icons.Default.CloudDownload,
                contentDescription = "下载",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // 列表图标
        IconButton(onClick = { /* TODO */ }) {
            Icon(
                imageVector = Icons.Default.PlaylistPlay,
                contentDescription = "列表",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * 专辑歌曲列表项
 */
@Composable
private fun AlbumSongItem(
    index: Int,
    song: Song,
    onClick: () -> Unit,
    onMoreClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 序号
            Text(
                text = index.toString(),
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.width(30.dp)
            )

            // 歌曲信息
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // 歌名和标签
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

                    Spacer(modifier = Modifier.width(4.dp))

                    // VIP标签
                    if (index % 2 == 0) {
                        Surface(
                            shape = RoundedCornerShape(3.dp),
                            color = Color(0xFFEF5350)
                        ) {
                            Text(
                                text = "VIP",
                                fontSize = 10.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                    }

                    // 音质标签
                    if (index % 3 == 0) {
                        Surface(
                            shape = RoundedCornerShape(3.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFD700)),
                            color = Color.Transparent
                        ) {
                            Text(
                                text = "超清母带",
                                fontSize = 10.sp,
                                color = Color(0xFFFFD700),
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // 歌手
                Text(
                    text = song.artist,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
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
    }
}

/**
 * 格式化数量显示
 */
private fun formatCount(count: Int): String {
    return when {
        count >= 10000 -> "${count / 10000}万"
        else -> count.toString()
    }
}
