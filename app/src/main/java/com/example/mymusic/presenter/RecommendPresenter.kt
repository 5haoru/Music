package com.example.mymusic.presenter

import android.content.Context
import com.example.mymusic.data.Playlist
import com.example.mymusic.data.Song
import com.example.mymusic.utils.DataLoader

/**
 * 推荐页面Presenter
 */
class RecommendPresenter(
    private val view: RecommendContract.View,
    private val context: Context
) : RecommendContract.Presenter {

    private var songs: List<Song> = emptyList()
    private var playlists: List<Playlist> = emptyList()

    override fun loadData() {
        view.showLoading()
        try {
            songs = DataLoader.loadSongs(context)
            playlists = DataLoader.loadPlaylists(context)

            // 过滤掉"我喜欢的音乐"，它只在"我的"页面显示
            val displayPlaylists = playlists.filter { it.playlistId != "my_favorites" }

            view.hideLoading()
            view.showRecommendedSongs(songs.take(5))
            view.showDailyRecommendPlaylists(displayPlaylists)
            view.showRankingPlaylists(displayPlaylists.takeLast(3))
        } catch (e: Exception) {
            view.hideLoading()
            view.showError("加载数据失败: ${e.message}")
        }
    }

    override fun onSongClick(songId: String) {
        val song = songs.find { it.songId == songId }
        song?.let {
            view.playSong(it)
        }
    }

    override fun onPlaylistClick(playlistId: String) {
        val playlist = playlists.find { it.playlistId == playlistId }
        playlist?.let {
            view.openPlaylist(it)
        }
    }

    override fun onSearchClick() {
        view.openSearch()
    }

    override fun onMicClick() {
        view.openListenRecognize()
    }

    override fun onDestroy() {
        // Clean up resources
    }
}

/**
 * 推荐页面契约接口
 */
interface RecommendContract {
    interface View : BaseView {
        fun showRecommendedSongs(songs: List<Song>)
        fun showDailyRecommendPlaylists(playlists: List<Playlist>)
        fun showRankingPlaylists(playlists: List<Playlist>)
        fun playSong(song: Song)
        fun openPlaylist(playlist: Playlist)
        fun openSearch()
        fun openListenRecognize()
    }

    interface Presenter : BasePresenter {
        fun loadData()
        fun onSongClick(songId: String)
        fun onPlaylistClick(playlistId: String)
        fun onSearchClick()
        fun onMicClick()
    }
}
