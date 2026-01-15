package com.example.mymusic.presentation.share

import android.content.Context
import com.example.mymusic.data.Song
import com.example.mymusic.data.model.ShareRecord
import com.example.mymusic.utils.DataLoader

/**
 * 分享页面Presenter
 */
class SharePresenter(
    private val view: ShareContract.View,
    private val context: Context
) : ShareContract.Presenter {

    private var currentSong: Song? = null

    /**
     * 直接设置当前歌曲（用于从外部传入Song对象《
     */
    fun setSong(song: Song) {
        currentSong = song
    }

    override fun loadData(songId: String) {
        view.showLoading()
        try {
            val songs = DataLoader.loadSongs(context)
            currentSong = songs.find { it.songId == songId }

            if (currentSong == null) {
                view.hideLoading()
                view.showError("未找到歌曲信息")
                return
            }

            currentSong?.let { view.showSong(it) }
            view.hideLoading()
        } catch (e: Exception) {
            view.hideLoading()
            view.showError("加载数据失败: ${e.message}")
        }
    }

    override fun onShareToPlatform(platform: String) {
        view.navigateToUnderDevelopment(platform)
    }

    override fun onCloseClick() {
        view.closeShareDialog()
    }

    override fun onDestroy() {
        // Clean up resources
    }
}
