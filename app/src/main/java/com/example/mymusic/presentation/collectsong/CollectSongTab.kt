package com.example.mymusic.presentation.collectsong

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mymusic.data.Playlist
import com.example.mymusic.data.repository.RepositoryProvider
import com.example.mymusic.presentation.collectsong.components.NewPlaylistButton
import com.example.mymusic.presentation.collectsong.components.PlaylistItem

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
    val context = LocalContext.current.applicationContext

    // 状态
    var playlists by remember { mutableStateOf<List<Playlist>>(emptyList()) }
    val playlistAddedStates = remember { mutableStateMapOf<String, Boolean>() }
    var isLoading by remember { mutableStateOf(false) }
    var refreshTrigger by remember { mutableStateOf(0) }

    // Presenter
    val presenter = remember {
        RepositoryProvider.initialize(context)
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
            RepositoryProvider.getPlaylistRepository(),
            RepositoryProvider.getSongRepository(),
            RepositoryProvider.getCollectionRepository()
        )
    }

    // 加载数据 - 当songId或refreshTrigger变化时重新加载
    LaunchedEffect(songId, refreshTrigger) {
        presenter.loadPlaylists(songId)
    }

    // 当页面重新显示时自动刷新
    DisposableEffect(Unit) {
        refreshTrigger++
        onDispose { }
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
                    // 刷新按钮
                    IconButton(onClick = { refreshTrigger++ }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "刷新"
                        )
                    }
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