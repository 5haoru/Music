package com.example.mymusic.presenter

import android.content.Context
import com.example.mymusic.data.Song
import com.example.mymusic.utils.DataLoader

/**
 * 歌词页面Presenter
 */
class LyricPresenter(
    private val view: LyricContract.View,
    private val context: Context
) : LyricContract.Presenter {

    private var currentSong: Song? = null
    private var isPlaying: Boolean = true // 从播放页面进入，默认正在播放
    private var isFavorite: Boolean = false
    private var currentProgress: Float = 0f
    private var currentLyricIndex: Int = 0
    private var lyricsList: List<String> = emptyList()

    override fun loadData(songId: String) {
        view.showLoading()
        try {
            val song = DataLoader.getSongById(context, songId)
            if (song != null) {
                currentSong = song
                view.showSong(song)

                // 解析歌词（按换行符分割）
                lyricsList = song.lyrics.split("\n").filter { it.isNotBlank() }
                view.showLyrics(lyricsList)

                view.hideLoading()
                view.updatePlayState(isPlaying)
            } else {
                view.hideLoading()
                view.showError("歌曲不存在")
            }
        } catch (e: Exception) {
            view.hideLoading()
            view.showError("加载歌词失败: ${e.message}")
        }
    }

    override fun onBackClick() {
        view.closeLyricPage()
    }

    override fun onRefreshClick() {
        // TODO: 刷新歌词/重新加载
    }

    override fun onFollowClick() {
        // TODO: 关注歌手
    }

    override fun onEncyclopediaClick() {
        // TODO: 打开歌曲百科
    }

    override fun onPlayPauseClick() {
        isPlaying = !isPlaying
        view.updatePlayState(isPlaying)
    }

    override fun onPreviousClick() {
        // TODO: 切换到上一曲（需要与PlayPresenter同步）
    }

    override fun onNextClick() {
        // TODO: 切换到下一曲（需要与PlayPresenter同步）
    }

    override fun onFavoriteClick() {
        isFavorite = !isFavorite
        view.updateFavoriteState(isFavorite)
        // TODO: 保存到collection_records.json
    }

    override fun onMoreClick() {
        // TODO: 显示更多选项
    }

    override fun onCommentClick() {
        // TODO: 打开评论页面
    }

    override fun onProgressChange(progress: Float) {
        currentProgress = progress
        // 根据进度计算当前歌词索引
        val newIndex = (progress * lyricsList.size).toInt().coerceIn(0, lyricsList.size - 1)
        if (newIndex != currentLyricIndex) {
            currentLyricIndex = newIndex
            view.updateCurrentLyricIndex(newIndex)
        }

        // 计算当前时间
        currentSong?.let { song ->
            val currentTimeMs = (progress * song.duration).toLong()
            val minutes = (currentTimeMs / 1000) / 60
            val seconds = (currentTimeMs / 1000) % 60
            val timeString = String.format("%02d:%02d", minutes, seconds)
            view.updateProgress(progress, timeString)
        }
    }

    override fun onDestroy() {
        // Clean up resources
    }
}
