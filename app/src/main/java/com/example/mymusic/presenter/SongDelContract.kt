package com.example.mymusic.presenter

import com.example.mymusic.data.Song

/**
 * 歌曲删除操作页面MVP契约接口
 */
interface SongDelContract {
    interface View : BaseView {
        /**
         * 显示歌曲信息
         */
        fun showSongInfo(song: Song)

        /**
         * 显示删除确认对话框
         */
        fun showDeleteConfirmDialog()

        /**
         * 关闭页面
         */
        fun close()
    }

    interface Presenter : BasePresenter {
        /**
         * 加载歌曲信息
         */
        fun loadSongInfo(songId: String, playlistId: String)

        /**
         * 返回按钮点击
         */
        fun onBackClick()

        /**
         * 评论按钮点击
         */
        fun onCommentClick()

        /**
         * 分享按钮点击
         */
        fun onShareClick()

        /**
         * 单曲购买按钮点击
         */
        fun onPurchaseClick()

        /**
         * 删除按钮点击
         */
        fun onDeleteClick()

        /**
         * 确认删除
         */
        fun confirmDelete()

        /**
         * 设置铃声点击
         */
        fun onRingtoneClick()

        /**
         * 音乐礼品卡点击
         */
        fun onGiftCardClick()
    }
}
