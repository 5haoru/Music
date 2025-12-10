package com.example.mymusic.presentation.search.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mymusic.data.Song

@Composable
fun DoubleRankSection(
    hotSearchList: List<Song>,
    hotSongList: List<Song>,
    onSongClick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 热搜榜
        RankListCard(
            title = "热搜榜",
            songs = hotSearchList,
            onSongClick = onSongClick,
            isHotSearch = true,
            modifier = Modifier.weight(1f)
        )

        // 热歌榜
        RankListCard(
            title = "热歌榜",
            songs = hotSongList,
            onSongClick = onSongClick,
            isHotSearch = false,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun RankListCard(
    title: String,
    songs: List<Song>,
    onSongClick: (String) -> Unit,
    isHotSearch: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp)),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "播放",
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            songs.forEachIndexed { index, song ->
                RankSongItem(
                    rank = index + 1,
                    song = song,
                    onSongClick = { onSongClick(song.songId) },
                    showHotBadge = isHotSearch && index == 0,
                    showFireIcon = isHotSearch && index == 1,
                    showTrendIcon = !isHotSearch && index < 3
                )
                if (index < songs.size - 1) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun RankSongItem(
    rank: Int,
    song: Song,
    onSongClick: () -> Unit,
    showHotBadge: Boolean = false,
    showFireIcon: Boolean = false,
    showTrendIcon: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSongClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            // 排名
            Text(
                text = rank.toString(),
                fontSize = 14.sp,
                fontWeight = if (rank <= 3) FontWeight.Bold else FontWeight.Normal,
                color = if (rank <= 3) {
                    when (rank) {
                        1 -> Color(0xFFFFD700) // 金色
                        2 -> Color(0xFFC0C0C0) // 银色
                        3 -> Color(0xFFCD7F32) // 铜色
                        else -> MaterialTheme.colorScheme.onSurface
                    }
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                },
                modifier = Modifier.width(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))

            // 歌名
            Text(
                text = song.songName,
                fontSize = 13.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            // 标签或图�?
            if (showHotBadge) {
                Spacer(modifier = Modifier.width(4.dp))
                Surface(
                    color = Color.Red,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = "热",
                        fontSize = 10.sp,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            } else if (showFireIcon) {
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "🔥",
                    fontSize = 12.sp
                )
            } else if (showTrendIcon) {
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = if (rank % 2 == 0) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = if (rank % 2 == 0) Color.Red else Color.Green
                )
            }
        }

        // 播放按钮
        IconButton(
            onClick = onSongClick,
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "播放",
                modifier = Modifier.size(18.dp),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}