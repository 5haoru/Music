package com.example.mymusic.presenter

import com.example.mymusic.data.PlayerStyle

/**
 * 播放器样式选择页面的 MVP Contract
 */
interface PlayerContract {

    /**
     * View 接口
     */
    interface View : BaseView {
        /**
         * 显示播放器样式列表
         */
        fun showPlayerStyles(styles: List<PlayerStyle>)

        /**
         * 显示当前选中的样式
         */
        fun showSelectedStyle(styleId: String)

        /**
         * 显示确认对话框
         */
        fun showConfirmDialog(style: PlayerStyle)

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
         * 加载播放器样式列表
         */
        fun loadPlayerStyles()

        /**
         * 用户选择了某个样式
         */
        fun onStyleSelected(style: PlayerStyle)

        /**
         * 确认更改样式
         */
        fun confirmStyleChange(style: PlayerStyle)

        /**
         * 用户切换了标签页
         */
        fun onTabSelected(category: String)

        /**
         * 点击返回按钮
         */
        fun onBackClick()
    }
}
