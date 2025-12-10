package com.example.mymusic.presentation.share

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mymusic.data.Song
import com.example.mymusic.presentation.share.ShareContract
import com.example.mymusic.presentation.share.SharePresenter

/**
 * 分享页面Tab
 * 半透明蒙版背景，覆盖在播放页面�?
 */
@Composable
fun ShareTab(
    song: Song,
    onCloseClick: () -> Unit = {}
) {
    val context = LocalContext.current

    // Presenter
    val presenter = remember {
        SharePresenter(
            object : ShareContract.View {
                override fun showSong(s: Song) {
                    // 不需要重新加载，直接使用传入的song
                }

                override fun showShareSuccess(platform: String) {
                    Toast.makeText(context, "已分享到$platform", Toast.LENGTH_SHORT).show()
                }

                override fun showShareFailure(message: String) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }

                override fun closeShareDialog() {
                    onCloseClick()
                }

                override fun showLoading() {
                    // 不需要loading
                }

                override fun hideLoading() {
                    // 不需要loading
                }

                override fun showError(message: String) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }

                override fun showSuccess(message: String) {
                    android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT).show()
                }
            },
            context
        ).apply {
            // 设置当前歌曲
            setSong(song)
        }
    }

    // 半透明深色蒙版背景
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f))
            .clickable { presenter.onCloseClick() } // 点击背景关闭
    ) {
        // 内容区域（底部）
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .background(MaterialTheme.colorScheme.surface)
                .clickable(enabled = false) {} // 阻止点击穿透到背景
                .padding(24.dp)
        ) {
            // "分享到"标题
            Text(
                text = "分享到",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // 分享平台图标�?
            SharePlatformRow(
                onPlatformClick = { platform ->
                    presenter.onShareToPlatform(platform)
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // "你还可以分享"标题
            Text(
                text = "你还可以分享",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // 扩展功能卡片�?
            ExtendedShareRow(
                song = song,
                onCardClick = { type ->
                    presenter.onShareToPlatform(type)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

/**
 * 分享平台图标�?
 */
@Composable
private fun SharePlatformRow(
    onPlatformClick: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 网易云笔记
        SharePlatformItem(
            icon = Icons.Default.EditNote,
            label = "网易云笔记",
            backgroundColor = Color(0xFFE74C3C),
            onClick = { onPlatformClick("网易云笔记") }
        )

        // 朋友圈
        SharePlatformItem(
            icon = Icons.Default.CameraAlt,
            label = "朋友圈",
            backgroundColor = Color(0xFF2ECC71),
            onClick = { onPlatformClick("朋友圈") }
        )

        // 微信好友
        SharePlatformItem(
            icon = Icons.Default.Chat,
            label = "微信好友",
            backgroundColor = Color(0xFF27AE60),
            onClick = { onPlatformClick("微信好友") }
        )

        // QQ空间
        SharePlatformItem(
            icon = Icons.Default.Star,
            label = "QQ空间",
            backgroundColor = Color(0xFFF39C12),
            onClick = { onPlatformClick("QQ空间") }
        )

        // QQ好友
        SharePlatformItem(
            icon = Icons.Default.Group,
            label = "QQ好友",
            backgroundColor = Color(0xFF3498DB),
            onClick = { onPlatformClick("QQ好友") }
        )
    }
}

/**
 * 单个分享平台图标
 */
@Composable
private fun SharePlatformItem(
    icon: ImageVector,
    label: String,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        // 圆形图标背景
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 平台名称
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}

/**
 * 扩展分享功能卡片�?
 */
@Composable
private fun ExtendedShareRow(
    song: Song,
    onCardClick: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 邀请好友一起听
        ExtendedShareCard(
            modifier = Modifier.weight(1f),
            title = "邀请好友一起听",
            icon = Icons.Default.GroupAdd,
            showBadge = false,
            onClick = { onCardClick("邀请好友一起听") }
        )

        // 送好友会员天数
        ExtendedShareCard(
            modifier = Modifier.weight(1f),
            title = "送好友会员天数",
            icon = Icons.Default.CardGiftcard,
            showBadge = true,
            badgeText = "限时",
            onClick = { onCardClick("送好友会员天数") }
        )

        // 微信状态
        ExtendedShareCard(
            modifier = Modifier.weight(1f),
            title = "微信状态",
            coverUrl = song.coverUrl,
            showBadge = false,
            onClick = { onCardClick("微信状态") }
        )
    }
}

/**
 * 扩展分享功能卡片
 */
@Composable
private fun ExtendedShareCard(
    modifier: Modifier = Modifier,
    title: String,
    icon: ImageVector? = null,
    coverUrl: String? = null,
    showBadge: Boolean = false,
    badgeText: String = "",
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(140.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // 背景内容
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // 图标或封�?
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        modifier = Modifier
                            .size(48.dp)
                            .padding(bottom = 8.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                } else if (coverUrl != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data("file:///android_asset/$coverUrl")
                            .crossfade(true)
                            .build(),
                        contentDescription = title,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                }

                // 标题
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // 限时标签
            if (showBadge) {
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp),
                    shape = RoundedCornerShape(4.dp),
                    color = Color.Red
                ) {
                    Text(
                        text = badgeText,
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}
