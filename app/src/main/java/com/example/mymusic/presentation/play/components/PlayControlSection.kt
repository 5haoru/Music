package com.example.mymusic.presentation.play.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mymusic.presentation.play.PlayMode

/**
 * 播放控制区域
 */
@Composable
fun PlayControlSection(
    isPlaying: Boolean,
    playMode: PlayMode,
    onPreviousClick: () -> Unit,
    onPlayPauseClick: () -> Unit,
    onNextClick: () -> Unit,
    onPlayModeClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 播放模式图标（根据当前模式动态切换）
        IconButton(onClick = onPlayModeClick) {
            Icon(
                imageVector = when (playMode) {
                    PlayMode.SEQUENCE -> Icons.Default.Repeat
                    PlayMode.SINGLE -> Icons.Default.RepeatOne
                    PlayMode.SHUFFLE -> Icons.Default.Shuffle
                },
                contentDescription = when (playMode) {
                    PlayMode.SEQUENCE -> "顺序播放"
                    PlayMode.SINGLE -> "单曲循环"
                    PlayMode.SHUFFLE -> "随机播放"
                },
                modifier = Modifier.size(28.dp)
            )
        }

        // 上一首
        IconButton(onClick = onPreviousClick) {
            Icon(
                imageVector = Icons.Default.SkipPrevious,
                contentDescription = "上一首",
                modifier = Modifier.size(40.dp)
            )
        }

        // 播放/暂停
        FilledIconButton(
            onClick = onPlayPauseClick,
            modifier = Modifier.size(64.dp),
            shape = CircleShape,
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(
                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = if (isPlaying) "暂停" else "播放",
                modifier = Modifier.size(36.dp),
                tint = Color.White
            )
        }

        // 下一首
        IconButton(onClick = onNextClick) {
            Icon(
                imageVector = Icons.Default.SkipNext,
                contentDescription = "下一首",
                modifier = Modifier.size(40.dp)
            )
        }

        // 播放列表
        IconButton(onClick = { /* TODO: 打开播放列表 */ }) {
            Icon(
                imageVector = Icons.Default.PlaylistPlay,
                contentDescription = "播放列表",
                modifier = Modifier.size(28.dp)
            )
        }
    }
}