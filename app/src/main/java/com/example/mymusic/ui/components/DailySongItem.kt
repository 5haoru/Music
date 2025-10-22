package com.example.mymusic.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mymusic.data.Song
import kotlin.random.Random

/**
 * 每日推荐页面的歌曲列表项
 * 包含序号、封面、歌曲信息、标签、播放按钮等
 */
@Composable
fun DailySongItem(
    index: Int,
    song: Song,
    onSongClick: () -> Unit,
    onPlayClick: () -> Unit,
    onMoreClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onSongClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 序号
        Text(
            text = "${index + 1}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            modifier = Modifier.width(28.dp)
        )

        // 封面图片
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data("file:///android_asset/${song.coverUrl}")
                .crossfade(true)
                .build(),
            contentDescription = song.songName,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(4.dp))
        )

        Spacer(modifier = Modifier.width(12.dp))

        // 歌曲信息和标签
        Column(
            modifier = Modifier.weight(1f)
        ) {
            // 歌名
            Text(
                text = song.songName,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            // 标签行：包含VIP、音质、热度等标签
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 根据歌曲生成一些模拟标签
                val tags = generateSongTags(song)

                tags.forEach { tag ->
                    SongTag(
                        text = tag.text,
                        textColor = tag.textColor,
                        backgroundColor = tag.backgroundColor
                    )
                }

                // 歌手名称
                Text(
                    text = song.artist,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // 播放按钮
        IconButton(
            onClick = onPlayClick,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "播放",
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }

        // 更多按钮
        IconButton(
            onClick = onMoreClick,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "更多",
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

/**
 * 歌曲标签数据类
 */
data class TagInfo(
    val text: String,
    val textColor: Color,
    val backgroundColor: Color
)

/**
 * 歌曲标签组件
 */
@Composable
private fun SongTag(
    text: String,
    textColor: Color,
    backgroundColor: Color
) {
    Box(
        modifier = Modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(2.dp)
            )
            .padding(horizontal = 4.dp, vertical = 2.dp)
    ) {
        Text(
            text = text,
            fontSize = 10.sp,
            color = textColor,
            fontWeight = FontWeight.Medium
        )
    }
}

/**
 * 根据歌曲生成标签
 * 这里使用简单的规则模拟标签，实际应用中应该从数据源获取
 */
private fun generateSongTags(song: Song): List<TagInfo> {
    val tags = mutableListOf<TagInfo>()

    // 根据歌曲ID模拟不同的标签
    val songIndex = song.songId.substringAfter("_").toIntOrNull() ?: 0

    // VIP标签（部分歌曲）
    if (songIndex % 3 == 0) {
        tags.add(
            TagInfo(
                text = "VIP",
                textColor = Color(0xFFFFB74D),
                backgroundColor = Color(0xFFFFB74D).copy(alpha = 0.2f)
            )
        )
    }

    // 音质标签
    when (songIndex % 4) {
        0 -> tags.add(
            TagInfo(
                text = "超清母带",
                textColor = Color(0xFF64B5F6),
                backgroundColor = Color(0xFF64B5F6).copy(alpha = 0.2f)
            )
        )
        1 -> tags.add(
            TagInfo(
                text = "Hi-Res",
                textColor = Color(0xFF81C784),
                backgroundColor = Color(0xFF81C784).copy(alpha = 0.2f)
            )
        )
    }

    // 热度标签（部分歌曲）
    if (songIndex % 5 == 1) {
        val percentage = Random.nextInt(60, 95)
        tags.add(
            TagInfo(
                text = "超${percentage}%人播放",
                textColor = Color(0xFFEF5350),
                backgroundColor = Color(0xFFEF5350).copy(alpha = 0.15f)
            )
        )
    }

    // 特色标签
    if (songIndex % 5 == 2) {
        tags.add(
            TagInfo(
                text = "小众佳作",
                textColor = Color(0xFF9575CD),
                backgroundColor = Color(0xFF9575CD).copy(alpha = 0.15f)
            )
        )
    }

    if (songIndex % 5 == 3) {
        tags.add(
            TagInfo(
                text = "新歌",
                textColor = Color(0xFFFF7043),
                backgroundColor = Color(0xFFFF7043).copy(alpha = 0.15f)
            )
        )
    }

    return tags
}
