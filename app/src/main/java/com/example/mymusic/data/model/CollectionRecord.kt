package com.example.mymusic.data.model

/**
 * 收藏记录数据类
 * 用于追踪收藏操作情况，检查收藏是否成功
 * 对应任务：8, 20, 28
 */
data class CollectionRecord(
    val collectionId: String,      // 收藏ID
    val contentType: String,       // 收藏内容类型（song/playlist/artist）
    val contentId: String,         // 内容ID
    val contentName: String,       // 内容名称
    val collectionTime: Long,      // 收藏时间（时间戳）
    val isSuccess: Boolean = false    // 是否收藏成功
)
