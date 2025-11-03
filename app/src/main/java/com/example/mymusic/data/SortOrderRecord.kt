package com.example.mymusic.data

/**
 * 排序记录数据类
 * 用于记录用户对歌单的排序偏好
 * 对应 assets/data/sort_order_records.json
 */
data class SortOrderRecord(
    val recordId: String,
    val playlistId: String,
    val sortType: String,
    val timestamp: Long
) {
    companion object {
        // 排序类型常量
        const val SORT_MANUAL = "手动排序"
        const val SORT_TIME_DESC = "按收藏时间从新到旧排序"
        const val SORT_TIME_ASC = "按收藏时间从旧到新排序"
        const val SORT_BY_SONG_NAME = "按歌曲名排序"
        const val SORT_BY_ALBUM_NAME = "按专辑名排序"
        const val SORT_BY_ARTIST_NAME = "按歌手名排序"
        const val SORT_NO_SOURCE_BOTTOM = "无音源歌曲置底"
    }
}
