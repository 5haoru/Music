package com.example.mymusic.presentation.lyric

import com.example.mymusic.base.BasePresenter
import com.example.mymusic.base.BaseView
import com.example.mymusic.data.Song

/**
 * 歌词页面契约接口
 */
interface LyricContract {
    interface View : BaseView {
        fun showSong(song: Song)
        fun showLyrics(lyrics: List<String>)
        fun updateCurrentLyricIndex(index: Int)
        fun updatePlayState(isPlaying: Boolean)
        fun updateFavoriteState(isFavorite: Boolean)
        fun updateProgress(progress: Float, currentTime: String)
        fun closeLyricPage()
        fun navigateToSongProfile(songId: String)
    }

    interface Presenter : BasePresenter {
        fun loadData(songId: String)
        fun onBackClick()
        fun onRefreshClick()
        fun onFollowClick()
        fun onEncyclopediaClick()
        fun onPlayPauseClick()
        fun onPreviousClick()
        fun onNextClick()
        fun onFavoriteClick()
        fun onMoreClick()
        fun onCommentClick()
        fun onProgressChange(progress: Float)
    }
}
