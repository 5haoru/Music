package com.example.mymusic.presenter

import android.content.Context
import com.example.mymusic.data.Playlist

/**
 * 歌单设置页面的 Presenter
 */
class PlaylistSettingPresenter(
    private val view: PlaylistSettingContract.View,
    private val context: Context
) : PlaylistSettingContract.Presenter {

    private var currentPlaylist: Playlist? = null
    private var wifiAutoDownloadEnabled = false
    private var showAlbumCoverEnabled = true

    override fun loadPlaylistSettings(playlist: Playlist) {
        view.showLoading()
        try {
            currentPlaylist = playlist
            view.showPlaylistInfo(playlist)
            view.hideLoading()
        } catch (e: Exception) {
            view.hideLoading()
            view.showError("加载歌单设置失败: ${e.message}")
        }
    }

    override fun onCopyDeepSeekClick() {
        // 模拟复制DeepSeek锐评指令
        val instruction = "分析我最近100首红心歌曲,点评我的听歌品味"

        // 复制到剪贴板
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
        val clip = android.content.ClipData.newPlainText("DeepSeek指令", instruction)
        clipboard.setPrimaryClip(clip)

        view.showToast("已复制DeepSeek锐评指令到剪贴板")
    }

    override fun onWifiAutoDownloadChanged(enabled: Boolean) {
        wifiAutoDownloadEnabled = enabled
        view.showToast(if (enabled) "已开启WiFi自动下载" else "已关闭WiFi自动下载")
    }

    override fun onPlaylistWallpaperClick() {
        view.showToast("歌单壁纸功能开发中")
    }

    override fun onAddSongClick() {
        view.showToast("添加歌曲功能开发中")
    }

    override fun onLikedSongsClick() {
        view.showToast("歌曲红心记录功能开发中")
    }

    override fun onChangeSortOrderClick() {
        currentPlaylist?.let {
            view.navigateToSongSort(it)
        }
    }

    override fun onClearDownloadsClick() {
        view.showToast("清空下载文件功能开发中")
    }

    override fun onAddWidgetClick() {
        view.showToast("添加小组件功能开发中")
    }

    override fun onShowAlbumCoverChanged(enabled: Boolean) {
        showAlbumCoverEnabled = enabled
        view.showToast(if (enabled) "已开启展示专辑封面" else "已关闭展示专辑封面")
    }

    override fun onDestroy() {
        // 清理资源
    }
}
