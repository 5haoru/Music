package com.example.mymusic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.mymusic.data.Playlist
import com.example.mymusic.presenter.MainContract
import com.example.mymusic.presenter.MainPresenter
import com.example.mymusic.ui.theme.MyMusicTheme
import com.example.mymusic.view.*

class MainActivity : ComponentActivity(), MainContract.View {

    private lateinit var presenter: MainPresenter
    private var selectedTab by mutableStateOf(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = MainPresenter(this, this)

        enableEdgeToEdge()
        setContent {
            MyMusicTheme {
                MainScreen()
            }
        }
    }

    @Composable
    fun MainScreen() {
        var currentTab by remember { mutableStateOf(0) }
        var showListenRecognize by remember { mutableStateOf(false) }
        var showModeSelection by remember { mutableStateOf(false) }
        var showSearchResult by remember { mutableStateOf(false) }
        var showComment by remember { mutableStateOf(false) }
        var showLyric by remember { mutableStateOf(false) }
        var showSubscribe by remember { mutableStateOf(false) }
        var showDuration by remember { mutableStateOf(false) }
        var showPlaylist by remember { mutableStateOf(false) }
        var selectedPlaylist by remember { mutableStateOf<Playlist?>(null) }
        var selectedSongId by remember { mutableStateOf("") }
        var searchResultQuery by remember { mutableStateOf("") }
        var selectedStrollMode by remember { mutableStateOf("伤感") }

        // 听歌识曲页面不显示底部导航栏
        if (showListenRecognize) {
            ListenRecognizeTab(onBackClick = { showListenRecognize = false })
            return
        }

        // 模式选择页面不显示底部导航栏
        if (showModeSelection) {
            ModeSelectionTab(
                currentMode = selectedStrollMode,
                onBackClick = { showModeSelection = false },
                onModeSelected = { modeName ->
                    selectedStrollMode = modeName
                }
            )
            return
        }

        // 搜索结果页面不显示底部导航栏
        if (showSearchResult) {
            SearchResultTab(
                searchQuery = searchResultQuery,
                onBackClick = { showSearchResult = false }
            )
            return
        }

        // 评论页面不显示底部导航栏
        if (showComment) {
            CommentTab(
                songId = selectedSongId,
                onBackClick = { showComment = false }
            )
            return
        }

        // 歌词页面不显示底部导航栏
        if (showLyric) {
            LyricTab(
                songId = selectedSongId,
                onBackClick = { showLyric = false }
            )
            return
        }

        // 关注页面不显示底部导航栏
        if (showSubscribe) {
            SubscribeTab(onBackClick = { showSubscribe = false })
            return
        }

        // 听歌时长页面不显示底部导航栏
        if (showDuration) {
            DurationTab(onBackClick = { showDuration = false })
            return
        }

        // 歌单详情页面不显示底部导航栏
        if (showPlaylist && selectedPlaylist != null) {
            PlaylistTab(
                playlist = selectedPlaylist!!,
                onBackClick = {
                    showPlaylist = false
                    selectedPlaylist = null
                },
                onNavigateToPlay = { song ->
                    // TODO: 导航到播放页面
                    currentTab = 2
                    showPlaylist = false
                    selectedPlaylist = null
                }
            )
            return
        }

        // 漫游和播放页面不显示底部导航栏
        when (currentTab) {
            1 -> StrollTab(
                currentMode = selectedStrollMode,
                onBackToRecommend = { currentTab = 0 },
                onNavigateToModeSelection = { showModeSelection = true }
            )
            2 -> PlayTab(
                onBackToRecommend = { currentTab = 0 },
                onNavigateToComment = { songId ->
                    selectedSongId = songId
                    showComment = true
                },
                onNavigateToLyric = { songId ->
                    selectedSongId = songId
                    showLyric = true
                }
            )
            else -> {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        NavigationBar {
                            bottomNavItems.forEachIndexed { index, item ->
                                NavigationBarItem(
                                    icon = { Icon(item.icon, contentDescription = item.label) },
                                    label = { Text(item.label) },
                                    selected = currentTab == index,
                                    onClick = {
                                        currentTab = index
                                        presenter.onTabSelected(index)
                                    }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (currentTab) {
                            0 -> RecommendTab(
                                onNavigateToListenRecognize = { showListenRecognize = true },
                                onNavigateToSearchResult = { query ->
                                    searchResultQuery = query
                                    showSearchResult = true
                                }
                            )
                            3 -> MeTab(
                                onNavigateToPlayTab = { currentTab = 2 },
                                onNavigateToSubscribe = { showSubscribe = true },
                                onNavigateToDuration = { showDuration = true },
                                onNavigateToPlaylist = { playlist ->
                                    selectedPlaylist = playlist
                                    showPlaylist = true
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    override fun switchTab(index: Int) {
        selectedTab = index
    }

    override fun showLoading() {
        // 显示加载中
    }

    override fun hideLoading() {
        // 隐藏加载中
    }

    override fun showError(message: String) {
        // 显示错误信息
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    private data class BottomNavItem(
        val label: String,
        val icon: ImageVector
    )

    private val bottomNavItems = listOf(
        BottomNavItem("推荐", Icons.Default.Home),
        BottomNavItem("漫游", Icons.Filled.MusicNote),
        BottomNavItem("播放", Icons.Default.PlayArrow),
        BottomNavItem("我的", Icons.Default.Person)
    )
}
