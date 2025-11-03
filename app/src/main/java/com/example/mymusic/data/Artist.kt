package com.example.mymusic.data

/**
 * 艺术家数据类
 * 对应 assets/data/artists.json
 */
data class Artist(
    val artistId: String,
    val artistName: String,
    val avatarUrl: String,
    val description: String,
    val songCount: Int,
    val albumCount: Int,
    val fans: Int,
    val pinyin: String? = null // 歌手名拼音，用于拼音搜索
)
