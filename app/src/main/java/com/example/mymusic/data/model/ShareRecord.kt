package com.example.mymusic.data.model

/**
 * 分享记录数据类
 * 用于追踪歌曲分享情况，检查歌曲是否分享成功
 */
data class ShareRecord(
    val shareId: String,           // 分享ID
    val songId: String,            // 歌曲ID
    val songName: String,          // 歌曲名称
    val shareTime: Long,           // 分享时间（时间戳）
    val platform: String,          // 分享平台（微信、QQ、微博等）
    val isSuccess: Boolean = false    // 是否分享成功
)
