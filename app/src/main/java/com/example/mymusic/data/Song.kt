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
    val releaseYear: Int,
    val pinyin: String? = null, // 歌曲名拼音，用于拼音搜索
    val artistPinyin: String? = null // 艺术家名拼音，用于拼音搜索
)
