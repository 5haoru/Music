package com.example.mymusic.presentation.playlist.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mymusic.data.Song

/**
 * 歌曲列表项
 */
@Composable
fun SongListItem(
    song: Song,
    onClick: () -> Unit,
    onMoreClick: () -> Unit = {}
) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 封面缩略图
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data("file:///android_asset/${song.coverUrl}")
                .crossfade(true)
                .build(),
            contentDescription = "歌曲封面",
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(4.dp)),
            contentScale = ContentScale.Crop
        )

        // 歌曲信息
        Column(
            modifier = Modifier.weight(1f)
        ) {
            // 歌名和标识
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = song.songName,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )

                // VIP标识（根据歌曲ID简单判断）
                if (song.songId.hashCode() % 3 == 0) {
                    Surface(
                        shape = RoundedCornerShape(3.dp),
                        border = BorderStroke(
                            1.dp,
                            Color(0xFFFFD700)
                        ),
                        color = Color.Transparent
                    ) {
                        Text(
                            text = "VIP",
                            fontSize = 10.sp,
                            color = Color(0xFFFFD700),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                }

                // 音质标识（部分歌曲显示）
                if (song.songId.hashCode() % 2 == 0) {
                    Surface(
                        shape = RoundedCornerShape(3.dp),
                        border = BorderStroke(
                            1.dp,
                            Color(0xFFFFD700)
                        ),
                        color = Color.Transparent
                    ) {
                        Text(
                            text = "超清母带",
                            fontSize = 10.sp,
                            color = Color(0xFFFFD700),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // 歌手和专辑
            Text(
                text = "${song.artist} - ${song.album}",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // 更多按钮
        IconButton(onClick = onMoreClick) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "更多",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}