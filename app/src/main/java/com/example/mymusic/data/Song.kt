package com.example.mymusic.data

/**
 * 歌曲数据类
 * 对应 assets/data/songs.json
 */
data class Song(
    val songId: String,
    val songName: String,
    val artist: String,
    val album: String,
    val duration: Long,
    val coverUrl: String,
    val lyrics: String,
    val releaseYear: Int
)
