package com.example.mymusic.presentation.me.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mymusic.data.User

/**
 * 用户信息头部
 */
@Composable
fun UserProfileHeader(
    user: User,
    onNavigateToSubscribe: () -> Unit,
    onNavigateToDuration: () -> Unit,
    onNavigateToFans: () -> Unit,
    fanCount: Int
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 大头像
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data("file:///android_asset/avatar/8.png")
                .crossfade(true)
                .build(),
            contentDescription = "用户头像",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 用户名 + 徽章
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = user.username,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(8.dp))
            // VIP徽章
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = Color(0xFFD4A574)
            ) {
                Text(
                    text = "VIP",
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            // 等级徽章
            Surface(
                shape = CircleShape,
                color = Color(0xFFE74C3C)
            ) {
                Text(
                    text = "Lv.${user.level}",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 统计信息（关注/粉丝/等级/时长）
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "6 关注",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.clickable { onNavigateToSubscribe() }
            )
            Text(text = "·", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(
                text = "$fanCount 粉丝",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.clickable { onNavigateToFans() }
            )
            Text(text = "·", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(
                text = "Lv.${user.level}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(text = "·", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(
                text = "${user.listenCount} 小时",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.clickable { onNavigateToDuration() }
            )
        }
    }
}
