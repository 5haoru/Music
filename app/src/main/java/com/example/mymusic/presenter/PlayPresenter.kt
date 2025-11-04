package com.example.mymusic.presenter

import android.content.Context
import com.example.mymusic.data.Song
import com.example.mymusic.model.CollectionRecord
import com.example.mymusic.utils.AutoTestHelper
import com.example.mymusic.utils.DataLoader
import kotlin.random.Random

/**
 * 播放模式枚举
 */
enum class PlayMode {
    SEQUENTIAL,     // 顺序播放
    SINGLE_LOOP,    // 单曲循环
    RANDOM          // 随机播放
}

/**
 * 播放页面Presenter
 */
class PlayPresenter(
    private val view: PlayContract.View,
    private val context: Context
) : PlayContract.Presenter {

    private var songs: List<Song> = emptyList()
    private var currentSong: Song? = null
    private var currentSongIndex: Int = 0
    private var isPlaying: Boolean = false
    private var isFavorite: Boolean = false
    private var currentProgress: Float = 0f
    private var currentPlayMode: PlayMode = PlayMode.SEQUENTIAL
    private var progressUpdateCallback: (() -> Unit)? = null

    override fun loadData() {
        view.showLoading()
        try {
            songs = DataLoader.loadSongs(context)
            if (songs.isNotEmpty()) {
                // 默认从第一首开始
                currentSongIndex = 0
                currentSong = songs[currentSongIndex]
                currentSong?.let {
                    view.showSong(it)
                    view.hideLoading()
                    // 检查是否已收藏到"我喜欢的音乐"
                    checkFavoriteStatus(it.songId)
                }
                view.updatePlayMode(currentPlayMode)
            } else {
                view.hideLoading()
                view.showError("暂无歌曲")
            }
        } catch (e: Exception) {
            view.hideLoading()
            view.showError("加载数据失败: ${e.message}")
        }
    }

    override fun loadSongById(songId: String) {
        view.showLoading()
        try {
            songs = DataLoader.loadSongs(context)
            val targetSong = songs.find { it.songId == songId }
            if (targetSong != null) {
                currentSong = targetSong
                currentSongIndex = songs.indexOf(targetSong)
                view.showSong(targetSong)
                view.hideLoading()
                // 检查是否已收藏到"我喜欢的音乐"
                checkFavoriteStatus(songId)
                view.updatePlayMode(currentPlayMode)
            } else {
                view.hideLoading()
                view.showError("未找到指定歌曲")
            }
        } catch (e: Exception) {
            view.hideLoading()
            view.showError("加载数据失败: ${e.message}")
        }
    }

    private fun checkFavoriteStatus(songId: String) {
        isFavorite = DataLoader.isSongInPlaylist(context, "my_favorites", songId)
        view.updateFavoriteState(isFavorite)
    }

    override fun onPlayPauseClick() {
        isPlaying = !isPlaying
        view.updatePlayState(isPlaying)

        // 记录播放状态
        currentSong?.let { song ->
            AutoTestHelper.updatePlayback(
                songId = song.songId,
                songName = song.songName,
                artist = song.artist,
                isPlaying = isPlaying
            )
            AutoTestHelper.addPlaybackHistory(song.songId, if (isPlaying) "play" else "pause")
        }

        if (isPlaying) {
            startPlayback()
        } else {
            stopPlayback()
        }
    }

    override fun startPlayback() {
        // 通知View开始播放进度更新
        progressUpdateCallback?.invoke()
    }

    override fun stopPlayback() {
        // 播放状态已更新，View会停止协程
    }

    override fun setProgressUpdateCallback(callback: () -> Unit) {
        progressUpdateCallback = callback
    }

    fun updateProgressAutomatically() {
        if (isPlaying && currentSong != null) {
            currentSong?.let { song ->
                val progressIncrement = 1000f / song.duration // 每秒增加的进度
                currentProgress += progressIncrement

                if (currentProgress >= 1f) {
                    // 播放完成，根据播放模式决定下一步
                    currentProgress = 0f
                    when (currentPlayMode) {
                        PlayMode.SINGLE_LOOP -> {
                            // 单曲循环：重新播放当前歌曲
                            view.updateProgress(0f, "00:00")
                            if (isPlaying) {
                                startPlayback()
                            }
                        }
                        PlayMode.SEQUENTIAL -> {
                            // 顺序播放：播放下一首
                            playNextSongSequential()
                            if (isPlaying) {
                                startPlayback()
                            }
                        }
                        PlayMode.RANDOM -> {
                            // 随机播放：随机选择下一首
                            playNextSongRandom()
                            if (isPlaying) {
                                startPlayback()
                            }
                        }
                    }
                } else {
                    val currentTimeMs = (currentProgress * song.duration).toLong()
                    val minutes = (currentTimeMs / 1000) / 60
                    val seconds = (currentTimeMs / 1000) % 60
                    val timeString = String.format("%02d:%02d", minutes, seconds)
                    view.updateProgress(currentProgress, timeString)
                }
            }
        }
    }

    override fun onPreviousClick() {
        if (songs.isEmpty()) return

        val wasPlaying = isPlaying
        when (currentPlayMode) {
            PlayMode.SINGLE_LOOP -> {
                // 单曲循环：重置进度到开头
                currentProgress = 0f
                view.updateProgress(0f, "00:00")
            }
            PlayMode.SEQUENTIAL -> {
                // 顺序播放：播放上一首
                playPreviousSongSequential()
            }
            PlayMode.RANDOM -> {
                // 随机播放：随机选择一首
                playNextSongRandom()
            }
        }

        if (wasPlaying) {
            startPlayback()
        }
    }

    override fun onNextClick() {
        if (songs.isEmpty()) return

        val wasPlaying = isPlaying
        when (currentPlayMode) {
            PlayMode.SINGLE_LOOP -> {
                // 单曲循环：重置进度到开头
                currentProgress = 0f
                view.updateProgress(0f, "00:00")
            }
            PlayMode.SEQUENTIAL -> {
                // 顺序播放：播放下一首
                playNextSongSequential()
            }
            PlayMode.RANDOM -> {
                // 随机播放：随机选择一首
                playNextSongRandom()
            }
        }

        if (wasPlaying) {
            startPlayback()
        }
    }

    override fun onPlayModeClick() {
        // 循环切换播放模式：顺序 -> 单曲循环 -> 随机 -> 顺序
        currentPlayMode = when (currentPlayMode) {
            PlayMode.SEQUENTIAL -> PlayMode.SINGLE_LOOP
            PlayMode.SINGLE_LOOP -> PlayMode.RANDOM
            PlayMode.RANDOM -> PlayMode.SEQUENTIAL
        }
        view.updatePlayMode(currentPlayMode)

        // 记录播放模式
        val mode = when (currentPlayMode) {
            PlayMode.SEQUENTIAL -> "sequential"
            PlayMode.SINGLE_LOOP -> "single_loop"
            PlayMode.RANDOM -> "shuffle"
        }
        AutoTestHelper.updatePlayback(playbackMode = mode)
    }

    private fun playNextSongSequential() {
        currentSongIndex = (currentSongIndex + 1) % songs.size
        currentSong = songs[currentSongIndex]
        currentSong?.let {
            view.showSong(it)
            currentProgress = 0f
            view.updateProgress(0f, "00:00")
        }
    }

    private fun playPreviousSongSequential() {
        currentSongIndex = if (currentSongIndex - 1 < 0) songs.size - 1 else currentSongIndex - 1
        currentSong = songs[currentSongIndex]
        currentSong?.let {
            view.showSong(it)
            currentProgress = 0f
            view.updateProgress(0f, "00:00")
        }
    }

    private fun playNextSongRandom() {
        // 随机选择，避免重复选择当前歌曲（如果歌曲数>1）
        if (songs.size > 1) {
            var newIndex: Int
            do {
                newIndex = Random.nextInt(songs.size)
            } while (newIndex == currentSongIndex)
            currentSongIndex = newIndex
        }
        currentSong = songs[currentSongIndex]
        currentSong?.let {
            view.showSong(it)
            currentProgress = 0f
            view.updateProgress(0f, "00:00")
        }
    }

    override fun onFavoriteClick() {
        currentSong?.let { song ->
            if (isFavorite) {
                // 已收藏，提示用户
                view.showSuccess("已在我喜欢的音乐中")
            } else {
                // 未收藏，添加到"我喜欢的音乐"
                val success = DataLoader.addSongToPlaylist(context, "my_favorites", song.songId)
                if (success) {
                    isFavorite = true
                    view.updateFavoriteState(isFavorite)

                    // 保存收藏记录
                    try {
                        val collectionId = DataLoader.generateCollectionId(context)
                        val record = CollectionRecord(
                            collectionId = collectionId,
                            contentType = "song_to_favorites",
                            contentId = song.songId,
                            contentName = song.songName,
                            collectionTime = System.currentTimeMillis(),
                            isSuccess = true
                        )
                        DataLoader.saveCollectionRecord(context, record)

                        // 记录到自动化测试文件
                        AutoTestHelper.addFavoriteSong(song.songId, song.songName, song.artist)

                        view.showSuccess("成功收藏")
                    } catch (e: Exception) {
                        e.printStackTrace()
                        view.showSuccess("成功收藏")
                    }
                } else {
                    view.showError("收藏失败")
                }
            }
        }
    }

    override fun onCommentClick() {
        currentSong?.let { song ->
            view.navigateToComment(song.songId)
        }
    }

    override fun onMoreClick() {
        // TODO: 显示更多选项
    }

    override fun onRefreshClick() {
        // 刷新推荐，选择新的随机歌曲
        if (songs.isNotEmpty()) {
            currentSong = songs[Random.nextInt(songs.size)]
            currentSong?.let {
                view.showSong(it)
                currentProgress = 0f
                view.updateProgress(0f, "00:00")
            }
        }
    }

    override fun onProgressChange(progress: Float) {
        currentProgress = progress
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

/**
 * 播放页面契约接口
 */
interface PlayContract {
    interface View : BaseView {
        fun showSong(song: Song)
        fun updatePlayState(isPlaying: Boolean)
        fun updateFavoriteState(isFavorite: Boolean)
        fun updateProgress(progress: Float, currentTime: String)
        fun updatePlayMode(mode: PlayMode)
        fun navigateToComment(songId: String)
        fun showSuccess(message: String)
    }

    interface Presenter : BasePresenter {
        fun loadData()
        fun loadSongById(songId: String)
        fun onPlayPauseClick()
        fun onPreviousClick()
        fun onNextClick()
        fun onFavoriteClick()
        fun onCommentClick()
        fun onMoreClick()
        fun onRefreshClick()
        fun onProgressChange(progress: Float)
        fun startPlayback()
        fun stopPlayback()
        fun setProgressUpdateCallback(callback: () -> Unit)
        fun onPlayModeClick()
    }
}
