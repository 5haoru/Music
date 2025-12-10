package com.example.mymusic.data

data class RecognitionRecord(
    val recordId: String,
    val songName: String,
    val artist: String,
    val coverUrl: String,
    val recognitionTime: Long
)
