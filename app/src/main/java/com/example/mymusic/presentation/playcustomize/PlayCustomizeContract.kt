package com.example.mymusic.presentation.playcustomize

import com.example.mymusic.base.BasePresenter
import com.example.mymusic.base.BaseView
import com.example.mymusic.data.Song

/**
 * 播放定制页面契约接口
 * 用于播放页面的定制选项浮层（收藏、下载、分享、关注等操作）
 */
interface PlayCustomizeContract {
    interface View : BaseView {
        fun showSong(song: Song)
        fun updateCollectionState(isCollected: Boolean)
        fun updateDownloadState(isDownloaded: Boolean)
        fun updateFollowState(isFollowed: Boolean)
        override fun showSuccess(message: String)
        fun close()
        fun navigateToPlayerStyle()
        fun navigateToSongProfile()
        fun navigateToCollectSong()
        override fun showLoading()
        override fun hideLoading()
        override fun showError(message: String)
    }

    interface Presenter : BasePresenter {
        fun loadSong(songId: String)
        fun onCollectionClick()
        fun onDownloadClick()
        fun onShareClick()
        fun onListenTogetherClick()
        fun onFollowClick()
        fun onSongEncyclopediaClick()
        fun onSimilarStrollClick()
        fun onPurchaseClick()
        fun onQualityClick()
        fun onAudioEffectClick()
        fun onPlayerStyleClick()
        fun onCloseClick()
    }
}
