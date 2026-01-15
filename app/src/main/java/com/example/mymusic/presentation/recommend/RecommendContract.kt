package com.example.mymusic.presentation.recommend

import com.example.mymusic.base.BasePresenter
import com.example.mymusic.base.BaseView
import com.example.mymusic.data.Playlist
import com.example.mymusic.data.Song

/**
 * 推荐页面契约接口
 */
interface RecommendContract {
    interface View : BaseView {
        fun showRecommendedSongs(songs: List<Song>)
        fun showDailyRecommendPlaylists(playlists: List<Playlist>)
        fun showFeaturedPlaylists(playlists: List<Playlist>)
        fun showRankingPlaylists(playlists: List<Playlist>)
        fun playSong(song: Song)
        fun openPlaylist(playlist: Playlist)
        fun openRank(rankId: String)
        fun openSearch()
        fun openListenRecognize()
    }

    interface Presenter : BasePresenter {
        fun loadData()
        fun onSongClick(songId: String)
        fun onPlaylistClick(playlist: Playlist)
        fun onSearchClick()
        fun onMicClick()
    }
}