package com.example.mymusic.data

/**
 * 歌曲删除记录数据类
 * 用于记录从歌单中删除歌曲的操作
 */
data class SongDeletionRecord(
    val recordId: String,       // 记录ID
    val playlistId: String,     // 歌单ID
    val songId: String,         // 歌曲ID
    val timestamp: Long         // 删除时间戳
)
