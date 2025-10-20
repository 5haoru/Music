package com.example.mymusic.model

/**
 * 排行榜/推荐选择记录数据类
 * 用于追踪榜单和推荐中的歌曲选择情况，检查选择是否成功
 * 对应任务：19
 */
data class PlaylistSelectionRecord(
    val listType: String,          // 榜单类型（ranking/recommendation/daily_recommend）
    val songId: String,            // 选择的歌曲ID
    val songName: String,          // 歌曲名称
    val selectionTime: Long,       // 选择时间（时间戳）
    val isSuccess: Boolean = false    // 是否选择成功
)
