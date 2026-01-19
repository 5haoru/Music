package com.example.mymusic.presentation.me.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mymusic.data.Playlist

/**
 * 我喜欢的音乐项
 */
@Composable
fun FavoriteMusicItem(
    playlist: Playlist,
    onPlaylistClick: (Playlist) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onPlaylistClick(playlist) }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 封面（特殊样式，蓝色渐变+心形图标）
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(
                    androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(Color(0xFF4A90E2), Color(0xFF357ABD))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // 信息
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "我喜欢的音乐",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${playlist.songCount}首",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // 心动模式按钮
        OutlinedButton(
            onClick = { /* TODO: 心动模式 */ },
            modifier = Modifier.height(32.dp),
            contentPadding = PaddingValues(horizontal = 12.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = Color.Red
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "心动模式", fontSize = 12.sp)
        }
    }
}
