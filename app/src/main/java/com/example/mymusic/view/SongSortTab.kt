package com.example.mymusic.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mymusic.data.Playlist
import com.example.mymusic.data.SortOrderRecord
import com.example.mymusic.presenter.SongSortContract
import com.example.mymusic.presenter.SongSortPresenter

/**
 * 歌曲排序选择页面Tab（三级页面）
 * 从歌单设置页点击"更改歌曲排序"进入
 */
@Composable
fun SongSortTab(
    playlist: Playlist,
    onBackClick: () -> Unit = {}
) {
    val context = LocalContext.current

    // 状态
    var selectedSortType by remember { mutableStateOf(SortOrderRecord.SORT_TIME_DESC) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Presenter
    val presenter = remember {
        SongSortPresenter(
            object : SongSortContract.View {
                override fun showCurrentSortOrder(sortType: String) {
                    selectedSortType = sortType
                }

                override fun navigateBack() {
                    onBackClick()
                }

                override fun showToast(message: String) {
                    android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT).show()
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

    // 加载当前排序设置
    LaunchedEffect(playlist.playlistId) {
        presenter.loadCurrentSortOrder(playlist.playlistId)
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
                SongSortTopBar(
                    onBackClick = { presenter.onBackClick() }
                )

                // 内容区域
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    // 标题
                    item {
                        Text(
                            text = "选择歌曲排序",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                        )
                    }

                    // 排序选项列表
                    val sortOptions = listOf(
                        SortOrderRecord.SORT_MANUAL,
                        SortOrderRecord.SORT_TIME_DESC,
                        SortOrderRecord.SORT_TIME_ASC,
                        SortOrderRecord.SORT_BY_SONG_NAME,
                        SortOrderRecord.SORT_BY_ALBUM_NAME,
                        SortOrderRecord.SORT_BY_ARTIST_NAME,
                        SortOrderRecord.SORT_NO_SOURCE_BOTTOM
                    )

                    items(sortOptions.size) { index ->
                        val sortOption = sortOptions[index]
                        SortOptionItem(
                            sortType = sortOption,
                            isSelected = selectedSortType == sortOption,
                            onClick = {
                                selectedSortType = sortOption
                                presenter.onSortOptionSelected(playlist.playlistId, sortOption)
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
private fun SongSortTopBar(onBackClick: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = "歌曲排序",
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
 * 排序选项列表项
 */
@Composable
private fun SortOptionItem(
    sortType: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // 排序选项文本
        Text(
            text = sortType,
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )

        // 单选按钮
        RadioButton(
            selected = isSelected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = Color(0xFFFF6B6B),
                unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}
