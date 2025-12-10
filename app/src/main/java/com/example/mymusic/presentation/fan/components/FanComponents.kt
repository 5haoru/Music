package com.example.mymusic.presentation.fan.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mymusic.data.model.FanRecord

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FanTabTopBar(
    onBackClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "粉丝",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "返回"
                )
            }
        },
        actions = {
            // Placeholder to balance the title if needed, or for future search/more actions
            Spacer(modifier = Modifier.width(48.dp))
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}

@Composable
fun FanItemCard(
    fan: FanRecord,
    onFollowClick: (FanRecord) -> Unit
) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* TODO: Navigate to fan profile */ }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar (with VIP indicator)
        Box {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data("file:///android_asset/${fan.avatarUrl}")
                    .crossfade(true)
                    .build(),
                contentDescription = fan.name,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            // VIP indicator
            if (fan.vipType != null) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(
                            when (fan.vipType) {
                                "svip" -> Color(0xFFD4AF37)
                                "vip" -> Color(0xFF1E90FF)
                                else -> Color.Gray
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "V",
                        fontSize = 8.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Information Area
        Column(
            modifier = Modifier.weight(1f)
        ) {
            // Name + VIP badge
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = fan.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                // VIP badge
                if (fan.vipType != null) {
                    Spacer(modifier = Modifier.width(6.dp))
                    VipBadge(vipType = fan.vipType)
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Subtitle
            if (fan.subtitle != null) {
                Text(
                    text = fan.subtitle,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        // Follow button
        Button(
            onClick = { onFollowClick(fan) },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA500)), // Orange color
            shape = RoundedCornerShape(50),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            elevation = ButtonDefaults.buttonElevation(0.dp)
        ) {
            Text(text = "+ 关注", color = Color.White, fontSize = 13.sp)
        }
    }
}

/**
 * Reusing the VipBadge component from SubscribeTab.kt (assuming it's reusable or copied)
 */
@Composable
fun VipBadge(vipType: String) {
    Surface(
        shape = RoundedCornerShape(4.dp),
        color = when (vipType) {
            "svip" -> Color(0xFFD4AF37)
            "black_svip" -> Color.Black
            "vip" -> Color(0xFF1E90FF)
            else -> Color.Gray
        }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                modifier = Modifier.size(10.dp),
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = when (vipType) {
                    "svip" -> "SVIP"
                    "black_svip" -> "黑胶SVIP"
                    "vip" -> "VIP"
                    else -> "VIP"
                },
                fontSize = 9.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
