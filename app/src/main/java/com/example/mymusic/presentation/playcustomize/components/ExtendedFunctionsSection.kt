package com.example.mymusic.presentation.playcustomize.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/**
 * 扩展功能区域
 */
@Composable
fun ExtendedFunctionsSection(
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
 * 列表项组件
 */
@Composable
fun ListItem(
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