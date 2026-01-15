package com.example.mymusic.presentation.playlistsetting

import android.content.Context
import com.example.mymusic.data.Playlist
import com.example.mymusic.data.model.PlaybackStyleRecord
import com.example.mymusic.data.repository.PlaylistRepository
import com.example.mymusic.utils.AutoTestHelper
import com.example.mymusic.utils.DataLoader

class PlaylistSettingPresenter(
    private val view: PlaylistSettingContract.View,
    private val playlistRepository: PlaylistRepository,
    private val context: Context // Keep context for clipboard service
) : PlaylistSettingContract.Presenter {

    private var currentPlaylist: Playlist? = null
    private var wifiAutoDownloadEnabled = false
    private var showAlbumCoverEnabled = true

    override fun loadPlaylistSettings(playlist: Playlist) {
        view.showLoading()
        try {
            currentPlaylist = playlistRepository.getAllPlaylists().find { it.playlistId == playlist.playlistId }
            currentPlaylist?.let {
                view.showPlaylistInfo(it)
                view.hideLoading()
            } ?: run {
                view.hideLoading()
                view.showError("加载歌单设置失败")
            }
        } catch (e: Exception) {
            view.hideLoading()
            view.showError("加载歌单设置失败: ${e.message}")
        }
    }

    override fun onCopyDeepSeekClick() {
        view.navigateToUnderDevelopment("复制DeepSeek锐评指令")
    }

    override fun onWifiAutoDownloadChanged(enabled: Boolean) {
        wifiAutoDownloadEnabled = enabled
        view.showToast("WiFi自动下载已${if (enabled) "开启" else "关闭"}")
    }

    override fun onPlaylistWallpaperClick() {
        view.navigateToUnderDevelopment("歌单壁纸")
    }

    override fun onAddSongClick() {
        view.navigateToUnderDevelopment("添加歌曲")
    }

    override fun onLikedSongsClick() {
        view.navigateToUnderDevelopment("歌曲红心记录")
    }

    override fun onChangeSortOrderClick() {
        currentPlaylist?.let { playlist ->
            view.navigateToSongSort(playlist)
        } ?: view.showToast("歌单信息未加载")
    }

    override fun onClearDownloadsClick() {
        view.navigateToUnderDevelopment("清空下载文件")
    }

    override fun onAddWidgetClick() {
        view.navigateToUnderDevelopment("添加小组件")
    }

    override fun onShowAlbumCoverChanged(enabled: Boolean) {
        showAlbumCoverEnabled = enabled
        view.showToast("展示专辑封面已${if (enabled) "开启" else "关闭"}")
    }

    override fun onDestroy() {
        // Clean up resources if any
    }
}
