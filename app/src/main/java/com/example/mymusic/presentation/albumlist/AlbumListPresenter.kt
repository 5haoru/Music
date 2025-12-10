package com.example.mymusic.presentation.albumlist

import com.example.mymusic.data.repository.AlbumRepository

/**
 * 专辑列表页面的 Presenter
 */
class AlbumListPresenter(
    private val view: AlbumListContract.View,
    private val albumRepository: AlbumRepository
) : AlbumListContract.Presenter {

    override fun loadAlbumsByArtist(artistId: String) {
        view.showLoading()
        try {
            val artistAlbums = albumRepository.getAlbumsByArtist(artistId)
            view.showAlbums(artistAlbums)
            view.hideLoading()
        } catch (e: Exception) {
            view.hideLoading()
            view.showError("加载专辑列表失败: ${e.message}")
        }
    }

    override fun onAlbumClick(albumId: String) {
        view.navigateToAlbum(albumId)
    }

    override fun onBackClick() {
        view.navigateBack()
    }

    override fun onDestroy() {
        // 清理资源
    }
}
