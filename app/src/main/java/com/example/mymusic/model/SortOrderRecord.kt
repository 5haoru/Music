package com.example.mymusic.model

/**
 * 排序调整记录数据类
 * 用于追踪歌单排序更改情况，检查排序是否成功
 * 对应任务：27
 */
data class SortOrderRecord(
    val playlistId: String,        // 歌单ID
    val sortType: String,          // 排序类型（by_time/by_name/by_artist/custom）
    val adjustTime: Long,          // 调整时间（时间戳）
    val isSuccess: Boolean = false    // 是否调整成功
)
