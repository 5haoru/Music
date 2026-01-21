package com.example.mymusic.presentation.collectsong

import com.example.mymusic.data.Playlist
import com.example.mymusic.data.repository.CollectionRepository
import com.example.mymusic.data.repository.PlaylistRepository
import com.example.mymusic.data.repository.SongRepository
import com.example.mymusic.data.model.CollectionRecord
import com.example.mymusic.utils.AutoTestHelper

/**
 * 收藏到歌单页面Presenter
 * 处理歌单列表加载和歌曲收藏操作的业务逻辑
 */
class CollectSongPresenter(
    private val view: CollectSongContract.View,
    private val playlistRepository: PlaylistRepository,
    private val songRepository: SongRepository,
    private val collectionRepository: CollectionRepository
) : CollectSongContract.Presenter {

    private var currentSongId: String? = null
    private var playlists: List<Playlist> = emptyList()
    private val addedPlaylistIds = mutableSetOf<String>()

    override fun loadPlaylists(songId: String) {
        view.showLoading()
        try {
            currentSongId = songId
            playlists = playlistRepository.getAllPlaylists()
            checkSongInPlaylists(songId)
            view.showPlaylists(playlists)
            view.hideLoading()
        } catch (e: Exception) {
            view.hideLoading()
            view.showError("加载歌单失败: ${e.message}")
        }
    }

    private fun checkSongInPlaylists(songId: String) {
        addedPlaylistIds.clear()
        for (playlist in playlists) {
            if (playlist.songIds.contains(songId)) {
                addedPlaylistIds.add(playlist.playlistId)
                view.updatePlaylistAddedState(playlist.playlistId, true)
            }
        }
    }

    override fun onPlaylistClick(playlistId: String, playlistName: String) {
        val songId = currentSongId ?: return

        try {
            if (addedPlaylistIds.contains(playlistId)) {
                view.showSuccess("歌曲已在「$playlistName」中")
                return
            }

            if (!playlistRepository.addSongToPlaylist(playlistId, songId)) {
                view.showError("添加歌曲到歌单失败")
                return
            }

            val song = songRepository.getSongById(songId)
            val record = CollectionRecord(
                collectionId = collectionRepository.generateCollectionId(),
                contentType = "song_to_playlist",
                contentId = songId,
                contentName = song?.songName ?: "未知歌曲",
                collectionTime = System.currentTimeMillis(),
                isSuccess = true
            )
            collectionRepository.saveCollectionRecord(record)

            playlists = playlistRepository.getAllPlaylists()
            AutoTestHelper.updatePlaylistSongs(playlistId, playlists.find { it.playlistId == playlistId }?.songIds ?: emptyList())

            // 如果是收藏到"我喜欢的音乐"，还需要更新user_favorites.json
            if (playlistId == "my_favorites" && song != null) {
                AutoTestHelper.addFavoriteSong(song.songId, song.songName, song.artist)
            }

            addedPlaylistIds.add(playlistId)
            view.updatePlaylistAddedState(playlistId, true)
            view.showSuccess("已成功收藏到「$playlistName」")
        } catch (e: Exception) {
            view.showError("收藏失败: ${e.message}")
        }
    }

    override fun onCreatePlaylistClick() {
        view.navigateToCreatePlaylist()
    }

    override fun onBackClick() {
        view.close()
    }

    override fun onCommonClick() {
        // 常用功能：可以按使用频率排序歌单
        view.showSuccess("常用功能")
    }

    override fun onMultiSelectClick() {
        // 多选功能：允许同时将歌曲添加到多个歌单
        view.showSuccess("多选功能")
    }

    override fun onDestroy() {
        // 清理资源
    }
}
