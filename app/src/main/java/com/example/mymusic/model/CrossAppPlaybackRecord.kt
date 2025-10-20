package com.example.mymusic.model

/**
 * 跨应用播放记录数据类
 * 用于追踪跨应用播放状态变化，检查BGM是否正确停止
 * 对应任务：26
 */
data class CrossAppPlaybackRecord(
    val currentApp: String,        // 当前应用
    val targetApp: String,         // 目标应用
    val playbackStatus: String,    // 播放状态变化（playing/paused/stopped）
    val timestamp: Long,           // 时间戳
    val isSuccess: Boolean = false    // 是否正确处理（如应停止BGM）
)
