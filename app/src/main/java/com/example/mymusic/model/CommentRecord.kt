package com.example.mymusic.model

/**
 * 评论记录数据类
 * 用于追踪评论发送情况，检查评论是否发送成功
 */
data class CommentRecord(
    val commentId: String,         // 评论ID
    val songId: String,            // 歌曲ID
    val content: String,           // 评论内容
    val sendTime: Long,            // 发送时间（时间戳）
    val isSuccess: Boolean = false,   // 是否发送成功
    val userId: String             // 用户ID
)
