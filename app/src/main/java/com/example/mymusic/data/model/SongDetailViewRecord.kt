package com.example.mymusic.data.model

/**
 * 歌曲详情查看记录数据类
 * 用于追踪查看歌曲详细信息情况，检查查看是否成功
 * 对应任务：15-16
 */
data class SongDetailViewRecord(
    val songId: String,            // 歌曲ID
    val viewType: String,          // 查看类型（detail_info/lyrics）
    val viewTime: Long,            // 查看时间（时间戳）
    val isSuccess: Boolean = false    // 是否查看成功
)
