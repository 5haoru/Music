package com.example.mymusic.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mymusic.data.Playlist
import com.example.mymusic.data.Song
import com.example.mymusic.data.User
import com.example.mymusic.presenter.MeContract
import com.example.mymusic.presenter.MePresenter

/**
 * 我的页面Tab - 参考网易云音乐设计
 */
@Composable
fun MeTab(
    onNavigateToPlayTab: () -> Unit = {},
    onNavigateToSubscribe: () -> Unit = {},
    onNavigateToDuration: () -> Unit = {},
    onNavigateToPlaylist: (Playlist) -> Unit = {}
) {
    val context = LocalContext.current

    // 状态
    var currentUser by remember { mutableStateOf<User?>(null) }
    var playlists by remember { mutableStateOf<List<Playlist>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showCreatePlaylistDialog by remember { mutableStateOf(false) }

    // Presenter
    val presenter = remember {
        MePresenter(
            object : MeContract.View {
                override fun showUser(user: User) {
                    currentUser = user
                }

                override fun showPlaylists(playlistList: List<Playlist>) {
                    playlists = playlistList
                }

                override fun showCurrentSong(song: Song) {
                    // Mini播放器已移除
                }

                override fun navigateToPlayTab() {
                    onNavigateToPlayTab()
                }

                override fun navigateToPlaylist(playlist: Playlist) {
                    onNavigateToPlaylist(playlist)
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
    LaunchedEffect(Unit) {
        presenter.loadData()
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        } else if (errorMessage != null) {
            Text(
                text = errorMessage ?: "",
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.error
            )
        } else {
            // 主要内容区域 - 去掉顶部栏，使用LazyColumn直接填充
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                // 顶部栏
                item {
                    MeTopBar(
                        username = currentUser?.username ?: "",
                        avatarUrl = "avatar/8.png"
                    )
                }

                // 用户信息区域
                item {
                    currentUser?.let { user ->
                        UserProfileHeader(
                            user = user,
                            onNavigateToSubscribe = onNavigateToSubscribe,
                            onNavigateToDuration = onNavigateToDuration
                        )
                    }
                }

                // 功能按钮行
                item {
                    FunctionButtonsRow()
                }

                // 歌单管理区域
                item {
                    PlaylistManagementBar()
                }

                // 歌单列表
                item {
                    PlaylistsSection(
                        playlists = playlists,
                        onPlaylistClick = { presenter.onPlaylistClick(it) }
                    )
                }

                // 新建和导入歌单选项
                item {
                    CreateAndImportSection(
                        onCreatePlaylistClick = {
                            presenter.onCreatePlaylistClick()
                            showCreatePlaylistDialog = true
                        }
                    )
                }
            }
        }

        // 创建歌单对话框
        if (showCreatePlaylistDialog) {
            CreatePlaylistDialog(
                onDismiss = { showCreatePlaylistDialog = false },
                onCreate = { title, isPrivate, isMusic ->
                    presenter.createPlaylist(title, isPrivate, isMusic)
                    showCreatePlaylistDialog = false
                    android.widget.Toast.makeText(context, "创建成功", android.widget.Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}

/**
 * 顶部栏 - 简化版
 */
@Composable
private fun MeTopBar(username: String, avatarUrl: String) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 左侧：汉堡菜单
        IconButton(onClick = { /* TODO: 菜单 */ }) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "菜单"
            )
        }

        // 中间：小头像 + 用户名
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data("file:///android_asset/$avatarUrl")
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = username,
                style = MaterialTheme.typography.titleMedium
            )
        }

        // 右侧：搜索 + 更多按钮
        Row {
            IconButton(onClick = { /* TODO: 搜索 */ }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "搜索"
                )
            }
            IconButton(onClick = { /* TODO: 更多 */ }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "更多"
                )
            }
        }
    }
}

/**
 * 用户信息头部
 */
