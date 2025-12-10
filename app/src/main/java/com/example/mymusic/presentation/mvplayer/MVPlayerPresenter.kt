package com.example.mymusic.presentation.mvplayer

import com.example.mymusic.presentation.mvplayer.MVPlayerContract
import com.example.mymusic.data.MusicVideo
import com.example.mymusic.data.repository.MusicVideoRepository
import com.example.mymusic.data.repository.PlaylistRepository
import com.example.mymusic.utils.AutoTestHelper

/**
 * MV播放页面Presenter
 */
class MVPlayerPresenter(
    private val view: MVPlayerContract.View,
    private val musicVideoRepository: MusicVideoRepository,
    private val playlistRepository: PlaylistRepository
) : MVPlayerContract.Presenter {

    private var currentMV: MusicVideo? = null
    private var isPlaying: Boolean = false
    private var currentProgress: Float = 0f

    override fun loadMV(mvId: String) {
        view.showLoading()
        try {
            currentMV = musicVideoRepository.getMusicVideoById(mvId)

            currentMV?.let { mv ->
                view.showMV(mv)
                AutoTestHelper.updateMVPlayback(
                    mvId = mv.mvId,
                    songId = mv.songId,
                    songName = mv.title,
                    artist = mv.artist,
                    isPlaying = false
                )
            } ?: view.showError("未找到MV")

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
        currentMV?.let { mv ->
            // For simplicity, directly add to "my_favorites" playlist for now
            val success = playlistRepository.addSongToPlaylist("my_favorites", mv.songId)
            if (success) {
                view.showSuccess("已收藏MV")
            } else {
                view.showError("收藏MV失败")
            }
        }
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
