package com.example.mymusic.presentation.searchresult.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
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
 * 歌手信息卡片
 */
@Composable
fun ArtistCard(
    artist: Artist,
    isFollowing: Boolean,
    onFollowClick: () -> Unit,
    onArtistClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onArtistClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 歌手头像
        Box(
            modifier = Modifier.size(60.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("file:///android_asset/${artist.avatarUrl}")
                    .crossfade(true)
                    .build(),
                contentDescription = artist.artistName,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            // V标识
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "认证",
                modifier = Modifier
                    .size(18.dp)
                    .align(Alignment.BottomEnd)
                    .background(Color.White, CircleShape),
                tint = Color.Red
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // 歌手信息
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "歌手: ${artist.artistName} (Joker Xue)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${artist.songCount}首 · ${artist.fans / 10000.0}万粉丝",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // 关注按钮
        OutlinedButton(
            onClick = onFollowClick,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.height(36.dp)
        ) {
            Text(
                text = if (isFollowing) "已关注" else "+ 关注",
                fontSize = 14.sp
            )
        }
    }
}