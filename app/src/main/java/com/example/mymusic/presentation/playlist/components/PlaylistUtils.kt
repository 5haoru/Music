package com.example.mymusic.presentation.playlist.components

import com.example.mymusic.data.Song

/**
 * 计算总时长
 */
fun calculateTotalDuration(songs: List<Song>): String {
    val totalSeconds = songs.sumOf { it.duration }
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60

    return when {
        hours > 0 -> "大于${hours}小时"
        minutes > 0 -> "${minutes}分钟"
        else -> "少于1分钟"
    }
}
