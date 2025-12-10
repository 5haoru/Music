package com.example.mymusic.presentation.share

import com.example.mymusic.base.BasePresenter
import com.example.mymusic.base.BaseView
import com.example.mymusic.data.Song

/**
 * 分享页面契约接口
 */
interface ShareContract {
    interface View : BaseView {
        fun showSong(song: Song)
        fun showShareSuccess(platform: String)
        fun showShareFailure(message: String)
        fun closeShareDialog()
    }

    interface Presenter : BasePresenter {
        fun loadData(songId: String)
        fun onShareToPlatform(platform: String)
        fun onCloseClick()
    }
}
