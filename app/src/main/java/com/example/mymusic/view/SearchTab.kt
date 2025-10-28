package com.example.mymusic.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mymusic.data.Song
import com.example.mymusic.presenter.SearchContract
import com.example.mymusic.presenter.SearchPresenter

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
                                onNavigateToSearchResult(song.songName)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchTopBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    TopAppBar(
        title = {
            TextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = { Text("搜索", fontSize = 16.sp) },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        keyboardController?.hide()
                    }
                ),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "搜索"
                    )
                }
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
        windowInsets = WindowInsets(0.dp)
    )
}

@Composable
private fun QuickEntrySection(
    onEntryClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            QuickEntryItem("歌手", Icons.Default.Person, "artist", onEntryClick)
            QuickEntryItem("曲风", Icons.Default.MusicNote, "genre", onEntryClick)
            QuickEntryItem("专区", Icons.Default.Album, "zone", onEntryClick)
            QuickEntryItem("识曲", Icons.Default.Mic, "recognize", onEntryClick)
            QuickEntryItem("听书", Icons.Default.Book, "audiobook", onEntryClick)
        }
    }
}

@Composable
private fun QuickEntryItem(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    type: String,
    onEntryClick: (String) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onEntryClick(type) }
            .padding(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            modifier = Modifier.size(32.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = text,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun SearchHistorySection(
    history: List<String>,
    onHistoryItemClick: (String) -> Unit,
    onClearHistory: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "搜索历史",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = onClearHistory) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "清空历史",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        history.forEach { query ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onHistoryItemClick(query) }
                    .padding(vertical = 8.dp),
                color = Color.Transparent
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = query,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
private fun RecommendedSongsSection(
    songs: List<Song>,
    onSongClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "猜你喜欢",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.height((songs.size / 2 * 50).dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(songs) { song ->
                Text(
                    text = song.songName,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.clickable { onSongClick(song.songId) }
                )
            }
        }
    }
}

@Composable
private fun DoubleRankSection(
    hotSearchList: List<Song>,
    hotSongList: List<Song>,
    onSongClick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 热搜榜
        RankListCard(
            title = "热搜榜",
            songs = hotSearchList,
            onSongClick = onSongClick,
            isHotSearch = true,
            modifier = Modifier.weight(1f)
        )

        // 热歌榜
        RankListCard(
            title = "热歌榜",
            songs = hotSongList,
            onSongClick = onSongClick,
            isHotSearch = false,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun RankListCard(
    title: String,
    songs: List<Song>,
    onSongClick: (String) -> Unit,
    isHotSearch: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp)),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "播放",
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            songs.forEachIndexed { index, song ->
                RankSongItem(
                    rank = index + 1,
                    song = song,
                    onSongClick = { onSongClick(song.songId) },
                    showHotBadge = isHotSearch && index == 0,
                    showFireIcon = isHotSearch && index == 1,
                    showTrendIcon = !isHotSearch && index < 3
                )
                if (index < songs.size - 1) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun RankSongItem(
    rank: Int,
    song: Song,
    onSongClick: () -> Unit,
    showHotBadge: Boolean = false,
    showFireIcon: Boolean = false,
    showTrendIcon: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSongClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            // 排名
            Text(
                text = rank.toString(),
                fontSize = 14.sp,
                fontWeight = if (rank <= 3) FontWeight.Bold else FontWeight.Normal,
                color = if (rank <= 3) {
                    when (rank) {
                        1 -> Color(0xFFFFD700) // 金色
                        2 -> Color(0xFFC0C0C0) // 银色
                        3 -> Color(0xFFCD7F32) // 铜色
                        else -> MaterialTheme.colorScheme.onSurface
                    }
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                },
                modifier = Modifier.width(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))

            // 歌名
            Text(
                text = song.songName,
                fontSize = 13.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            // 标签或图标
            if (showHotBadge) {
                Spacer(modifier = Modifier.width(4.dp))
                Surface(
                    color = Color.Red,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = "爆",
                        fontSize = 10.sp,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            } else if (showFireIcon) {
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "🔥",
                    fontSize = 12.sp
                )
            } else if (showTrendIcon) {
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = if (rank % 2 == 0) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = if (rank % 2 == 0) Color.Red else Color.Green
                )
            }
        }

        // 播放按钮
        IconButton(
            onClick = onSongClick,
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "播放",
                modifier = Modifier.size(18.dp),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
private fun SearchResultItem(
    song: Song,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        color = Color.Transparent
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = song.songName,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${song.artist} - ${song.album}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            IconButton(onClick = onClick) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "播放"
                )
            }
        }
    }
}
