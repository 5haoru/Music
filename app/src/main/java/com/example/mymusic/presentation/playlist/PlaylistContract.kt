package com.example.mymusic.presentation.playlist

import com.example.mymusic.base.BasePresenter
import com.example.mymusic.base.BaseView
import com.example.mymusic.data.Playlist
import com.example.mymusic.data.Song

/**
 * 歌单详情页面契约接口
 */
interface PlaylistContract {
    interface View : BaseView {
        fun showSongs(songs: List<Song>)
        fun navigateToPlay(song: Song)
        fun navigateToUnderDevelopment(feature: String)
        override fun showSuccess(message: String)
    }

    interface Presenter : BasePresenter {
        fun loadPlaylistSongs(playlist: Playlist)
        fun onSongClick(song: Song)
        fun onPlayAllClick()
        fun onShareClick()
        fun onCommentClick()
        fun onCollectClick()
    }
}
