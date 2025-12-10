package com.example.mymusic.presentation.lyric

import com.example.mymusic.data.Song
import com.example.mymusic.data.repository.CollectionRepository
import com.example.mymusic.data.repository.LyricRepository
import com.example.mymusic.data.repository.PlaylistRepository
import com.example.mymusic.data.repository.SongRepository
import com.example.mymusic.data.model.CollectionRecord

/**
 * 歌词页面Presenter
 */
class LyricPresenter(
    private val view: LyricContract.View,
    private val songRepository: SongRepository,
    private val lyricRepository: LyricRepository,
    private val playlistRepository: PlaylistRepository,
    private val collectionRepository: CollectionRepository
) : LyricContract.Presenter {

    private var currentSong: Song? = null
    private var isPlaying: Boolean = true
    private var isFavorite: Boolean = false
    private var currentProgress: Float = 0f
    private var currentLyricIndex: Int = 0
    private var lyricsList: List<String> = emptyList()

    override fun loadData(songId: String) {
        view.showLoading()
        try {
            val song = songRepository.getSongById(songId)
            if (song != null) {
                currentSong = song
                view.showSong(song)

                // For this project, we get lyrics from the song object itself.
                // In a real app, you might use lyricRepository.getLyricForSong(songId)
                lyricsList = song.lyrics.split("\n").filter { it.isNotBlank() }
                view.showLyrics(lyricsList)

                // Check favorite status
                val favoritesPlaylist = playlistRepository.getAllPlaylists().find { it.playlistId == "my_favorites" }
                isFavorite = favoritesPlaylist?.songIds?.contains(songId) == true
                view.updateFavoriteState(isFavorite)

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
        currentSong?.let { song ->
            if (isFavorite) {
                // 已收藏，取消收藏
                val success = playlistRepository.removeSongFromPlaylist("my_favorites", song.songId)
                if (success) {
                    isFavorite = false
                    view.updateFavoriteState(isFavorite)
                } else {
                    view.showError("取消收藏失败")
                }
            } else {
                // 未收藏，添加到“我喜欢的音乐”
                val success = playlistRepository.addSongToPlaylist("my_favorites", song.songId)
                if (success) {
                    isFavorite = true
                    view.updateFavoriteState(isFavorite)
                    val record = CollectionRecord(
                        collectionId = collectionRepository.generateCollectionId(),
                        contentType = "song_to_favorites",
                        contentId = song.songId,
                        contentName = song.songName,
                        collectionTime = System.currentTimeMillis(),
                        isSuccess = true
                    )
                    collectionRepository.saveCollectionRecord(record)
                } else {
                    view.showError("收藏失败")
                }
            }
        }
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
