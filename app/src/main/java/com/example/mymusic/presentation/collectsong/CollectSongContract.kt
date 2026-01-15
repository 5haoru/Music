package com.example.mymusic.presentation.collectsong

import com.example.mymusic.base.BasePresenter
import com.example.mymusic.base.BaseView
import com.example.mymusic.data.Playlist

/**
 * 收藏到歌单页面契约接《
 * 用于展示歌单列表并将歌曲收藏到选定的歌单中
 */
interface CollectSongContract {
    interface View : BaseView {
        fun showPlaylists(playlists: List<Playlist>)
        fun updatePlaylistAddedState(playlistId: String, isAdded: Boolean)
        override fun showSuccess(message: String)
        fun close()
        fun navigateToCreatePlaylist()
    }

    interface Presenter : BasePresenter {
        fun loadPlaylists(songId: String)
        fun onPlaylistClick(playlistId: String, playlistName: String)
        fun onCreatePlaylistClick()
        fun onBackClick()
        fun onCommonClick()
        fun onMultiSelectClick()
    }
}
