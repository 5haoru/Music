package com.example.mymusic.presentation.albumlist

import com.example.mymusic.base.BasePresenter
import com.example.mymusic.base.BaseView
import com.example.mymusic.data.Album

/**
 * 专辑列表页面的 Contract
 */
interface AlbumListContract {

    interface View : BaseView {
        /**
         * 显示专辑列表
         */
        fun showAlbums(albums: List<Album>)

        /**
         * 导航到专辑详情
         */
        fun navigateToAlbum(albumId: String)

        /**
         * 返回上一页
         */
        fun navigateBack()
    }

    interface Presenter : BasePresenter {
        /**
         * 加载专辑列表
         */
        fun loadAlbumsByArtist(artistId: String)

        /**
         * 点击专辑
         */
        fun onAlbumClick(albumId: String)

        /**
         * 返回
         */
        fun onBackClick()
    }
}
