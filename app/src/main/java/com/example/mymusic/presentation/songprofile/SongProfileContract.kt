package com.example.mymusic.presentation.songprofile

import com.example.mymusic.base.BasePresenter
import com.example.mymusic.base.BaseView
import com.example.mymusic.data.SongProfile

/**
 * 歌曲档案页面MVP契约接口
 */
interface SongProfileContract {
    interface View : BaseView {
        /**
         * 显示歌曲档案信息
         */
        fun showSongProfile(profile: SongProfile)

        /**
         * 导航返回上一�?
         */
        fun navigateBack()
    }

    interface Presenter : BasePresenter {
        /**
         * 加载歌曲档案数据
         */
        fun loadSongProfile(songId: String)

        /**
         * 返回按钮点击
         */
        fun onBackClick()

        /**
         * 点击制作信息
         */
        fun onProductionClick()

        /**
         * 点击简�?
         */
        fun onIntroductionClick()

        /**
         * 点击影综
         */
        fun onFilmTvClick()

        /**
         * 点击奖项
         */
        fun onAwardsClick()

        /**
         * 点击乐谱
         */
        fun onScoresClick()
    }
}
