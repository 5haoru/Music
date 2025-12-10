package com.example.mymusic.data.model

/**
 * MV播放记录数据类
 * 用于追踪MV播放情况，检查MV是否成功播放
 * 对应任务：30
 */
data class MVPlayRecord(
    val mvId: String,              // MV ID
    val songId: String,            // 歌曲ID
    val songName: String,          // 歌曲名称
    val playTime: Long,            // 播放时间（时间戳）
    val isSuccess: Boolean = false    // 是否播放成功
)
