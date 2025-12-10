package com.example.mymusic.presentation.rank.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mymusic.presentation.rank.RankContract

/**
 * 榜单歌曲项
 */
@Composable
fun RankSongItem(
    songItem: RankContract.RankSongItem,
    onSongClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onMoreClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSongClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 排名
        Box(
            modifier = Modifier.width(36.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${songItem.rank}",
                fontSize = 16.sp,
                fontWeight = if (songItem.rank <= 3) FontWeight.Bold else FontWeight.Normal,
                color = if (songItem.rank <= 3) Color(0xFFFF6B6B) else MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(modifier = Modifier.width(4.dp))

        // 排名变化指示器
        Box(
            modifier = Modifier.width(24.dp),
            contentAlignment = Alignment.Center
        ) {
            when (songItem.rankChange) {
                RankContract.RankChange.NEW -> {
                    Text(
                        text = "NEW",
                        fontSize = 9.sp,
                        color = Color.Red,
                        modifier = Modifier
                            .background(
                                color = Color.Red.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(3.dp)
                            )
                            .padding(horizontal = 3.dp, vertical = 1.dp)
                    )
                }
                RankContract.RankChange.UP -> {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "升",
                            fontSize = 10.sp,
                            color = Color.Red
                        )
                        if (songItem.changeValue > 0) {
                            Text(
                                text = "${songItem.changeValue}",
                                fontSize = 9.sp,
                                color = Color.Red
                            )
                        }
                    }
                }
                RankContract.RankChange.DOWN -> {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "降",
                            fontSize = 10.sp,
                            color = Color(0xFF4CAF50)
                        )
                        if (songItem.changeValue > 0) {
                            Text(
                                text = "${songItem.changeValue}",
                                fontSize = 9.sp,
                                color = Color(0xFF4CAF50)
                            )
                        }
                    }
                }
                RankContract.RankChange.STABLE -> {
                    // 不显示任何内容
                }
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // 歌曲信息
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // 收藏图标
                if (songItem.isFavorite) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = Color.Red,
                        modifier = Modifier.size(16.dp)
                    )
                }

                // 音质标签
                if (songItem.hasQualityTag) {
                    Surface(
                        shape = RoundedCornerShape(3.dp),
                        color = Color(0xFFFFE0B2)
                    ) {
                        Text(
                            text = "超清母带",
                            fontSize = 9.sp,
                            color = Color(0xFFD84315),
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                }

                // 歌曲名
                Text(
                    text = songItem.song.songName,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // NEW标签
                if (songItem.isNew) {
                    Text(
                        text = "NEW",
                        fontSize = 9.sp,
                        color = Color.Red,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // 歌手信息
            Text(
                text = songItem.song.artist,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // 播放按钮
        IconButton(onClick = onSongClick) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "播放",
                modifier = Modifier.size(24.dp)
            )
        }

        // 更多按钮
        IconButton(onClick = onMoreClick) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "更多",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
