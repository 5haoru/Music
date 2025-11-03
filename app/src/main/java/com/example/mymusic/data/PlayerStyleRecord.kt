package com.example.mymusic.data

/**
 * 播放器样式使用记录数据类
 * 用于记录用户选择的播放器样式
 * 对应 assets/data/playback_style_records.json
 */
data class PlayerStyleRecord(
    val recordId: String,
    val styleId: String,
    val timestamp: Long
)
