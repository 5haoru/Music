package com.example.mymusic

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.mymusic.data.Playlist
import com.example.mymusic.contract.MainContract
import com.example.mymusic.presenter.MainPresenter
import com.example.mymusic.ui.theme.MyMusicTheme
import com.example.mymusic.utils.AutoTestHelper
import com.example.mymusic.utils.DataLoader

// 导入所有自定义的Tab Composable
import com.example.mymusic.presentation.mvplayer.MVPlayerTab
import com.example.mymusic.presentation.singer.SingerTab
import com.example.mymusic.presentation.listenrecognize.ListenRecognizeTab
import com.example.mymusic.presentation.modeselection.ModeSelectionTab
import com.example.mymusic.presentation.searchresult.SearchResultTab
import com.example.mymusic.presentation.comment.CommentTab
import com.example.mymusic.presentation.lyric.LyricTab
import com.example.mymusic.presentation.subscribe.SubscribeTab
import com.example.mymusic.presentation.duration.DurationTab
import com.example.mymusic.presentation.fan.FanTab
import com.example.mymusic.presentation.songsort.SongSortTab
import com.example.mymusic.presentation.songdel.SongDelTab
import com.example.mymusic.presentation.playlistsetting.PlaylistSettingTab
import com.example.mymusic.presentation.playlist.PlaylistTab
import com.example.mymusic.presentation.stroll.StrollTab
import com.example.mymusic.presentation.play.PlayTab
import com.example.mymusic.presentation.recommend.RecommendTab
import com.example.mymusic.presentation.dailyrecommend.DailyRecommendTab
import com.example.mymusic.presentation.me.MeTab
import com.example.mymusic.presentation.underdevelopment.UnderDevelopmentTab

class MainActivity : ComponentActivity(), MainContract.View {

