package com.example.mymusic.data

/**
 * 歌单数据类
 * 对应 assets/data/playlists.json
 */
data class Playlist(
    val playlistId: String,
    val playlistName: String,
    val description: String,
    val coverUrl: String,
    val songIds: List<String>,
    val createTime: Long,
    val songCount: Int
)
