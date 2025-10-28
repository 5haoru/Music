package com.example.mymusic.data

/**
 * 评论记录数据类
 * 用于记录用户发送评论的操作历史
 * 对应 assets/data/comment_records.json
 */
data class CommentRecord(
    val recordId: String,
    val userId: String,
    val username: String,
    val songId: String,
    val songName: String,
    val artist: String,
    val commentContent: String,
    val timestamp: Long,
    val commentId: String
) {
    /**
     * 获取格式化的时间显示
     */
    fun getFormattedTime(): String {
        val now = System.currentTimeMillis()
        val diff = now - timestamp

        return when {
            diff < 60 * 1000 -> "刚刚"
            diff < 60 * 60 * 1000 -> "${diff / (60 * 1000)}分钟前"
            diff < 24 * 60 * 60 * 1000 -> "${diff / (60 * 60 * 1000)}小时前"
            diff < 7 * 24 * 60 * 60 * 1000 -> "${diff / (24 * 60 * 60 * 1000)}天前"
            else -> "一周前"
        }
    }
}