package com.example.mymusic.presentation.dailyrecommend

import com.example.mymusic.base.BasePresenter
import com.example.mymusic.base.BaseView
import com.example.mymusic.data.Song

/**
 * 每日推荐页面�?MVP 契约
 */
interface DailyRecommendContract {

    interface View : BaseView {
        /**
         * 显示每日推荐歌曲列表
         */
        fun showDailyRecommendedSongs(songs: List<Song>)

        /**
         * 导航到播放页面播放指定歌�?
         */
        fun navigateToPlay(songId: String)

        /**
         * 播放全部歌曲
         */
        fun playAllSongs(songs: List<Song>)
    }

    interface Presenter : BasePresenter {
        /**
         * 加载每日推荐数据
         */
        fun loadDailyRecommendData()

        /**
         * 点击歌曲
         * @param songId 歌曲ID
         * @param songIndex 歌曲索引（从1开始）
         */
        fun onSongClick(songId: String, songIndex: Int)

        /**
         * 点击播放全部
         */
        fun onPlayAllClick()

        /**
         * 点击历史日推
         */
        fun onHistoryClick()

        /**
         * 点击查看今日运势
         */
        fun onTodayFortuneClick()
    }
}
