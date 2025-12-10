package com.example.mymusic.presentation.rank

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.mymusic.data.repository.RepositoryProvider
import com.example.mymusic.presentation.rank.RankContract
import com.example.mymusic.presentation.rank.RankPresenter
import com.example.mymusic.presentation.rank.components.*

/**
 * 榜单详情页面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RankTab(
    rankId: String,
    onBackClick: () -> Unit = {},
    onNavigateToPlay: (String) -> Unit = {}
) {
    val context = LocalContext.current

    // 状态
    var rankDetail by remember { mutableStateOf<RankContract.RankDetail?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isDescriptionExpanded by remember { mutableStateOf(false) }

    // Presenter
    val presenter = remember {
        RepositoryProvider.initialize(context)
        RankPresenter(
            object : RankContract.View {
                override fun showRankDetail(detail: RankContract.RankDetail) {
                    rankDetail = detail
                }

                override fun updateFollowStatus(isFollowing: Boolean) {
                    rankDetail = rankDetail?.copy(isFollowing = isFollowing)
                }

                override fun updateLikeStatus(isLiked: Boolean, likeCount: Long) {
                    rankDetail = rankDetail?.copy(likeCount = likeCount)
                }

                override fun updateSongFavoriteStatus(songId: String, isFavorite: Boolean) {
                    rankDetail = rankDetail?.let { detail ->
                        val updatedSongs = detail.songs.map { item ->
                            if (item.song.songId == songId) {
                                item.copy(isFavorite = isFavorite)
                            } else {
                                item
                            }
                        }
                        detail.copy(songs = updatedSongs)
                    }
                }

                override fun showSuccess(message: String) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }

                override fun navigateToPlay(songId: String) {
                    onNavigateToPlay(songId)
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
            RepositoryProvider.getSongRepository(),
            RepositoryProvider.getPlaylistRepository()
        )
    }

    // 加载数据
    LaunchedEffect(rankId) {
        presenter.loadRankDetail(rankId)
    }

    Scaffold(
        topBar = {
            RankTopBar(onBackClick = onBackClick)
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
            rankDetail?.let { detail ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    // 榜单头部信息
                    item {
                        RankHeader(
                            detail = detail,
                            onFollowClick = { presenter.onFollowClick() }
                        )
                    }

                    // 描述区域
                    item {
                        RankDescription(
                            description = detail.description,
                            isExpanded = isDescriptionExpanded,
                            onExpandClick = { isDescriptionExpanded = !isDescriptionExpanded }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // 互动按钮行
                    item {
                        InteractionButtonsRow(
                            shareCount = detail.shareCount,
                            commentCount = detail.commentCount,
                            likeCount = detail.likeCount,
                            onShareClick = { presenter.onShareClick() },
                            onCommentClick = { presenter.onCommentClick() },
                            onLikeClick = { presenter.onLikeClick() }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // 播放控制栏
                    item {
                        PlayControlBar(
                            songCount = detail.songs.size,
                            onPlayAllClick = { presenter.onPlayAllClick() }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    // 歌曲列表
                    items(detail.songs) { songItem ->
                        RankSongItem(
                            songItem = songItem,
                            onSongClick = { presenter.onSongClick(songItem.song.songId) },
                            onFavoriteClick = { presenter.onFavoriteClick(songItem.song.songId) },
                            onMoreClick = { presenter.onSongMoreClick(songItem.song.songId) }
                        )
                    }

                    // 底部留白（为底部播放器留空间）
                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }
    }
}
