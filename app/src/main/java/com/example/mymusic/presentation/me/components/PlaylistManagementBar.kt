package com.example.mymusic.presentation.me.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 歌单管理栏（近期/创建/收藏）
 */
@Composable
fun PlaylistManagementBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "近期",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = "创建",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.clickable { /* TODO */ }
            )
            Text(
                text = "收藏",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.clickable { /* TODO */ }
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            IconButton(onClick = { /* TODO: 播放全部 */ }, modifier = Modifier.size(32.dp)) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "播放全部",
                    modifier = Modifier.size(20.dp)
                )
            }
            IconButton(onClick = { /* TODO: 更多 */ }, modifier = Modifier.size(32.dp)) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "更多",
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
