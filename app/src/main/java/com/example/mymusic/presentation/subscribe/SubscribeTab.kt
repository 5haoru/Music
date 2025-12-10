package com.example.mymusic.presentation.subscribe

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.mymusic.data.FollowItem
import com.example.mymusic.presentation.subscribe.components.*

/**
 * 关注页面Tab（二级页面）
 * 从我的页面的"关注"入口进入
 */
@Composable
fun SubscribeTab(
    onBackClick: () -> Unit = {}
) {
    val context = LocalContext.current

    // State
    var followItems by remember { mutableStateOf<List<FollowItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var selectedTab by remember { mutableStateOf(1) } // 0:推荐, 1:关注, 2:粉丝
    var selectedFilter by remember { mutableStateOf("all") } // "all", "artist", "user"
    var showAggregatedUpdate by remember { mutableStateOf(false) }
    var aggregatedArtistNames by remember { mutableStateOf<List<String>>(emptyList()) }
    var aggregatedCount by remember { mutableStateOf(0) }
    var showUnfollowDialog by remember { mutableStateOf(false) }
    var selectedFollowItem by remember { mutableStateOf<FollowItem?>(null) }

    // Presenter
    val presenter = remember {
        SubscribePresenter(
            object : SubscribeContract.View {
                override fun showFollowItems(items: List<FollowItem>) {
                    followItems = items
                }

                override fun updateFilteredItems(items: List<FollowItem>) {
                    followItems = items
                }

                override fun showAggregatedUpdate(artistNames: List<String>, count: Int) {
                    showAggregatedUpdate = true
                    aggregatedArtistNames = artistNames
                    aggregatedCount = count
                }

                override fun showUnfollowSuccess(name: String) {
                    android.widget.Toast.makeText(context, "已成功取消关注$name", android.widget.Toast.LENGTH_SHORT).show()
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

    // Load data
    LaunchedEffect(Unit) {
        presenter.loadData()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top navigation bar
        SubscribeTopBar(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it },
            onBackClick = onBackClick,
            onSearchClick = { presenter.onSearchClick() }
        )

        // Filter bar
        FilterBar(
            selectedFilter = selectedFilter,
            onFilterSelected = {
                selectedFilter = it
                presenter.filterByType(it)
            },
            onSortClick = { presenter.sortByTime() }
        )

        // Content area
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
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    // Aggregated update banner
                    if (showAggregatedUpdate) {
                        item {
                            AggregatedUpdateBanner(
                                artistNames = aggregatedArtistNames,
                                count = aggregatedCount,
                                onClick = { presenter.onAggregatedUpdateClick() }
                            )
                        }
                    }

                    // Follow list
                    items(followItems) { item ->
                        FollowItemCard(
                            item = item,
                            onClick = { presenter.onFollowItemClick(item) },
                            onMoreClick = {
                                selectedFollowItem = item
                                showUnfollowDialog = true
                                presenter.onFollowItemMoreClick(item)
                            }
                        )
                    }

                    // Bottom recommendation section
                    item {
                        RecommendationSection()
                    }
                }
            }
        }

        // Unfollow dialog
        if (showUnfollowDialog && selectedFollowItem != null) {
            UnfollowDialog(
                item = selectedFollowItem!!,
                onDismiss = {
                    showUnfollowDialog = false
                    selectedFollowItem = null
                },
                onConfirm = {
                    presenter.unfollowItem(selectedFollowItem!!)
                    showUnfollowDialog = false
                    selectedFollowItem = null
                }
            )
        }
    }
}
