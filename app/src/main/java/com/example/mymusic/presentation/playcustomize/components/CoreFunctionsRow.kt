package com.example.mymusic.presentation.playcustomize.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 核心功能按钮
 */
@Composable
fun CoreFunctionsRow(
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