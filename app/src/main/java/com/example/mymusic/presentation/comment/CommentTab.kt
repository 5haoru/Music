package com.example.mymusic.presentation.comment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import android.widget.Toast
import com.example.mymusic.data.Comment
import com.example.mymusic.data.Song
import com.example.mymusic.data.repository.CommentRepository
import com.example.mymusic.data.repository.SongRepository
import com.example.mymusic.presentation.comment.components.CommentBottomBar
import com.example.mymusic.presentation.comment.components.CommentItem
import com.example.mymusic.presentation.comment.components.CommentSongInfo
import com.example.mymusic.presentation.comment.components.CommentSortBar
import com.example.mymusic.presentation.comment.components.CommentTopBar

/**
 * 评论页面Tab
 */
@Composable
fun CommentTab(
    songId: String,
    onBackClick: () -> Unit = {}
) {
    val context = LocalContext.current

    // 状态
    var currentSong by remember { mutableStateOf<Song?>(null) }
    var comments by remember { mutableStateOf<List<Comment>>(emptyList()) }
    var commentCount by remember { mutableStateOf(0) }
    var notesCount by remember { mutableStateOf(0) }
    var currentSortMode by remember { mutableStateOf(CommentSortMode.RECOMMENDED) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var selectedTab by remember { mutableStateOf(0) } // 0: 评论, 1: 笔记
    var likedComments by remember { mutableStateOf(setOf<String>()) }
    var commentText by remember { mutableStateOf("") }

    // Presenter
    val presenter = remember {
        val songRepository = SongRepository(context)
        val commentRepository = CommentRepository(context)
        CommentPresenter(
            object : CommentContract.View {
                override fun showSong(song: Song) {
                    currentSong = song
                }

                override fun showComments(commentsList: List<Comment>) {
                    comments = commentsList
                }

                override fun showCommentCount(count: Int) {
                    commentCount = count
                }

                override fun showNotesCount(count: Int) {
                    notesCount = count
                }

                override fun updateSortMode(mode: CommentSortMode) {
                    currentSortMode = mode
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
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }

                override fun updateCommentLikeState(
                    commentId: String,
                    isLiked: Boolean,
                    likeCount: Int
                ) {
                    likedComments = if (isLiked) {
                        likedComments + commentId
                    } else {
                        likedComments - commentId
                    }
                }

                override fun navigateBack() {
                    onBackClick()
                }

                override fun showSuccess(message: String) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }

                override fun clearCommentInput() {
                    commentText = ""
                }
            },
            songRepository,
            commentRepository
        )
    }

    // 加载数据
    LaunchedEffect(songId) {
        presenter.loadData(songId)
    }

    Scaffold(
        topBar = {
            CommentTopBar(
                selectedTab = selectedTab,
                notesCount = notesCount,
                onTabSelected = { selectedTab = it },
                onBackClick = { presenter.onBackClick() }
            )
        },
        bottomBar = {
            CommentBottomBar(
                commentText = commentText,
                onCommentTextChange = { commentText = it },
                onSendComment = { content ->
                    presenter.onSendComment(content)
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
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
            } else if (selectedTab == 1) {
                // 笔记页面（空页面占位）
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "笔记功能暂未实现",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                // 评论页面
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // 歌曲信息
                    item {
                        currentSong?.let { song ->
                            CommentSongInfo(
                                song = song,
                                commentCount = commentCount
                            )
                        }
                    }

                    // 排序筛选栏
                    item {
                        CommentSortBar(
                            currentMode = currentSortMode,
                            onModeChange = { mode ->
                                presenter.onSortModeChange(mode)
                            }
                        )
                    }

                    // 评论列表
                    items(comments) { comment ->
                        CommentItem(
                            comment = comment,
                            isLiked = likedComments.contains(comment.commentId),
                            onLikeClick = { presenter.onCommentLike(comment.commentId) },
                            onReplyClick = { presenter.onReplyClick(comment.commentId) }
                        )
                        Divider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        )
                    }

                    // 底部空白，避免被底部栏遮挡
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}