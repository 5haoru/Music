package com.example.mymusic.presentation.album

import com.example.mymusic.data.Album
import com.example.mymusic.data.Song
import com.example.mymusic.data.repository.AlbumRepository
import com.example.mymusic.data.repository.SongRepository
import com.example.mymusic.utils.AutoTestHelper

/**
 * 专辑详情页面的 Presenter
 */
class AlbumPresenter(
    private val view: AlbumContract.View,
    private val albumRepository: AlbumRepository,
    private val songRepository: SongRepository
) : AlbumContract.Presenter {

    private var currentAlbum: Album? = null
    private var songs: List<Song> = emptyList()

    override fun loadAlbumDetail(albumId: String) {
        view.showLoading()
        try {
            currentAlbum = albumRepository.getAlbumById(albumId)

            if (currentAlbum == null) {
                view.showError("未找到专辑信息")
                view.hideLoading()
                return
            }

            // 记录当前查看的专辑ID
            AutoTestHelper.updateCurrentAlbumId(albumId)

            view.showAlbumInfo(currentAlbum!!)

            val allSongs = songRepository.getAllSongs()
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
            // 记录收藏到AutoTestHelper
            AutoTestHelper.addCollectedAlbum(
                albumId = album.albumId,
                albumName = album.albumName,
                artist = album.artist,
                artistId = album.artistId
            )

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