    private lateinit var presenter: MainPresenter
    private var selectedTab by mutableStateOf(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 初始化自动化测试辅助工具
        AutoTestHelper.init(this)

        // 初始化粉丝数据文件
        DataLoader.initFanItems(this)

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
        var showFanTab by remember { mutableStateOf(false) }
        var showPlaylist by remember { mutableStateOf(false) }
        var showPlaylistSetting by remember { mutableStateOf(false) }
        var showSongSort by remember { mutableStateOf(false) }
        var showSongDel by remember { mutableStateOf(false) }
        var selectedPlaylist by remember { mutableStateOf<Playlist?>(null) }
        var selectedSettingPlaylist by remember { mutableStateOf<Playlist?>(null) }
        var selectedSortPlaylist by remember { mutableStateOf<Playlist?>(null) }
        var selectedDelSongId by remember { mutableStateOf("") }
        var selectedDelPlaylistId by remember { mutableStateOf("") }
        var selectedSongId by remember { mutableStateOf("") }
        var searchResultQuery by remember { mutableStateOf("") }
        var selectedStrollMode by remember { mutableStateOf("伤感") }
        var playTabSongId by remember { mutableStateOf<String?>(null) } // 用于从搜索等页面导航到播放页面时指定歌曲
        var showSinger by remember { mutableStateOf(false) }
        var selectedArtistId by remember { mutableStateOf("") }
        var showMVPlayer by remember { mutableStateOf(false) }
        var selectedMVId by remember { mutableStateOf("") }
        var showUnderDevelopment by remember { mutableStateOf(false) }
        var underDevelopmentFeature by remember { mutableStateOf("") }

        // 正在开发中页面不显示底部导航栏
        if (showUnderDevelopment) {
            UnderDevelopmentTab(
                featureName = underDevelopmentFeature,
                onBackClick = {
                    showUnderDevelopment = false
                    underDevelopmentFeature = ""
                }
            )
            return
        }

        // MV播放页面不显示底部导航栏
        if (showMVPlayer && selectedMVId.isNotEmpty()) {
            // 记录页面导航
            LaunchedEffect(Unit) {
                AutoTestHelper.updateCurrentPage("mv_player")
            }

            MVPlayerTab(
                mvId = selectedMVId,
                onBackClick = {
                    showMVPlayer = false
                    selectedMVId = ""
                }
            )
            return
        }

        // 歌手详情页面不显示底部导航栏
        if (showSinger && selectedArtistId.isNotEmpty()) {
            // 记录页面导航
            LaunchedEffect(Unit) {
                AutoTestHelper.updateCurrentPage("singer")
            }

            SingerTab(
                artistId = selectedArtistId,
                onBackClick = {
                    showSinger = false
                    selectedArtistId = ""
                },
                onNavigateToPlay = { songId ->
                    playTabSongId = songId
                    currentTab = 2
                    showSinger = false
                    showSearchResult = false // 确保不会返回搜索结果页
                },
                onNavigateToMVPlayer = { mvId ->
                    selectedMVId = mvId
                    showMVPlayer = true
                }
            )
            return
        }

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
                onBackClick = { showSearchResult = false },
                onNavigateToPlay = { songId ->
                    playTabSongId = songId
                    currentTab = 2
                    showSearchResult = false
                },
                onNavigateToSinger = { artistId ->
                    selectedArtistId = artistId
                    showSinger = true
                    showSearchResult = false // 清除搜索结果页标志
                }
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

        // 粉丝页面不显示底部导航栏
        if (showFanTab) {
            FanTab(onBackClick = { showFanTab = false })
            return
        }

        // 歌曲排序页面不显示底部导航栏
        if (showSongSort && selectedSortPlaylist != null) {
            SongSortTab(
                playlist = selectedSortPlaylist!!,
                onBackClick = {
                    showSongSort = false
                    selectedSortPlaylist = null
                }
            )
            return
        }

        // 歌曲删除页面不显示底部导航栏
        if (showSongDel && selectedDelSongId.isNotEmpty()) {
            SongDelTab(
                songId = selectedDelSongId,
                playlistId = selectedDelPlaylistId,
                onBackClick = {
                    showSongDel = false
                    selectedDelSongId = ""
                    selectedDelPlaylistId = ""
                }
            )
            return
        }

        // 歌单设置页面不显示底部导航栏
        if (showPlaylistSetting && selectedSettingPlaylist != null) {
            PlaylistSettingTab(
                playlist = selectedSettingPlaylist!!,
                onBackClick = {
                    showPlaylistSetting = false
                    selectedSettingPlaylist = null
                },
                onNavigateToSongSort = { playlist ->
                    selectedSortPlaylist = playlist
                    showSongSort = true
                },
                onNavigateToUnderDevelopment = { feature ->
                    underDevelopmentFeature = feature
                    showUnderDevelopment = true
                }
            )
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
                },
                onNavigateToSetting = { playlist ->
                    selectedSettingPlaylist = playlist
                    showPlaylistSetting = true
                },
                onNavigateToSongDel = { songId, playlistId ->
                    selectedDelSongId = songId
                    selectedDelPlaylistId = playlistId
                    showSongDel = true
                },
                onNavigateToUnderDevelopment = { feature ->
                    underDevelopmentFeature = feature
                    showUnderDevelopment = true
                }
            )
            return
        }

        // 漫游和播放页面不显示底部导航栏
        when (currentTab) {
            1 -> {
                // 记录页面导航
                LaunchedEffect(Unit) {
                    AutoTestHelper.updateCurrentPage("stroll")
                }
                StrollTab(
                    currentMode = selectedStrollMode,
                    onBackToRecommend = { currentTab = 0 },
                    onNavigateToModeSelection = { showModeSelection = true },
                    onNavigateToUnderDevelopment = { feature ->
                        underDevelopmentFeature = feature
                        showUnderDevelopment = true
                    }
                )
            }
            2 -> {
                // 记录页面导航
                LaunchedEffect(Unit) {
                    AutoTestHelper.updateCurrentPage("play")
                }
                PlayTab(
                    initialSongId = playTabSongId,
                    onBackToRecommend = {
                        currentTab = 0
                        playTabSongId = null // 返回时清除指定的歌曲ID
                    },
                    onNavigateToComment = { songId ->
                        selectedSongId = songId
                        showComment = true
                    },
                    onNavigateToLyric = { songId ->
                        selectedSongId = songId
                        showLyric = true
                    },
                    onNavigateToUnderDevelopment = { feature ->
                        underDevelopmentFeature = feature
                        showUnderDevelopment = true
                    }
                )
            }
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
                            0 -> {
                                // 记录页面导航
                                LaunchedEffect(Unit) {
                                    AutoTestHelper.updateCurrentPage("recommend")
                                }
                                RecommendTab(
                                    onNavigateToListenRecognize = { showListenRecognize = true },
                                    onNavigateToSearchResult = { query ->
                                        searchResultQuery = query
                                        showSearchResult = true
                                    },
                                    onNavigateToPlay = { songId ->
                                        playTabSongId = songId
                                        currentTab = 2
                                    },
                                    onNavigateToUnderDevelopment = { feature ->
                                        underDevelopmentFeature = feature
                                        showUnderDevelopment = true
                                    }
                                )
                            }
                            3 -> {
                                // 记录页面导航
                                LaunchedEffect(Unit) {
                                    AutoTestHelper.updateCurrentPage("my")
                                }
                                MeTab(
                                    onNavigateToPlayTab = { currentTab = 2 },
                                    onNavigateToSubscribe = { showSubscribe = true },
                                    onNavigateToDuration = { showDuration = true },
                                    onNavigateToFans = { showFanTab = true },
                                    onNavigateToPlaylist = { playlist ->
                                        selectedPlaylist = playlist
                                        showPlaylist = true
                                    },
                                    onNavigateToUnderDevelopment = { feature ->
                                        underDevelopmentFeature = feature
                                        showUnderDevelopment = true
                                    }
                                )
                            }
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

    override fun showSuccess(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
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
