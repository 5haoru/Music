package com.example.mymusic.presentation.play

import com.example.mymusic.base.BasePresenter
import com.example.mymusic.base.BaseView
import com.example.mymusic.data.Song

/**
 * 播放页面契约接口
 */
interface PlayContract {
    interface View : BaseView {
        fun showSong(song: Song)
        fun updatePlayState(isPlaying: Boolean)
        fun updateFavoriteState(isFavorite: Boolean)
        fun updateProgress(progress: Float, currentTime: String)
        fun updatePlayMode(mode: PlayMode)
        fun navigateToComment(songId: String)
        override fun showSuccess(message: String)
    }

    interface Presenter : BasePresenter {
        fun loadData()
        fun loadSongById(songId: String)
        fun onPlayPauseClick()
        fun onPreviousClick()
        fun onNextClick()
        fun onFavoriteClick()
        fun onCommentClick()
        fun onMoreClick()
        fun onRefreshClick()
        fun onProgressChange(progress: Float)
        fun startPlayback()
        fun stopPlayback()
        fun setProgressUpdateCallback(callback: () -> Unit)
        fun onPlayModeClick()
    }
}

enum class PlayMode {
    SEQUENCE, SHUFFLE, SINGLE
}