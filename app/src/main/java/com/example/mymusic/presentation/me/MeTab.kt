package com.example.mymusic.presentation.me

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.mymusic.data.Playlist
import com.example.mymusic.data.Song
import com.example.mymusic.data.User
import com.example.mymusic.presentation.me.contract.MeContract
import com.example.mymusic.presentation.me.MePresenter
import com.example.mymusic.presentation.me.components.*

/**
 * 我的页面Tab - 参考网易云音乐设计
 */
@Composable
fun MeTab(
    onNavigateToPlayTab: () -> Unit = {},
    onNavigateToSubscribe: () -> Unit = {},
    onNavigateToDuration: () -> Unit = {},
    onNavigateToFans: () -> Unit = {},
    onNavigateToPlaylist: (Playlist) -> Unit = {},
    onNavigateToUnderDevelopment: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // 状态
    var currentUser by remember { mutableStateOf<User?>(null) }
    var playlists by remember { mutableStateOf<List<Playlist>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showCreatePlaylistDialog by remember { mutableStateOf(false) }
    var fanCount by remember { mutableIntStateOf(0) }

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

                override fun showSuccess(message: String) {
                    android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT).show()
                }
            },
            context
        )
    }

    // 加载数据
    LaunchedEffect(Unit) {
        presenter.loadData()
    }

    // 监听生命周期，在页面可见时重新加载数据
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                presenter.loadData()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // 加载粉丝数
    LaunchedEffect(Unit) {
        fanCount = 3
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
                            onNavigateToDuration = onNavigateToDuration,
                            onNavigateToFans = onNavigateToFans,
                            fanCount = fanCount
                        )
                    }
                }

                // 功能按钮行
                item {
                    FunctionButtonsRow(
                        onFunctionClick = { feature ->
                            onNavigateToUnderDevelopment(feature)
                        }
                    )
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
                        },
                        onImportPlaylistClick = {
                            onNavigateToUnderDevelopment("导入外部歌单")
                        }
                    )
                }
            }
        }

        // 创建歌单对话框
        if (showCreatePlaylistDialog) {
            CreatePlaylistDialog(
                onDismiss = { showCreatePlaylistDialog = false },
                onCreate = { title: String, isPrivate: Boolean, isMusic: Boolean ->
                    presenter.createPlaylist(title, isPrivate, isMusic)
                    showCreatePlaylistDialog = false
                    android.widget.Toast.makeText(context, "创建成功", android.widget.Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}
