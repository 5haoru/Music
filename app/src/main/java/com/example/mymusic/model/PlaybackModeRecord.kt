package com.example.mymusic.model

/**
 * 播放模式记录数据类
 * 用于追踪播放模式切换情况，检查切换是否成功
 * 对应任务：17
 */
data class PlaybackModeRecord(
    val previousMode: String,      // 切换前模式（loop/shuffle/sequential）
    val currentMode: String,       // 切换后模式（loop/shuffle/sequential）
    val switchTime: Long,          // 切换时间（时间戳）
    val isSuccess: Boolean = false    // 是否切换成功
)
