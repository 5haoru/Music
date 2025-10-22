package com.example.mymusic.presenter

import com.example.mymusic.data.Song

/**
 * 排行榜页面的 MVP 契约
 */
interface RankListContract {

    /**
     * 榜单数据类
     */
    data class RankData(
        val rankId: String,
        val rankName: String,
        val updateFrequency: String,  // 更新频率，如"每天更新"
        val coverUrl: String,
        val topSongs: List<TopSongItem>
    )

    /**
     * TOP歌曲项
     */
    data class TopSongItem(
        val rank: Int,               // 排名 1,2,3
        val song: Song,
        val status: SongStatus       // 歌曲状态
    )

    /**
     * 歌曲状态枚举
     */
    enum class SongStatus {
        NEW,        // 新歌
        UP,         // 上升
        DOWN,       // 下降
        STABLE      // 稳定（无标识）
    }

    /**
     * 特色榜单卡片数据
     */
    data class FeaturedRank(
        val id: String,
        val name: String,
        val gradientColors: List<Long>  // 渐变色
    )

    interface View : BaseView {
        /**
         * 显示特色榜单推荐
         */
        fun showFeaturedRanks(ranks: List<FeaturedRank>)

        /**
         * 显示官方榜单列表
         */
        fun showOfficialRanks(ranks: List<RankData>)
    }

    interface Presenter : BasePresenter {
        /**
         * 加载榜单数据
         */
        fun loadRankData()

        /**
         * 点击榜单
         */
        fun onRankClick(rankId: String)

        /**
         * 点击榜单中的歌曲
         */
        fun onSongClick(songId: String)
    }
}
