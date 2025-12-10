package com.example.mymusic.presentation.rank.components

/**
 * 格式化播放次数
 */
fun formatPlayCount(count: Long): String {
    return when {
        count >= 100000000 -> "${count / 100000000}亿"
        count >= 10000 -> "${count / 10000}万"
        else -> "$count"
    }
}

/**
 * 格式化数量
 */
fun formatCount(count: Int): String {
    return if (count >= 10000) {
        "${count / 10000}万"
    } else {
        "$count"
    }
}

/**
 * 格式化点赞数
 */
fun formatLikeCount(count: Long): String {
    return when {
        count >= 100000 -> "${(count / 10000.0).toFloat()}万"
        count >= 10000 -> "${count / 10000}万"
        else -> "$count"
    }
}
