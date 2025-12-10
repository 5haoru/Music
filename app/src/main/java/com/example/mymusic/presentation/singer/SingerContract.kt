package com.example.mymusic.presentation.singer

import com.example.mymusic.base.BasePresenter
import com.example.mymusic.base.BaseView
import com.example.mymusic.data.Artist
import com.example.mymusic.data.MusicVideo
import com.example.mymusic.data.SongDetail

/**
 * 歌手详情页面�?MVP 契约
 */
interface SingerContract {

    interface View : BaseView {
        /**
         * 显示歌手信息
         */
        fun showSingerInfo(artist: Artist)

        /**
         * 显示歌曲列表
         */
        fun showSongs(songs: List<SongDetail>)

        /**
         * 显示MV列表
         */
        fun showMVs(mvs: List<MusicVideo>)

        /**
         * 更新关注状�?
         */
        fun updateFollowStatus(isFollowing: Boolean)

        /**
         * 导航返回
         */
        fun navigateBack()

        /**
         * 导航到播放页�?
         */
        fun navigateToPlay(songId: String)

        /**
         * 导航到MV播放页面
         */
        fun navigateToMVPlayer(mvId: String)
    }

    interface Presenter : BasePresenter {
        /**
         * 加载歌手数据
         */
        fun loadSingerData(artistId: String)

        /**
         * 点击关注/取消关注
         */
        fun onFollowClick()

        /**
         * 点击歌曲
         */
        fun onSongClick(songId: String)

        /**
         * 点击MV
         */
        fun onMVClick(mvId: String)

        /**
         * 点击返回
         */
        fun onBackClick()
    }
}
