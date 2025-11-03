package com.example.mymusic.view

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mymusic.data.Song
import com.example.mymusic.presenter.PlayCustomizeContract
import com.example.mymusic.presenter.PlayCustomizePresenter

/**
 * 播放定制页面（浮层Overlay）
 * 从播放页面底部"更多"按钮进入
 */
@Composable
fun PlayCustomizeTab(
    song: Song,
    onCloseClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
    onNavigateToPlayerStyle: () -> Unit = {},
    onNavigateToSongProfile: () -> Unit = {},
    onNavigateToCollectSong: () -> Unit = {}
) {
    val context = LocalContext.current

    // 状态
    var currentSong by remember { mutableStateOf<Song?>(null) }
    var collected by remember { mutableStateOf(false) }
    var downloaded by remember { mutableStateOf(false) }
    var followed by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    // Presenter
    val presenter = remember {
        PlayCustomizePresenter(
            object : PlayCustomizeContract.View {
                override fun showSong(song: Song) {
                    currentSong = song
                }

                override fun updateCollectionState(isCollected: Boolean) {
                    collected = isCollected
                }

                override fun updateDownloadState(isDownloaded: Boolean) {
                    downloaded = isDownloaded
                }

                override fun updateFollowState(isFollowed: Boolean) {
                    followed = isFollowed
                }

                override fun showSuccess(message: String) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }

                override fun close() {
                    onCloseClick()
                }

                override fun navigateToPlayerStyle() {
                    onNavigateToPlayerStyle()
                }

                override fun navigateToSongProfile() {
                    onNavigateToSongProfile()
                }

                override fun navigateToCollectSong() {
                    onNavigateToCollectSong()
                }

                override fun showLoading() {
                    isLoading = true
                }

                override fun hideLoading() {
                    isLoading = false
                }

                override fun showError(message: String) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            },
            context
        )
    }

    // 加载数据
    LaunchedEffect(song.songId) {
        presenter.loadSong(song.songId)
    }

    // 背景遮罩 + 内容卡片
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f))
            .clickable { onCloseClick() }, // 点击背景关闭
        contentAlignment = Alignment.BottomCenter
    ) {
        // 内容卡片（点击不关闭）
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp)
                .clickable(enabled = false) {}, // 阻止点击事件传递到背景
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // 顶部歌曲信息区域
                SongInfoSection(song = song)

                Spacer(modifier = Modifier.height(24.dp))

                // 第一层：核心功能按钮
                CoreFunctionsRow(
                    isCollected = collected,
                    isDownloaded = downloaded,
                    onCollectionClick = { presenter.onCollectionClick() },
                    onDownloadClick = { presenter.onDownloadClick() },
                    onShareClick = {
                        onCloseClick()
                        onShareClick()
                    },
                    onListenTogetherClick = { presenter.onListenTogetherClick() }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 第二层：详细信息
                DetailInfoSection(
                    song = song,
                    isFollowed = followed,
                    onFollowClick = { presenter.onFollowClick() }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Divider()

                Spacer(modifier = Modifier.height(16.dp))

                // 第三层：扩展功能
                ExtendedFunctionsSection(
                    onEncyclopediaClick = { presenter.onSongEncyclopediaClick() },
                    onSimilarStrollClick = { presenter.onSimilarStrollClick() },
                    onPurchaseClick = { presenter.onPurchaseClick() }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Divider()

                Spacer(modifier = Modifier.height(16.dp))

                // 第四层：播放设置
                PlaybackSettingsSection(
                    onQualityClick = { presenter.onQualityClick() },
                    onAudioEffectClick = { presenter.onAudioEffectClick() },
                    onPlayerStyleClick = { presenter.onPlayerStyleClick() }
                )

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

/**
 * 顶部歌曲信息区域
 */
@Composable
private fun SongInfoSection(song: Song) {
    val context = LocalContext.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 封面缩略图
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data("file:///android_asset/${song.coverUrl}")
                .crossfade(true)
                .build(),
            contentDescription = song.songName,
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        // 歌曲名和歌手名
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = song.songName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.width(4.dp))
                // VIP标识
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Color(0xFFD4AF37)
                ) {
                    Text(
                        text = "VIP",
                        fontSize = 10.sp,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = song.artist,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * 核心功能按钮行
 */
@Composable
private fun CoreFunctionsRow(
    isCollected: Boolean,
    isDownloaded: Boolean,
    onCollectionClick: () -> Unit,
    onDownloadClick: () -> Unit,
    onShareClick: () -> Unit,
    onListenTogetherClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        FunctionButton(
            icon = if (isCollected) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            label = "收藏",
            onClick = onCollectionClick,
            iconTint = if (isCollected) Color.Red else MaterialTheme.colorScheme.onSurface
        )
        FunctionButton(
            icon = Icons.Default.Download,
            label = "下载",
            onClick = onDownloadClick,
            badge = "VIP"
        )
        FunctionButton(
            icon = Icons.Default.Share,
            label = "分享",
            onClick = onShareClick
        )
        FunctionButton(
            icon = Icons.Default.Group,
            label = "一起听",
            onClick = onListenTogetherClick
        )
    }
}

/**
 * 功能按钮组件
 */
@Composable
private fun FunctionButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    badge: String? = null,
    iconTint: Color = MaterialTheme.colorScheme.onSurface
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(24.dp),
                tint = iconTint
            )
            if (badge != null) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Color(0xFFD4AF37),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                ) {
                    Text(
                        text = badge,
                        fontSize = 8.sp,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 3.dp, vertical = 1.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * 详细信息区域
 */
@Composable
private fun DetailInfoSection(
    song: Song,
    isFollowed: Boolean,
    onFollowClick: () -> Unit
) {
    Column {
        // 专辑信息
        InfoRow(
            icon = Icons.Default.Album,
            label = "专辑：${song.album} ${song.artist}"
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 歌手信息（带关注按钮）
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "歌手：${song.artist}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // 关注按钮
            OutlinedButton(
                onClick = onFollowClick,
                modifier = Modifier.height(32.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (isFollowed) MaterialTheme.colorScheme.primary else Color.Transparent,
                    contentColor = if (isFollowed) Color.White else MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = if (isFollowed) "已关注" else "+关注",
                    fontSize = 12.sp
                )
            }
        }
    }
}

/**
 * 信息行组件
 */
@Composable
private fun InfoRow(
    icon: ImageVector,
    label: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

/**
 * 扩展功能区域
 */
@Composable
private fun ExtendedFunctionsSection(
    onEncyclopediaClick: () -> Unit,
    onSimilarStrollClick: () -> Unit,
    onPurchaseClick: () -> Unit
) {
    Column {
        ListItem(
            icon = Icons.Default.Info,
            label = "查看歌曲百科",
            onClick = onEncyclopediaClick
        )
        ListItem(
            icon = Icons.Default.Explore,
            label = "开始相似歌曲漫游",
            onClick = onSimilarStrollClick
        )
        ListItem(
            icon = Icons.Default.ShoppingCart,
            label = "单曲购买",
            onClick = onPurchaseClick
        )
    }
}

/**
 * 播放设置区域
 */
@Composable
private fun PlaybackSettingsSection(
    onQualityClick: () -> Unit,
    onAudioEffectClick: () -> Unit,
    onPlayerStyleClick: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onQualityClick)
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.HighQuality,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "音质：",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                // 红点图标
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Color.Red)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "极高",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(4.dp))
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Color(0xFFD4AF37)
                ) {
                    Text(
                        text = "VIP",
                        fontSize = 10.sp,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            }
        }

        ListItem(
            icon = Icons.Default.GraphicEq,
            label = "音效",
            onClick = onAudioEffectClick
        )
        ListItem(
            icon = Icons.Default.Palette,
            label = "播放器样式",
            onClick = onPlayerStyleClick
        )
    }
}

/**
 * 列表项组件
 */
@Composable
private fun ListItem(
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
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
