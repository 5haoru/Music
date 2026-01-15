package com.example.mymusic.presentation.playlistsetting

import com.example.mymusic.base.BasePresenter
import com.example.mymusic.base.BaseView
import com.example.mymusic.data.Playlist

/**
 * 歌单设置页面 MVP Contract
 */
interface PlaylistSettingContract {

    /**
     * View 接口
     */
    interface View : BaseView {
        /**
         * 显示歌单信息
         */
        fun showPlaylistInfo(playlist: Playlist)

        /**
         * 导航返回
         */
        fun navigateBack()

        /**
         * 显示提示信息
         */
        fun showToast(message: String)

        /**
         * 导航到歌曲排序页面
         */
        fun navigateToSongSort(playlist: Playlist)

        /**
         * 导航到正在开发中页面
         */
        fun navigateToUnderDevelopment(feature: String)
    }

    /**
     * Presenter 接口
     */
    interface Presenter : BasePresenter {
        /**
         * 加载歌单设置
         */
        fun loadPlaylistSettings(playlist: Playlist)

        /**
         * 点击复制DeepSeek锐评指令
         */
        fun onCopyDeepSeekClick()

        /**
         * WiFi自动下载开关变化
         */
        fun onWifiAutoDownloadChanged(enabled: Boolean)

        /**
         * 点击歌单壁纸
         */
        fun onPlaylistWallpaperClick()

        /**
         * 点击添加歌曲
         */
        fun onAddSongClick()

        /**
         * 点击歌曲红心记录
         */
        fun onLikedSongsClick()

        /**
         * 点击更改歌曲排序
         */
        fun onChangeSortOrderClick()

        /**
         * 点击清空下载文件
         */
        fun onClearDownloadsClick()

        /**
         * 点击添加小组件
         */
        fun onAddWidgetClick()

        /**
         * 展示专辑封面开关变化
         */
        fun onShowAlbumCoverChanged(enabled: Boolean)
    }
}
