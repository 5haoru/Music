package com.example.mymusic.presenter

import android.content.Context
import com.example.mymusic.data.MusicVideo
import com.example.mymusic.utils.AutoTestHelper

/**
 * MV播放页面的 Presenter
 */
class MVPlayerPresenter(
    private val view: MVPlayerContract.View,
    private val context: Context
) : MVPlayerContract.Presenter {

    private var currentMV: MusicVideo? = null
    private var isPlaying: Boolean = false
    private var currentProgress: Float = 0f

    override fun loadMV(mvId: String) {
        view.showLoading()
        try {
            // 创建模拟MV数据
            currentMV = MusicVideo(
                mvId = mvId,
                title = "Demo MV",
                artist = "Demo Artist",
                duration = "03:30",
                playCount = 100000,
                coverUrl = "cover/1.png",
                songId = "song_001"
            )

            currentMV?.let { mv ->
                view.showMV(mv)
                // 记录MV播放状态到AutoTestHelper
                AutoTestHelper.updateMVPlayback(
                    mvId = mv.mvId,
                    songId = mv.songId,
                    songName = mv.title,
                    artist = mv.artist,
                    isPlaying = false
                )
            }

            view.hideLoading()
        } catch (e: Exception) {
            view.showError("加载MV失败: ${e.message}")
        }
    }

    override fun onPlayPauseClick() {
        isPlaying = !isPlaying
        view.updatePlayState(isPlaying)

        // 更新AutoTestHelper中的MV播放状态
        currentMV?.let { mv ->
            AutoTestHelper.updateMVPlayback(
                mvId = mv.mvId,
                songId = mv.songId,
                songName = mv.title,
                artist = mv.artist,
                isPlaying = isPlaying
            )
        }
    }

    override fun onFavoriteClick() {
        view.showSuccess("已收藏")
    }

    override fun onProgressChange(progress: Float) {
        currentProgress = progress
        val totalSeconds = 210 // 3分30秒
        val currentSeconds = (totalSeconds * progress).toInt()
        val minutes = currentSeconds / 60
        val seconds = currentSeconds % 60
        val timeString = String.format("%02d:%02d", minutes, seconds)
        view.updateProgress(progress, timeString)
    }

    override fun updateProgressAutomatically() {
        if (isPlaying && currentProgress < 1f) {
            currentProgress += 0.001f // 每秒增加一点
            if (currentProgress > 1f) {
                currentProgress = 1f
                isPlaying = false
                view.updatePlayState(false)
            }
            val totalSeconds = 210
            val currentSeconds = (totalSeconds * currentProgress).toInt()
            val minutes = currentSeconds / 60
            val seconds = currentSeconds % 60
            val timeString = String.format("%02d:%02d", minutes, seconds)
            view.updateProgress(currentProgress, timeString)
        }
    }

    override fun onDestroy() {
        // 清理资源
    }
}
