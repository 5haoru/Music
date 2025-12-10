package com.example.mymusic.presentation.songsort

import com.example.mymusic.base.BasePresenter
import com.example.mymusic.base.BaseView

/**
 * 歌曲排序选择页面�?MVP Contract
 */
interface SongSortContract {

    /**
     * View 接口
     */
    interface View : BaseView {
        /**
         * 显示当前选中的排序方�?
         */
        fun showCurrentSortOrder(sortType: String)

        /**
         * 导航返回
         */
        fun navigateBack()

        /**
         * 显示提示信息
         */
        fun showToast(message: String)
    }

    /**
     * Presenter 接口
     */
    interface Presenter : BasePresenter {
        /**
         * 加载当前歌单的排序方�?
         */
        fun loadCurrentSortOrder(playlistId: String)

        /**
         * 用户选择了某个排序选项
         */
        fun onSortOptionSelected(playlistId: String, sortType: String)

        /**
         * 点击返回按钮
         */
        fun onBackClick()
    }
}
