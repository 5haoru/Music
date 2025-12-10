package com.example.mymusic.data.model

/**
 * 播放记录数据类
 * 用于追踪歌曲播放情况，检查歌曲是否成功播放
 */
data class PlayRecord(
    val songId: String,           // 歌曲ID
    val songName: String,          // 歌曲名称
    val artist: String,            // 艺术家
    val startTime: Long,           // 播放开始时间（时间戳）
    val duration: Long,            // 播放时长（毫秒）
    val isCompleted: Boolean = false  // 是否播放完成
)
