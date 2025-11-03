package com.example.mymusic.presenter

import android.content.Context
import com.example.mymusic.data.Album
import com.example.mymusic.data.Song
import com.example.mymusic.utils.DataLoader
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * 专辑详情页面的 Presenter
 */
class AlbumPresenter(
    private val view: AlbumContract.View,
    private val context: Context
) : AlbumContract.Presenter {

    private var currentAlbum: Album? = null
    private var songs: List<Song> = emptyList()

    override fun loadAlbumDetail(albumId: String) {
        view.showLoading()
        try {
            // 加载专辑信息
            val json = context.assets.open("data/albums.json").bufferedReader().use { it.readText() }
            val type = object : TypeToken<List<Album>>() {}.type
            val albums: List<Album> = Gson().fromJson(json, type)
            currentAlbum = albums.find { it.albumId == albumId }

            if (currentAlbum == null) {
                view.showError("未找到专辑信息")
                return
            }

            view.showAlbumInfo(currentAlbum!!)

            // 加载专辑中的歌曲
            val allSongs = DataLoader.loadSongs(context)
            songs = currentAlbum!!.songIds.mapNotNull { songId ->
                allSongs.find { it.songId == songId }
            }

            view.showSongs(songs)
            view.hideLoading()
        } catch (e: Exception) {
            view.hideLoading()
            view.showError("加载数据失败: ${e.message}")
        }
    }

    override fun onCollectClick() {
        currentAlbum?.let { album ->
            view.showSuccess("成功收藏《${album.albumName}》")
        }
    }

    override fun onCommentClick() {
        view.showSuccess("评论功能待开发")
    }

    override fun onShareClick() {
        view.showSuccess("分享功能待开发")
    }

    override fun onPlayAllClick() {
        if (songs.isNotEmpty()) {
            view.navigateToPlay(songs[0].songId)
        }
    }

    override fun onSongClick(songId: String) {
        view.navigateToPlay(songId)
    }

    override fun onArtistClick() {
        currentAlbum?.let { album ->
            view.navigateToSinger(album.artistId)
        }
    }

    override fun onBackClick() {
        view.navigateBack()
    }

    override fun onDestroy() {
        // 清理资源
    }
}
