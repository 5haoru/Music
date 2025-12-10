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
     * 直接设置当前歌曲（用于从外部传入Song对象�?
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
        currentSong?.let { song ->
            try {
                // 生成分享记录ID
                val shareId = DataLoader.generateShareId(context)

                // 创建分享记录
                val shareRecord = ShareRecord(
                    shareId = shareId,
                    songId = song.songId,
                    songName = song.songName,
                    shareTime = System.currentTimeMillis(),
                    platform = platform,
                    isSuccess = true
                )

                // 保存分享记录
                DataLoader.saveShareRecord(context, shareRecord)

                // 显示成功提示
                view.showShareSuccess(platform)

                // 延迟关闭对话�?
                view.closeShareDialog()
            } catch (e: Exception) {
                view.showShareFailure("分享失败: ${e.message}")
            }
        } ?: run {
            view.showShareFailure("歌曲信息不存在")
        }
    }

    override fun onCloseClick() {
        view.closeShareDialog()
    }

    override fun onDestroy() {
        // Clean up resources
    }
}
