package com.example.mymusic.presenter

import android.content.Context
import com.example.mymusic.data.Song
import com.example.mymusic.model.CollectionRecord
import com.example.mymusic.utils.DataLoader
import kotlin.random.Random

/**
 * 漫游页面Presenter
 */
class StrollPresenter(
    private val view: StrollContract.View,
    private val context: Context
) : StrollContract.Presenter {

    private var songs: List<Song> = emptyList()
    private var currentSong: Song? = null
    private var isPlaying: Boolean = false
    private var isFavorite: Boolean = false
    private var currentProgress: Float = 0f
    private var progressUpdateCallback: (() -> Unit)? = null

    override fun loadData() {
        view.showLoading()
        try {
            songs = DataLoader.loadSongs(context)
            if (songs.isNotEmpty()) {
                // 随机选择一首歌曲（漫游模式）
                currentSong = songs[Random.nextInt(songs.size)]
                currentSong?.let {
                    view.showSong(it)
                    view.hideLoading()
                    // 检查是否已收藏到"我喜欢的音乐"
                    checkFavoriteStatus(it.songId)
                }
            } else {
                view.hideLoading()
                view.showError("暂无歌曲")
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
                    // 播放完成，自动切换到下一首
                    currentProgress = 0f
                    onNextClick()
                    if (isPlaying) {
                        startPlayback()
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
        // 切换到上一首（随机）
        if (songs.isNotEmpty()) {
            val wasPlaying = isPlaying
            currentSong = songs[Random.nextInt(songs.size)]
            currentSong?.let {
                view.showSong(it)
                currentProgress = 0f
                view.updateProgress(0f, "00:00")
                // 检查是否已收藏到"我喜欢的音乐"
                checkFavoriteStatus(it.songId)
                // 如果之前在播放，继续播放新歌
                if (wasPlaying) {
                    startPlayback()
                }
            }
        }
    }

    override fun onNextClick() {
        // 切换到下一首（随机）
        if (songs.isNotEmpty()) {
            val wasPlaying = isPlaying
            currentSong = songs[Random.nextInt(songs.size)]
            currentSong?.let {
                view.showSong(it)
                currentProgress = 0f
                view.updateProgress(0f, "00:00")
                // 检查是否已收藏到"我喜欢的音乐"
                checkFavoriteStatus(it.songId)
                // 如果之前在播放，继续播放新歌
                if (wasPlaying) {
                    startPlayback()
                }
            }
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
        // 打开更多选项（播放定制），由View层处理显示PlayCustomizeTab
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
 * 漫游页面契约接口
 */
interface StrollContract {
    interface View : BaseView {
        fun showSong(song: Song)
        fun updatePlayState(isPlaying: Boolean)
        fun updateFavoriteState(isFavorite: Boolean)
        fun updateProgress(progress: Float, currentTime: String)
        fun navigateToComment(songId: String)
        fun showSuccess(message: String)
    }

    interface Presenter : BasePresenter {
        fun loadData()
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
    }
}
