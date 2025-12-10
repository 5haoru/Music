package com.example.mymusic.data.model

/**
 * 播放器样式记录数据类
 * 用于追踪播放器样式更改情况，检查更改是否成功
 * 对应任务：31
 */
data class PlaybackStyleRecord(
    val styleType: String,         // 样式类型（classic/modern/minimal/custom）
    val changeTime: Long,          // 更改时间（时间戳）
    val isSuccess: Boolean = false    // 是否更改成功
)
