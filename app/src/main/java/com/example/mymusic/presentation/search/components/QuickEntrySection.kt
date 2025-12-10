package com.example.mymusic.presentation.search.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun QuickEntrySection(
    onEntryClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            QuickEntryItem("歌手", Icons.Default.Person, "artist", onEntryClick)
            QuickEntryItem("曲风", Icons.Default.MusicNote, "genre", onEntryClick)
            QuickEntryItem("专区", Icons.Default.Album, "zone", onEntryClick)
            QuickEntryItem("识曲", Icons.Default.Mic, "recognize", onEntryClick)
            QuickEntryItem("听书", Icons.Default.Book, "audiobook", onEntryClick)
        }
    }
}

@Composable
private fun QuickEntryItem(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    type: String,
    onEntryClick: (String) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onEntryClick(type) }
            .padding(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            modifier = Modifier.size(32.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = text,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}