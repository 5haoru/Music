package com.example.mymusic.data.model

/**
 * 评论复制记录数据类
 * 用于追踪评论复制情况，检查复制是否成功
 * 对应任务：18
 */
data class CommentCopyRecord(
    val commentId: String,         // 评论ID
    val commentContent: String,    // 评论内容
    val copyTime: Long,            // 复制时间（时间戳）
    val isSuccess: Boolean = false    // 是否复制成功
)
