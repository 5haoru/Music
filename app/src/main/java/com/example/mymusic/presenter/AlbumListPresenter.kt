package com.example.mymusic.presenter

import android.content.Context
import com.example.mymusic.data.Album
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * 专辑列表页面的 Presenter
 */
class AlbumListPresenter(
    private val view: AlbumListContract.View,
    private val context: Context
) : AlbumListContract.Presenter {

    override fun loadAlbumsByArtist(artistId: String) {
        view.showLoading()
        try {
            // 加载所有专辑
            val json = context.assets.open("data/albums.json").bufferedReader().use { it.readText() }
            val type = object : TypeToken<List<Album>>() {}.type
            val allAlbums: List<Album> = Gson().fromJson(json, type)

            // 筛选该歌手的专辑
            val artistAlbums = allAlbums.filter { it.artistId == artistId }

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
