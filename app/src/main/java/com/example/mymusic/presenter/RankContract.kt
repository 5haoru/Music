package com.example.mymusic.presenter

import com.example.mymusic.data.Song

/**
 * 榜单详情页面的Contract接口
 */
interface RankContract {

    /**
     * 榜单详情数据类
     */
    data class RankDetail(
        val rankId: String,
        val rankName: String,              // 如"网易云日语榜"
        val playCount: Long,               // 播放次数
        val officialName: String,          // 官方账号名称，如"网易云音乐"
        val updateDate: String,            // 更新时间，如"10月28日更新"
        val description: String,           // 榜单描述
        val coverUrl: String,              // 封面图片
        val shareCount: Int,               // 分享数
        val commentCount: Int,             // 评论数
        val likeCount: Long,               // 点赞数
        val isFollowing: Boolean,          // 是否已关注官方账号
        val songs: List<RankSongItem>      // 歌曲列表
    )

    /**
     * 榜单歌曲项数据类
     */
    data class RankSongItem(
        val rank: Int,                     // 排名（1-100）
        val song: Song,                    // 歌曲信息
        val isFavorite: Boolean,           // 是否已收藏
        val hasQualityTag: Boolean,        // 是否显示音质标签（超清母带）
        val isNew: Boolean,                // 是否新歌
        val rankChange: RankChange,        // 排名变化类型
        val changeValue: Int               // 变化值（如1表示▲1，2表示▼2）
    )

    /**
     * 排名变化枚举
     */
    enum class RankChange {
        NEW,        // 新上榜
        UP,         // 上升
        DOWN,       // 下降
        STABLE      // 稳定（无变化）
    }

    interface View : BaseView {
        /**
         * 显示榜单详情
         */
        fun showRankDetail(detail: RankDetail)

        /**
         * 更新关注状态
         */
        fun updateFollowStatus(isFollowing: Boolean)

        /**
         * 更新点赞状态
         */
        fun updateLikeStatus(isLiked: Boolean, likeCount: Long)

        /**
         * 更新歌曲收藏状态
         */
        fun updateSongFavoriteStatus(songId: String, isFavorite: Boolean)

        /**
         * 显示成功提示
         */
        fun showSuccess(message: String)

        /**
         * 导航到播放页面
         */
        fun navigateToPlay(songId: String)
    }

    interface Presenter : BasePresenter {
        /**
         * 加载榜单详情
         */
        fun loadRankDetail(rankId: String)

        /**
         * 关注/取消关注官方账号
         */
        fun onFollowClick()

        /**
         * 分享榜单
         */
        fun onShareClick()

        /**
         * 查看评论
         */
        fun onCommentClick()

        /**
         * 点赞/取消点赞
         */
        fun onLikeClick()

        /**
         * 播放全部歌曲
         */
        fun onPlayAllClick()

        /**
         * 点击歌曲
         */
        fun onSongClick(songId: String)

        /**
         * 收藏/取消收藏歌曲
         */
        fun onFavoriteClick(songId: String)

        /**
         * 点击更多（歌曲）
         */
        fun onSongMoreClick(songId: String)
    }
}
