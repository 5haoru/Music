package com.example.mymusic.presenter

import com.example.mymusic.data.Artist
import com.example.mymusic.data.MusicVideo
import com.example.mymusic.data.SongDetail

/**
 * 搜索结果页面的Contract接口
 */
interface SearchResultContract {
    interface View {
        fun showArtist(artist: Artist?)
        fun showMusicVideo(mv: MusicVideo?)
        fun showSongResults(songs: List<SongDetail>)
        fun showLoading()
        fun hideLoading()
        fun showError(message: String)
        fun navigateBack()
        fun updateFollowStatus(isFollowing: Boolean)
        fun navigateToPlay(songId: String)
        fun navigateToSinger(artistId: String)
    }

    interface Presenter {
        fun loadSearchResults(query: String)
        fun onCategorySelected(category: String)
        fun onFollowArtist(artistId: String)
        fun onMVClick(mvId: String)
        fun onSongClick(songId: String)
        fun onBackClick()
        fun onArtistClick(artistId: String)
    }
}
