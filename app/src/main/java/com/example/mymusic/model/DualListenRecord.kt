package com.example.mymusic.model

/**
 * 双人听歌记录数据类
 * 用于追踪邀请好友一起听歌情况，检查邀请是否成功
 * 对应任务：25
 */
data class DualListenRecord(
    val inviterId: String,         // 邀请人ID
    val inviteeId: String,         // 被邀请人ID
    val songId: String,            // 歌曲ID
    val inviteTime: Long,          // 邀请时间（时间戳）
    val isSuccess: Boolean = false    // 是否邀请成功
)
