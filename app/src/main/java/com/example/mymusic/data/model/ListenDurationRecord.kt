package com.example.mymusic.data.model

/**
 * 听歌时长记录数据类
 * 用于追踪每日/每月听歌时长查看情况，检查查看是否成功
 * 对应任务：22
 */
data class ListenDurationRecord(
    val viewTime: Long,            // 查看时间（时间戳）
    val period: String,            // 统计周期（daily/monthly）
    val duration: Long = 0,        // 听歌时长（秒）
    val isSuccess: Boolean = false    // 是否查看成功
)
