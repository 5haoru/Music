package com.example.mymusic.data

/**
 * 播放器样式数据类
 */
data class PlayerStyle(
    val styleId: String,
    val styleName: String,
    val category: String,
    val imageUrl: String,
    val description: String = "",
    val isInUse: Boolean = false
) {
    companion object {
        // 样式类别常量
        const val CATEGORY_CLASSIC = "经典"
        const val CATEGORY_RETRO = "复古"
        const val CATEGORY_CREATIVE = "创意"
        const val CATEGORY_ARTIST = "艺术家"
        const val CATEGORY_COLLAB = "联名"

        /**
         * 获取所有预设的播放器样式
         */
        fun getAllStyles(): List<PlayerStyle> {
            return listOf(
                // 经典类别
                PlayerStyle(
                    styleId = "classic_vinyl",
                    styleName = "经典黑胶",
                    category = CATEGORY_CLASSIC,
                    imageUrl = "player/1.jpg",
                    description = "默认模式，支持横屏"
                ),
                PlayerStyle(
                    styleId = "fullscreen_cover",
                    styleName = "全屏封面",
                    category = CATEGORY_CLASSIC,
                    imageUrl = "player/2.jpg",
                    description = "全屏展示歌曲封面"
                ),
                PlayerStyle(
                    styleId = "album_cover",
                    styleName = "唱片封面",
                    category = CATEGORY_CLASSIC,
                    imageUrl = "player/3.jpg",
                    description = "黑胶封套的艺术"
                ),

                // 复古类别
                PlayerStyle(
                    styleId = "retro_pod",
                    styleName = "复刻·千禧Pod",
                    category = CATEGORY_RETRO,
                    imageUrl = "player/4.jpg",
                    description = "流行于2000年前后"
                ),
                PlayerStyle(
                    styleId = "retro_cd",
                    styleName = "复刻·镭射CD",
                    category = CATEGORY_RETRO,
                    imageUrl = "player/5.jpg",
                    description = "繁盛于80年代 - 00年代",
                    isInUse = true // 默认使用中
                ),
                PlayerStyle(
                    styleId = "retro_light",
                    styleName = "复刻·琉光",
                    category = CATEGORY_RETRO,
                    imageUrl = "player/6.jpg",
                    description = "70年代 - 00年代"
                )
            )
        }

        /**
         * 根据类别筛选样式
         */
        fun getStylesByCategory(category: String): List<PlayerStyle> {
            return getAllStyles().filter { it.category == category }
        }
    }
}
