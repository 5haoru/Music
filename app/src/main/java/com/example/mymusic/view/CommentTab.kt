package com.example.mymusic.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.widget.Toast
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mymusic.data.Comment
import com.example.mymusic.data.Song
import com.example.mymusic.presenter.CommentContract
import com.example.mymusic.presenter.CommentPresenter
import com.example.mymusic.presenter.CommentSortMode

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
            context
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

/**
 * 顶部栏 - 评论/笔记切换
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CommentTopBar(
    selectedTab: Int,
    notesCount: Int,
    onTabSelected: (Int) -> Unit,
    onBackClick: () -> Unit
) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 评论标签
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { onTabSelected(0) }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "评论",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedTab == 0) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (selectedTab == 0) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .width(24.dp)
                                .height(3.dp)
                                .background(
                                    MaterialTheme.colorScheme.primary,
                                    RoundedCornerShape(2.dp)
                                )
                        )
                    }
                }

                Spacer(modifier = Modifier.width(24.dp))

                // 笔记标签
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { onTabSelected(1) }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "笔记",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == 1) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = notesCount.toString(),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 10.sp
                        )
                    }
                    if (selectedTab == 1) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .width(24.dp)
                                .height(3.dp)
                                .background(
                                    MaterialTheme.colorScheme.primary,
                                    RoundedCornerShape(2.dp)
                                )
                        )
                    }
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "返回"
                )
            }
        },
        actions = {
            IconButton(onClick = { /* TODO: 相机功能 */ }) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "拍照"
                )
            }
            IconButton(onClick = { /* TODO: 更多选项 */ }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "更多"
                )
            }
        }
    )
}

/**
 * 歌曲信息显示区域
 */
@Composable
private fun CommentSongInfo(
    song: Song,
    commentCount: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 歌曲封面
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("file:///android_asset/${song.coverUrl}")
                .crossfade(true)
                .build(),
            contentDescription = song.songName,
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        // 歌曲名和艺术家
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "${song.songName} - ${song.artist}",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "评论($commentCount)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * 排序筛选栏
 */
@Composable
private fun CommentSortBar(
    currentMode: CommentSortMode,
    onModeChange: (CommentSortMode) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CommentSortMode.values().forEach { mode ->
            val isSelected = currentMode == mode
            val text = when (mode) {
                CommentSortMode.RECOMMENDED -> "推荐"
                CommentSortMode.HOTTEST -> "最热"
                CommentSortMode.NEWEST -> "最新"
            }

            TextButton(
                onClick = { onModeChange(mode) },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Text(
                    text = text,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

/**
 * 评论项
 */
@Composable
private fun CommentItem(
    comment: Comment,
    isLiked: Boolean,
    onLikeClick: () -> Unit,
    onReplyClick: () -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            // 用户头像
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data("file:///android_asset/${comment.avatarUrl}")
                    .crossfade(true)
                    .build(),
                contentDescription = comment.username,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            // 评论内容区域
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // 用户名
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = comment.username,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    // VIP标识（简化显示）
                    if (comment.userLevel >= 5) {
                        Icon(
                            imageVector = Icons.Default.Diamond,
                            contentDescription = "VIP",
                            modifier = Modifier.size(14.dp),
                            tint = Color(0xFFFFD700)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // 评论内容
                Text(
                    text = comment.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = if (comment.isLongComment && comment.isCollapsed) 3 else Int.MAX_VALUE,
                    overflow = if (comment.isLongComment && comment.isCollapsed) TextOverflow.Ellipsis else TextOverflow.Clip
                )

                // 长评论展开按钮
                if (comment.isLongComment) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (comment.isCollapsed) "——展开" else "——收起",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable { /* TODO: 展开/收起 */ }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 时间和回复数
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = comment.getFormattedTime(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (comment.replyCount > 0) {
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "展开${comment.getFormattedReplyCount()}条回复",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.clickable { onReplyClick() }
                        )
                    }
                }
            }

            // 点赞按钮
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(start = 8.dp)
            ) {
                IconButton(onClick = onLikeClick) {
                    Icon(
                        imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "点赞",
                        modifier = Modifier.size(20.dp),
                        tint = if (isLiked) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (comment.likeCount > 0) {
                    Text(
                        text = comment.getFormattedLikeCount(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}

/**
 * 底部话题与评论输入区域
 */
@Composable
private fun CommentBottomBar(
    commentText: String,
    onCommentTextChange: (String) -> Unit,
    onSendComment: (String) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        tonalElevation = 3.dp,
        shadowElevation = 3.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(8.dp)
        ) {
            // 话题标签
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    IconButton(
                        onClick = { /* TODO: 打开话题列表 */ },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tag,
                            contentDescription = "话题",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                items(listOf("金曲捞", "耳机常驻歌曲", "一听前奏就...")) { topic ->
                    AssistChip(
                        onClick = { /* TODO: 插入话题 */ },
                        label = {
                            Text(
                                text = topic,
                                style = MaterialTheme.typography.bodySmall
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Tag,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 评论输入框
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = commentText,
                    onValueChange = onCommentTextChange,
                    modifier = Modifier.weight(1f),
                    placeholder = {
                        Text(
                            text = "随乐而起,有爱评论",
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    shape = RoundedCornerShape(20.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        if (commentText.isNotBlank()) {
                            onSendComment(commentText)
                        }
                    },
                    enabled = commentText.isNotBlank()
                ) {
                    Text("发送")
                }
            }
        }
    }
}
