package com.example.mymusic.presentation.playcustomize.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mymusic.data.Song

/**
 * 详细信息区域
 */
@Composable
fun DetailInfoSection(
    song: Song,
    isFollowed: Boolean,
    onFollowClick: () -> Unit
) {
    Column {
        // 专辑信息
        InfoRow(
            icon = Icons.Default.Album,
            label = "专辑：${song.album} ${song.artist}"
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 歌手信息（带关注按钮）
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "歌手：${song.artist}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // 关注按钮
            OutlinedButton(
                onClick = onFollowClick,
                modifier = Modifier.height(32.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (isFollowed) MaterialTheme.colorScheme.primary else Color.Transparent,
                    contentColor = if (isFollowed) Color.White else MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = if (isFollowed) "已关注" else "+关注",
                    fontSize = 12.sp
                )
            }
        }
    }
}

/**
 * 信息行组件
 */
@Composable
private fun InfoRow(
    icon: ImageVector,
    label: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}