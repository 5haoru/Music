package com.example.mymusic.model

/**
 * 歌手关注记录数据类
 * 用于追踪关注/取消关注歌手情况，检查操作是否成功
 * 对应任务：29
 */
data class ArtistFollowRecord(
    val artistId: String,          // 歌手ID
    val artistName: String,        // 歌手名称
    val operationType: String,     // 操作类型（follow/unfollow）
    val operationTime: Long,       // 操作时间（时间戳）
    val isSuccess: Boolean = false    // 是否操作成功
)
