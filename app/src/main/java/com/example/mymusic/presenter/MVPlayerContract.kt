package com.example.mymusic.presenter

import com.example.mymusic.data.MusicVideo

/**
 * MV播放页面的 MVP 契约
 */
interface MVPlayerContract {

    interface View : BaseView {
        /**
         * 显示MV信息
         */
        fun showMV(mv: MusicVideo)

        /**
         * 更新播放状态
         */
        fun updatePlayState(isPlaying: Boolean)

        /**
         * 更新进度
         */
        fun updateProgress(progress: Float, currentTime: String)

        /**
         * 显示成功消息
         */
        fun showSuccess(message: String)
    }

    interface Presenter : BasePresenter {
        /**
         * 加载MV数据
         */
        fun loadMV(mvId: String)

        /**
         * 点击播放/暂停按钮
         */
        fun onPlayPauseClick()

        /**
         * 点击收藏按钮
         */
        fun onFavoriteClick()

        /**
         * 进度改变
         */
        fun onProgressChange(progress: Float)

        /**
         * 自动更新进度
         */
        fun updateProgressAutomatically()
    }
}
