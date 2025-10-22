package com.example.mymusic.presenter

import com.example.mymusic.data.Song

/**
 * 每日推荐页面的 MVP 契约
 */
interface DailyRecommendContract {

    interface View : BaseView {
        /**
         * 显示每日推荐歌曲列表
         */
        fun showDailyRecommendedSongs(songs: List<Song>)

        /**
         * 播放指定歌曲
         */
        fun playSong(song: Song)

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
         */
        fun onSongClick(songId: String)

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
