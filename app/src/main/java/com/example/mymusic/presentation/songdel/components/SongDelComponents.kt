package com.example.mymusic.presentation.songdel.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mymusic.data.Song

// 浅色主题颜色
private val LightBackground = Color(0xFFFFFFFF)
private val LightCardBackground = Color(0xFFF5F5F5)
private val TextPrimary = Color(0xFF000000)
private val TextSecondary = Color(0xFF666666)
private val TextTertiary = Color(0xFF999999)
private val DangerRed = Color(0xFFFF0000)
private val VIPGold = Color(0xFFD4AF37)

/**
 * 顶部栏
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongDelTopBar(onBackClick: () -> Unit) {
    TopAppBar(
        title = { },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "返回",
                    tint = TextPrimary
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = LightBackground.copy(alpha = 0.95f)
        )
    )
}

/**
 * 歌曲信息卡片
 */
@Composable
fun SongInfoCard(song: Song) {
    val context = LocalContext.current

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = LightCardBackground
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // 封面图片
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data("file:///android_asset/${song.coverUrl}")
                    .crossfade(true)
                    .build(),
                contentDescription = song.songName,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            // 歌曲信息
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "歌曲：${song.songName}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    // VIP标识
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = VIPGold
                    ) {
                        Text(
                            text = "VIP",
                            fontSize = 10.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = song.artist,
                    fontSize = 14.sp,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "下载后仅限VIP有效期内在云音乐本地播放",
                    fontSize = 12.sp,
                    color = TextTertiary,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

/**
 * 操作按钮区域
 */
@Composable
fun ActionButtonsSection(
    commentCount: String,
    onCommentClick: () -> Unit,
    onShareClick: () -> Unit,
    onPurchaseClick: () -> Unit
) {
    Column {
        ActionItem(
            icon = Icons.Default.ChatBubbleOutline,
            label = "评论($commentCount)",
            onClick = onCommentClick
        )

        Spacer(modifier = Modifier.height(12.dp))

        ActionItem(
            icon = Icons.Default.Share,
            label = "分享",
            onClick = onShareClick
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onPurchaseClick)
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = TextPrimary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "单曲购买",
                fontSize = 15.sp,
                color = TextPrimary,
                modifier = Modifier.weight(1f)
            )
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = Color(0xFFE0E0E0)
            ) {
                Text(
                    text = "永久拥有该歌曲",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

/**
 * 操作项
 */
@Composable
fun ActionItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = TextPrimary
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            fontSize = 15.sp,
            color = TextPrimary
        )
    }
}

/**
 * 详细信息区域
 */
@Composable
fun DetailInfoSection(song: Song) {
    Column {
        InfoRow(
            icon = Icons.Default.Person,
            label = "歌手：${song.artist}"
        )

        Spacer(modifier = Modifier.height(12.dp))

        InfoRow(
            icon = Icons.Default.Group,
            label = "创作者：${song.artist}/郑伟/张宝宇/赵英俊"
        )

        Spacer(modifier = Modifier.height(12.dp))

        InfoRow(
            icon = Icons.Default.Album,
            label = "专辑：${song.album}"
        )

        Spacer(modifier = Modifier.height(12.dp))

        InfoRow(
            icon = Icons.Default.MusicNote,
            label = "更多乐谱：吉他/钢琴/人声/小提琴/打击乐"
        )
    }
}

/**
 * 信息行
 */
@Composable
fun InfoRow(
    icon: ImageVector,
    label: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = TextSecondary
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            fontSize = 15.sp,
            color = TextPrimary
        )
    }
}

/**
 * 扩展功能区域
 */
@Composable
fun ExtendedFunctionsSection(
    onRingtoneClick: () -> Unit,
    onGiftCardClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Column {
        ActionItem(
            icon = Icons.Default.PhoneAndroid,
            label = "设置铃声或彩铃",
            onClick = onRingtoneClick
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onGiftCardClick)
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.CardGiftcard,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = TextPrimary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "音乐礼品卡",
                fontSize = 15.sp,
                color = TextPrimary,
                modifier = Modifier.weight(1f)
            )
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = Color(0xFFFFE4E1)
            ) {
                Text(
                    text = "送好友",
                    fontSize = 12.sp,
                    color = DangerRed,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 删除按钮（红色高亮）
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onDeleteClick)
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = DangerRed
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "删除",
                fontSize = 15.sp,
                color = DangerRed,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

/**
 * 删除确认对话框
 */
@Composable
fun DeleteConfirmDialog(
    song: Song,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "确定删除该歌曲？",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "${song.songName} - ${song.artist}",
                    fontSize = 15.sp,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // 取消按钮
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = TextSecondary
                        )
                    ) {
                        Text("取消")
                    }

                    // 确定按钮
                    Button(
                        onClick = onConfirm,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DangerRed
                        )
                    ) {
                        Text("确定", color = Color.White)
                    }
                }
            }
        }
    }
}
