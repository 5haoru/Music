package com.example.mymusic.presentation.singer.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mymusic.data.Artist

/**
 * 歌手信息区域
 */
@Composable
fun SingerInfoSection(
    artist: Artist,
    isFollowing: Boolean,
    onFollowClick: () -> Unit
) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 歌手头像
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data("file:///android_asset/${artist.avatarUrl}")
                .crossfade(true)
                .build(),
            contentDescription = "歌手头像",
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        // 歌手信息
        Column(
            modifier = Modifier.weight(1f)
        ) {
            // 歌手名称
            Text(
                text = "歌手:${artist.artistName} (${artist.description.take(10)}...)",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            // 数据统计
            Text(
                text = "${artist.songCount}首 · ${artist.fans / 10000}万粉丝",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // 乐迷团按钮
        OutlinedButton(
            onClick = onFollowClick,
            modifier = Modifier.height(36.dp),
            shape = RoundedCornerShape(18.dp),
            border = BorderStroke(
                1.dp,
                if (isFollowing) MaterialTheme.colorScheme.primary
                else Color(0xFF4CAF50)
            ),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (isFollowing) Icons.Default.Check else Icons.Default.Favorite,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = if (isFollowing) MaterialTheme.colorScheme.primary
                    else Color(0xFF4CAF50)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (isFollowing) "已加入" else "乐迷团",
                    fontSize = 13.sp,
                    color = if (isFollowing) MaterialTheme.colorScheme.primary
                    else Color(0xFF4CAF50)
                )
            }
        }
    }
}
