package com.example.mymusic.presentation.me.contract

import com.example.mymusic.base.BasePresenter
import com.example.mymusic.base.BaseView
import com.example.mymusic.data.Playlist
import com.example.mymusic.data.Song
import com.example.mymusic.data.User

/**
 * 我的页面契约接口
 */
interface MeContract {
    interface View : BaseView {
        fun showUser(user: User)
        fun showPlaylists(playlists: List<Playlist>)
        fun showCurrentSong(song: Song)
        fun navigateToPlayTab()
        fun navigateToPlaylist(playlist: Playlist)
    }

    interface Presenter : BasePresenter {
        fun loadData()
        fun onFollowClick()
        fun onCreatePlaylistClick()
        fun createPlaylist(title: String, isPrivate: Boolean, isMusic: Boolean)
        fun onPlaylistClick(playlist: Playlist)
        fun onMiniPlayerClick()
        fun onMiniPlayerPlayPauseClick()
    }
}
