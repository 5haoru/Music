package com.example.mymusic.presentation.playlistsetting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.mymusic.data.Playlist
import com.example.mymusic.data.repository.RepositoryProvider
import com.example.mymusic.presentation.playlistsetting.PlaylistSettingContract
import com.example.mymusic.presentation.playlistsetting.PlaylistSettingPresenter
import com.example.mymusic.presentation.playlistsetting.components.*

/**
 * 歌单设置页面Tab（三级页面）
 * 从歌单详情页点击右上角三个点进入
 */
@Composable
fun PlaylistSettingTab(
    playlist: Playlist,
    onBackClick: () -> Unit = {},
    onNavigateToSongSort: (Playlist) -> Unit = {}
) {
    val context = LocalContext.current

    // 状态
    var playlistInfo by remember { mutableStateOf(playlist) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var wifiAutoDownload by remember { mutableStateOf(false) }
    var showAlbumCover by remember { mutableStateOf(true) }

    // Presenter
    val presenter = remember {
        RepositoryProvider.initialize(context)
        PlaylistSettingPresenter(
            object : PlaylistSettingContract.View {
                override fun showPlaylistInfo(playlist: Playlist) {
                    playlistInfo = playlist
                }

                override fun navigateBack() {
                    onBackClick()
                }

                override fun showToast(message: String) {
                    android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT).show()
                }

                override fun navigateToSongSort(playlist: Playlist) {
                    onNavigateToSongSort(playlist)
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
                    android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT).show()
                }

                override fun showSuccess(message: String) {
                    android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT).show()
                }
            },
            RepositoryProvider.getPlaylistRepository(),
            context
        )
    }

    // 加载数据
    LaunchedEffect(playlist.playlistId) {
        presenter.loadPlaylistSettings(playlist)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // 顶部栏
                PlaylistSettingTopBar(
                    onBackClick = onBackClick
                )

                // 内容区域
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    // 歌单信息头部
                    item {
                        PlaylistSettingHeader(playlist = playlistInfo)
                    }

                    // 分隔符
                    item {
                        Divider(
                            modifier = Modifier.padding(vertical = 8.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                        )
                    }

                    // 复制DeepSeek锐评指令（带限时标签）
                    item {
                        SettingItem(
                            icon = Icons.Default.ContentCopy,
                            title = "复制DeepSeek锐评指令",
                            subtitle = "解析你最爱200首红心歌曲，点评你的听歌品味",
                            showBadge = true,
                            badgeText = "限时",
                            onClick = { presenter.onCopyDeepSeekClick() }
                        )
                    }

                    // WiFi下自动下载歌曲（开关）
                    item {
                        SettingItemWithSwitch(
                            icon = Icons.Default.CloudDownload,
                            title = "WiFi下自动下载歌曲",
                            subtitle = "下载会员歌曲将占用当月付费下载额度",
                            checked = wifiAutoDownload,
                            onCheckedChange = { enabled ->
                                wifiAutoDownload = enabled
                                presenter.onWifiAutoDownloadChanged(enabled)
                            }
                        )
                    }

                    // 歌单壁纸
                    item {
                        SettingItem(
                            icon = Icons.Default.Image,
                            title = "歌单壁纸",
                            onClick = { presenter.onPlaylistWallpaperClick() }
                        )
                    }

                    // 添加歌曲
                    item {
                        SettingItem(
                            icon = Icons.Default.Add,
                            title = "添加歌曲",
                            onClick = { presenter.onAddSongClick() }
                        )
                    }

                    // 歌曲红心记录
                    item {
                        SettingItem(
                            icon = Icons.Default.Favorite,
                            title = "歌曲红心记录",
                            onClick = { presenter.onLikedSongsClick() }
                        )
                    }

                    // 更改歌曲排序
                    item {
                        SettingItem(
                            icon = Icons.Default.Sort,
                            title = "更改歌曲排序",
                            onClick = { presenter.onChangeSortOrderClick() }
                        )
                    }

                    // 清空下载文件
                    item {
                        SettingItem(
                            icon = Icons.Default.Delete,
                            title = "清空下载文件",
                            onClick = { presenter.onClearDownloadsClick() }
                        )
                    }

                    // 添加小组件
                    item {
                        SettingItem(
                            icon = Icons.Default.Widgets,
                            title = "添加小组件",
                            onClick = { presenter.onAddWidgetClick() }
                        )
                    }

                    // 展示专辑封面（开关）
                    item {
                        SettingItemWithSwitch(
                            icon = Icons.Default.Album,
                            title = "展示专辑封面",
                            checked = showAlbumCover,
                            onCheckedChange = { enabled ->
                                showAlbumCover = enabled
                                presenter.onShowAlbumCoverChanged(enabled)
                            }
                        )
                    }
                }
            }
        }
    }
}
