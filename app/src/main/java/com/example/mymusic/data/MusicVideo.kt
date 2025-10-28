package com.example.mymusic.data

/**
 * MV数据类
 * 对应 assets/data/music_videos.json
 */
data class MusicVideo(
    val mvId: String,
    val title: String,
    val artist: String,
    val duration: String,
    val playCount: Long,
    val coverUrl: String,
    val songId: String // 关联的歌曲ID
)
