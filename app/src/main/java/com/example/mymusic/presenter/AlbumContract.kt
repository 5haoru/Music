package com.example.mymusic.presenter

import com.example.mymusic.data.Album
import com.example.mymusic.data.Song

/**
 * 专辑详情页面的 Contract
 */
interface AlbumContract {

    interface View : BaseView {
        /**
         * 显示专辑信息
         */
        fun showAlbumInfo(album: Album)

        /**
         * 显示歌曲列表
         */
        fun showSongs(songs: List<Song>)

        /**
         * 返回上一页
         */
        fun navigateBack()

        /**
         * 导航到播放页面
         */
        fun navigateToPlay(songId: String)

        /**
         * 导航到歌手页面
         */
        fun navigateToSinger(artistId: String)

        /**
         * 显示Toast提示
         */
        fun showSuccess(message: String)
    }

    interface Presenter : BasePresenter {
        /**
         * 加载专辑详情
         */
        fun loadAlbumDetail(albumId: String)

        /**
         * 收藏专辑
         */
        fun onCollectClick()

        /**
         * 评论专辑
         */
        fun onCommentClick()

        /**
         * 分享专辑
         */
        fun onShareClick()

        /**
         * 播放全部
         */
        fun onPlayAllClick()

        /**
         * 播放单曲
         */
        fun onSongClick(songId: String)

        /**
         * 点击歌手信息
         */
        fun onArtistClick()

        /**
         * 返回
         */
        fun onBackClick()
    }
}
