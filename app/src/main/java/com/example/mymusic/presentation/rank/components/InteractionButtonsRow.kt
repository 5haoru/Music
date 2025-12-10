package com.example.mymusic.presentation.rank.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 互动按钮行
 */
@Composable
fun InteractionButtonsRow(
    shareCount: Int,
    commentCount: Int,
    likeCount: Long,
    onShareClick: () -> Unit,
    onCommentClick: () -> Unit,
    onLikeClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 分享按钮
        InteractionButton(
            icon = Icons.Default.Share,
            text = formatCount(shareCount),
            onClick = onShareClick,
            modifier = Modifier.weight(1f),
            backgroundColor = MaterialTheme.colorScheme.surfaceVariant
        )

        // 评论按钮
        InteractionButton(
            icon = Icons.Default.Send,
            text = formatCount(commentCount),
            onClick = onCommentClick,
            modifier = Modifier.weight(1f),
            backgroundColor = MaterialTheme.colorScheme.surfaceVariant
        )

        // 点赞按钮（金色背景）
        InteractionButton(
            icon = Icons.Default.ThumbUp,
            text = formatLikeCount(likeCount),
            onClick = onLikeClick,
            modifier = Modifier.weight(1f),
            backgroundColor = Color(0xFFFFB74D)
        )
    }
}

/**
 * 互动按钮
 */
@Composable
private fun InteractionButton(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = backgroundColor,
        modifier = modifier.height(44.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = text,
                fontSize = 14.sp
            )
        }
    }
}
