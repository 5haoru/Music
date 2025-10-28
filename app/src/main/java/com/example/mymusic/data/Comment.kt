package com.example.mymusic.data

/**
 * 评论数据类
 * 对应 assets/data/comments.json
 */
data class Comment(
    val commentId: String,
    val songId: String,
    val userId: String,
    val username: String,
    val avatarUrl: String,
    val content: String,
    val timestamp: Long,
    val likeCount: Int,
    val replyCount: Int,
    val isLiked: Boolean = false,
    val userLevel: Int = 1,
    val isLongComment: Boolean = false,
    val isCollapsed: Boolean = false
) {
    /**
     * 格式化点赞数显示
     */
    fun getFormattedLikeCount(): String {
        return when {
            likeCount >= 10000 -> String.format("%.1f万", likeCount / 10000.0)
            likeCount >= 1000 -> String.format("%.1f千", likeCount / 1000.0)
            else -> likeCount.toString()
        }
    }

    /**
     * 格式化回复数显示
     */
    fun getFormattedReplyCount(): String {
        return when {
            replyCount >= 10000 -> String.format("%.1f万", replyCount / 10000.0)
            replyCount >= 1000 -> String.format("%.1f千", replyCount / 1000.0)
            else -> replyCount.toString()
        }
    }

    /**
     * 格式化时间显示
     */
    fun getFormattedTime(): String {
        val now = System.currentTimeMillis()
        val diff = now - timestamp
        val minutes = diff / (1000 * 60)
        val hours = diff / (1000 * 60 * 60)
        val days = diff / (1000 * 60 * 60 * 24)

        return when {
            minutes < 60 -> "${minutes}分钟前"
            hours < 24 -> "${hours}小时前"
            days < 30 -> "${days}天前"
            else -> {
                // 简单返回日期格式
                val date = java.util.Date(timestamp)
                val format = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                format.format(date)
            }
        }
    }
}