package com.example.mymusic.data.model

/**
 * 粉丝记录数据类
 * 用于展示粉丝列表信息
 */
data class FanRecord(
    val id: String,          // 粉丝ID
    val name: String,        // 粉丝昵称
    val avatarUrl: String,   // 粉丝头像URL
    val subtitle: String?,   // 副文本（例如：个性签名）
    val vipType: String?,    // VIP等级（例如："vip", "svip"）
    val fanTime: Long        // 成为粉丝的时间（时间戳）
)
