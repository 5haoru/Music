package com.example.mymusic.data

/**
 * 专辑数据类
 * 对应 assets/data/albums.json
 */
data class Album(
    val albumId: String,
    val albumName: String,
    val artist: String,
    val artistId: String,
    val coverUrl: String,
    val releaseDate: String, // 格式：YYYY.M.DD（如1999.3.10）
    val description: String,
    val songIds: List<String>,
    val songCount: Int,
    val collectCount: Int,
    val commentCount: Int,
    val shareCount: Int
)
