package com.example.mymusic.model

/**
 * 音量调节记录数据类
 * 用于追踪音量调节情况，检查音量调节是否成功
 * 对应任务：9
 */
data class VolumeAdjustRecord(
    val previousVolume: Int,       // 调节前音量（0-100）
    val currentVolume: Int,        // 调节后音量（0-100）
    val adjustTime: Long,          // 调节时间（时间戳）
    val isSuccess: Boolean = false    // 是否调节成功
)
