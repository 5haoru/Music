package com.example.mymusic.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mymusic.data.Playlist
import com.example.mymusic.presenter.CollectSongContract
import com.example.mymusic.presenter.CollectSongPresenter

/**
 * 收藏到歌单页面
 * 显示所有歌单，允许用户选择将歌曲收藏到特定歌单
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectSongTab(
    songId: String,
    onBackClick: () -> Unit = {},
    onNavigateToCreatePlaylist: () -> Unit = {}
) {
    val context = LocalContext.current

    // 状态
    var playlists by remember { mutableStateOf<List<Playlist>>(emptyList()) }
    val playlistAddedStates = remember { mutableStateMapOf<String, Boolean>() }
    var isLoading by remember { mutableStateOf(false) }

    // Presenter
    val presenter = remember {
        CollectSongPresenter(
            object : CollectSongContract.View {
                override fun showPlaylists(playlistList: List<Playlist>) {
                    playlists = playlistList
                }

                override fun updatePlaylistAddedState(playlistId: String, isAdded: Boolean) {
                    playlistAddedStates[playlistId] = isAdded
                }

                override fun showSuccess(message: String) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }

                override fun close() {
                    onBackClick()
                }

                override fun navigateToCreatePlaylist() {
                    onNavigateToCreatePlaylist()
                }

                override fun showLoading() {
                    isLoading = true
                }

                override fun hideLoading() {
                    isLoading = false
                }

                override fun showError(message: String) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            },
            context
        )
    }

    // 加载数据
    LaunchedEffect(songId) {
        presenter.loadPlaylists(songId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "收藏到歌单",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { presenter.onBackClick() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                },
                actions = {
                    // 常用按钮
                    TextButton(onClick = { presenter.onCommonClick() }) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Sort,
                                contentDescription = "常用",
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("常用")
                        }
                    }
                    // 多选按钮
                    TextButton(onClick = { presenter.onMultiSelectClick() }) {
                        Text("多选")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // 新建歌单按钮
                    item {
                        NewPlaylistButton(
                            onClick = { presenter.onCreatePlaylistClick() }
                        )
                    }

                    // 歌单列表
                    items(playlists) { playlist ->
                        PlaylistItem(
                            playlist = playlist,
                            isAdded = playlistAddedStates[playlist.playlistId] == true,
                            onClick = {
                                presenter.onPlaylistClick(
                                    playlist.playlistId,
                                    playlist.playlistName
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

/**
 * 新建歌单按钮
 */
@Composable
private fun NewPlaylistButton(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 加号图标背景
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "新建",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = "新建歌单",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

/**
 * 歌单列表项
 */
@Composable
private fun PlaylistItem(
    playlist: Playlist,
    isAdded: Boolean,
    onClick: () -> Unit
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 歌单封面
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data("file:///android_asset/${playlist.coverUrl}")
                    .crossfade(true)
                    .build(),
                contentDescription = playlist.playlistName,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            // 歌单信息
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = playlist.playlistName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${playlist.songCount}首",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // 已添加标签
            if (isAdded) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "已添加",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "已添加",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}
