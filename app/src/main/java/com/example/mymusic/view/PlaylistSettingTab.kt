package com.example.mymusic.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mymusic.data.Playlist
import com.example.mymusic.presenter.PlaylistSettingContract
import com.example.mymusic.presenter.PlaylistSettingPresenter

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
            },
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

                    // 分隔线
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
                            subtitle = "解析你最近100首红心歌曲,点评你的听歌品味",
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

/**
 * 顶部栏
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlaylistSettingTopBar(onBackClick: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = "歌单设置",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "返回",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}

/**
 * 歌单信息头部
 */
@Composable
private fun PlaylistSettingHeader(playlist: Playlist) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 歌单封面
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data("file:///android_asset/${playlist.coverUrl}")
                .crossfade(true)
                .build(),
            contentDescription = "歌单封面",
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        // 歌单信息
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 歌单标题
            Text(
                text = playlist.playlistName,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            // 创建者和播放次数
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 创建者头像（小圆形）
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                )

                Text(
                    text = "箱子里的稻草人",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = "1540次播放",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * 设置项（普通可点击）
 */
@Composable
private fun SettingItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    showBadge: Boolean = false,
    badgeText: String = "",
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 图标
        Icon(
            imageVector = icon,
            contentDescription = title,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.onSurface
        )

        // 文字内容
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // 标题行（可能包含标签）
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                // 限时标签
                if (showBadge) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = Color(0xFFFF6B6B)
                    ) {
                        Text(
                            text = badgeText,
                            fontSize = 11.sp,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            // 副标题
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * 设置项（带开关）
 */
@Composable
private fun SettingItemWithSwitch(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 图标
        Icon(
            imageVector = icon,
            contentDescription = title,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.onSurface
        )

        // 文字内容
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            // 副标题
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // 开关
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}
