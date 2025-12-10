package com.example.mymusic.data.model

data class Rank(
    val id: String,
    val name: String,
    val coverUrl: String,
    val songs: List<SongBrief>
)

data class SongBrief(
    val id: String,
    val name: String,
    val artist: String
)