@Composable
private fun UserProfileHeader(
    user: User,
    onNavigateToSubscribe: () -> Unit,
    onNavigateToDuration: () -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 大头像
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data("file:///android_asset/avatar/8.png")
                .crossfade(true)
                .build(),
            contentDescription = "用户头像",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 用户名 + 徽章
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = user.username,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(8.dp))
            // VIP徽章
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = Color(0xFFD4A574)
            ) {
                Text(
                    text = "VIP",
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            // 等级徽章
            Surface(
                shape = CircleShape,
                color = Color(0xFFE74C3C)
            ) {
                Text(
                    text = "Lv.${user.level}",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 统计信息（关注/粉丝/等级/时长）
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "6 关注",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.clickable { onNavigateToSubscribe() }
            )
            Text(text = "·", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(
                text = "0 粉丝",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.clickable { /* TODO: 打开粉丝列表 */ }
            )
            Text(text = "·", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(
                text = "Lv.${user.level}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(text = "·", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(
                text = "${user.listenCount} 小时",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.clickable { onNavigateToDuration() }
            )
        }
    }
}

/**
 * 功能按钮行
 */
@Composable
private fun FunctionButtonsRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        FunctionButton(icon = Icons.Default.History, label = "最近")
        FunctionButton(icon = Icons.Default.PhoneAndroid, label = "本地")
        FunctionButton(icon = Icons.Default.Cloud, label = "网盘")
        FunctionButton(icon = Icons.Default.Style, label = "装扮")
        FunctionButton(icon = Icons.Default.MoreHoriz, label = "更多")
    }
}

@Composable
private fun FunctionButton(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { /* TODO */ }
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.size(56.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            fontSize = 11.sp
        )
    }
}

/**
 * 歌单管理栏（近期/创建/收藏）
 */
@Composable
private fun PlaylistManagementBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "近期",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = "创建²",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.clickable { /* TODO */ }
            )
            Text(
                text = "收藏³",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.clickable { /* TODO */ }
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            IconButton(onClick = { /* TODO: 播放全部 */ }, modifier = Modifier.size(32.dp)) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "播放全部",
                    modifier = Modifier.size(20.dp)
                )
            }
            IconButton(onClick = { /* TODO: 更多 */ }, modifier = Modifier.size(32.dp)) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "更多",
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

/**
 * 歌单列表区域
 */
@Composable
private fun PlaylistsSection(
    playlists: List<Playlist>,
    onPlaylistClick: (Playlist) -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        // 我喜欢的音乐（特殊样式）
        FavoriteMusicItem()

        Spacer(modifier = Modifier.height(8.dp))

        // 其他歌单
        playlists.forEach { playlist ->
            PlaylistItem(
                playlist = playlist,
                onClick = { onPlaylistClick(playlist) }
            )
        }
    }
}

/**
 * 我喜欢的音乐项
 */
@Composable
private fun FavoriteMusicItem() {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* TODO */ }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 封面（特殊样式，蓝色渐变+心形图标）
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(
                    androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(Color(0xFF4A90E2), Color(0xFF357ABD))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // 信息
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "我喜欢的音乐",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "147首·1477次播放",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // 心动模式按钮
        OutlinedButton(
            onClick = { /* TODO: 心动模式 */ },
            modifier = Modifier.height(32.dp),
            contentPadding = PaddingValues(horizontal = 12.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = Color.Red
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "心动模式", fontSize = 12.sp)
        }
    }
}

/**
 * 歌单列表项
 */
@Composable
private fun PlaylistItem(
    playlist: Playlist,
    onClick: () -> Unit
) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 封面
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data("file:///android_asset/${playlist.coverUrl}")
                .crossfade(true)
                .build(),
            contentDescription = playlist.playlistName,
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        // 信息
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = playlist.playlistName,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "歌单·${playlist.songCount}首·${playlist.playlistId}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        IconButton(onClick = { /* TODO: 更多选项 */ }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "更多",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * 新建和导入歌单区域
 */
@Composable
private fun CreateAndImportSection(onCreatePlaylistClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // 新建歌单
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onCreatePlaylistClick)
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.size(64.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "新建歌单",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        // 导入外部歌单
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { /* TODO: 导入外部歌单 */ }
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.size(64.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Upload,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "导入外部歌单",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "轻松导入其他APP里的歌单",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // 底部提示
        Text(
            text = "展示创建/收藏的30条内容,该列表仅自己可见",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            textAlign = TextAlign.Center
        )
    }
}

