package com.example.mymusic.presentation.play

import com.example.mymusic.data.Song
import com.example.mymusic.data.repository.CollectionRepository
import com.example.mymusic.data.repository.PlaylistRepository
import com.example.mymusic.data.repository.SongRepository
import com.example.mymusic.data.model.CollectionRecord
import com.example.mymusic.utils.AutoTestHelper
import kotlin.random.Random

class PlayPresenter(
    private val view: PlayContract.View,
    private val songRepository: SongRepository,
    private val playlistRepository: PlaylistRepository,
    private val collectionRepository: CollectionRepository
) : PlayContract.Presenter {

    private var songs: List<Song> = emptyList()
    private var currentSong: Song? = null
    private var currentSongIndex: Int = -1
    private var isPlaying: Boolean = false
    private var isFavorite: Boolean = false
    private var currentPlayMode: PlayMode = PlayMode.SEQUENCE
    private var progressUpdateCallback: (() -> Unit)? = null

    override fun loadData() {
        view.showLoading()
        try {
            songs = songRepository.getAllSongs()
            if (songs.isNotEmpty()) {
                currentSongIndex = 0
                currentSong = songs[currentSongIndex]
                currentSong?.let {
                    view.showSong(it)
                    checkFavoriteStatus(it.songId)
                    AutoTestHelper.updatePlayback(songId = it.songId, songName = it.songName, artist = it.artist)
                }
                view.updatePlayMode(currentPlayMode)
                view.hideLoading()
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
            val targetSong = songRepository.getSongById(songId)
            if (targetSong != null) {
                currentSong = targetSong
                songs = songRepository.getAllSongs()
                currentSongIndex = songs.indexOf(targetSong)
                view.showSong(targetSong)
                checkFavoriteStatus(songId)
                AutoTestHelper.updatePlayback(songId = targetSong.songId, songName = targetSong.songName, artist = targetSong.artist)
                view.hideLoading()
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
        val favoritesPlaylist = playlistRepository.getAllPlaylists().find { it.playlistId == "my_favorites" }
        isFavorite = favoritesPlaylist?.songIds?.contains(songId) == true
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

    override fun onPreviousClick() {
        if (songs.isEmpty()) return
        currentSongIndex = when (currentPlayMode) {
            PlayMode.SEQUENCE, PlayMode.SINGLE -> (currentSongIndex - 1 + songs.size) % songs.size
            PlayMode.SHUFFLE -> Random.nextInt(songs.size)
        }
        currentSong = songs[currentSongIndex]
        currentSong?.let {
            view.showSong(it)
            checkFavoriteStatus(it.songId)
            AutoTestHelper.updatePlayback(songId = it.songId, songName = it.songName, artist = it.artist)
        }
    }

    override fun onNextClick() {
        if (songs.isEmpty()) return
        currentSongIndex = when (currentPlayMode) {
            PlayMode.SEQUENCE, PlayMode.SINGLE -> (currentSongIndex + 1) % songs.size
            PlayMode.SHUFFLE -> Random.nextInt(songs.size)
        }
        currentSong = songs[currentSongIndex]
        currentSong?.let {
            view.showSong(it)
            checkFavoriteStatus(it.songId)
            AutoTestHelper.updatePlayback(songId = it.songId, songName = it.songName, artist = it.artist)
        }
    }

    override fun onFavoriteClick() {
        currentSong?.let { song ->
            if (isFavorite) {
                if (playlistRepository.removeSongFromPlaylist("my_favorites", song.songId)) {
                    isFavorite = false
                    view.updateFavoriteState(isFavorite)
                    AutoTestHelper.removeFavoriteSong(song.songId)
                    view.showSuccess("已取消收藏")
                } else {
                    view.showError("取消收藏失败")
                }
            } else {
                if (playlistRepository.addSongToPlaylist("my_favorites", song.songId)) {
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
                    AutoTestHelper.addFavoriteSong(song.songId, song.songName, song.artist)
                    view.showSuccess("成功收藏")
                } else {
                    view.showError("收藏失败")
                }
            }
        }
    }

    override fun onCommentClick() {
        currentSong?.let { view.navigateToComment(it.songId) }
    }

    override fun onMoreClick() {
        // TODO: Implement more options logic
    }

    override fun onRefreshClick() {
        loadData()
    }

    override fun onProgressChange(progress: Float) {
        // TODO: Handle progress change
    }

    override fun startPlayback() {
        // TODO: Start playback timer or media player
    }

    override fun stopPlayback() {
        // TODO: Stop playback timer or media player
    }

    override fun setProgressUpdateCallback(callback: () -> Unit) {
        progressUpdateCallback = callback
    }

    override fun onPlayModeClick() {
        currentPlayMode = when (currentPlayMode) {
            PlayMode.SEQUENCE -> PlayMode.SHUFFLE
            PlayMode.SHUFFLE -> PlayMode.SINGLE
            PlayMode.SINGLE -> PlayMode.SEQUENCE
        }
        view.updatePlayMode(currentPlayMode)
    }

    override fun onDestroy() {
        stopPlayback()
    }
}
