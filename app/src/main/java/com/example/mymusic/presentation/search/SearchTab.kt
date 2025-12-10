package com.example.mymusic.presentation.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.mymusic.data.Song
import com.example.mymusic.presentation.search.components.*

/**
 * 搜索页面Tab
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTab(
    onBackClick: () -> Unit,
    onNavigateToSearchResult: (String) -> Unit = {}
) {
    val context = LocalContext.current

    // 状态
    var searchQuery by remember { mutableStateOf("") }
    var searchHistory by remember { mutableStateOf<List<String>>(emptyList()) }
    var recommendedSongs by remember { mutableStateOf<List<Song>>(emptyList()) }
    var hotSearchList by remember { mutableStateOf<List<Song>>(emptyList()) }
    var hotSongList by remember { mutableStateOf<List<Song>>(emptyList()) }
    var searchResults by remember { mutableStateOf<List<Song>?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Presenter
    val presenter = remember {
        SearchPresenter(
            object : SearchContract.View {
                override fun showSearchHistory(history: List<String>) {
                    searchHistory = history
                }

                override fun showRecommendedSongs(songs: List<Song>) {
                    recommendedSongs = songs
                }

                override fun showHotSearchList(songs: List<Song>) {
                    hotSearchList = songs
                }

                override fun showHotSongList(songs: List<Song>) {
                    hotSongList = songs
                }

                override fun showSearchResults(songs: List<Song>) {
                    searchResults = songs
                }

                override fun playSong(song: Song) {
                    // TODO: 实现播放功能
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

    Scaffold(
        topBar = {
            SearchTopBar(
                searchQuery = searchQuery,
                onSearchQueryChange = { query ->
                    searchQuery = query
                    presenter.onSearchTextChanged(query)
                },
                onBackClick = onBackClick
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
                    .padding(paddingValues)
            ) {
                if (searchResults == null) {
                    // 显示默认内容：快捷入口、搜索历史、猜你喜欢、热搜榜和热歌榜

                    // 快捷入口
                    item {
                        QuickEntrySection(
                            onEntryClick = { presenter.onQuickEntryClick(it) }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    // 搜索历史
                    if (searchHistory.isNotEmpty()) {
                        item {
                            SearchHistorySection(
                                history = searchHistory,
                                onHistoryItemClick = { query ->
                                    searchQuery = query
                                    presenter.onSearchHistoryItemClick(query)
                                },
                                onClearHistory = { presenter.onClearSearchHistory() }
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }

                    // 猜你喜欢
                    item {
                        RecommendedSongsSection(
                            songs = recommendedSongs,
                            onSongClick = { presenter.onSongClick(it) }
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }

                    // 双榜单区域
                    item {
                        DoubleRankSection(
                            hotSearchList = hotSearchList,
                            hotSongList = hotSongList,
                            onSongClick = { presenter.onSongClick(it) }
                        )
                        Spacer(modifier = Modifier.height(80.dp)) // 为底部导航栏留空间
                    }
                } else {
                    // 显示搜索结果
                    items(searchResults ?: emptyList()) { song ->
                        SearchResultItem(
                            song = song,
                            onClick = {
                                // 点击搜索结果项，导航到详细搜索结果页面
                                // 传入原始搜索词，以便在结果页显示完整的歌曲/歌曲信息
                                onNavigateToSearchResult(searchQuery)
                            }
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }
    }
}