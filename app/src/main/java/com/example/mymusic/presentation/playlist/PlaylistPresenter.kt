package com.example.mymusic.presentation.playlist

import com.example.mymusic.data.Playlist
import com.example.mymusic.data.Song
import com.example.mymusic.data.repository.CollectionRepository
import com.example.mymusic.data.repository.SongRepository
import com.example.mymusic.data.model.CollectionRecord
import com.example.mymusic.utils.AutoTestHelper

/**
 * 歌单详情页面Presenter
 * 处理歌单歌曲列表的业务逻辑
 */
class PlaylistPresenter(
    private val view: PlaylistContract.View,
    private val songRepository: SongRepository,
    private val collectionRepository: CollectionRepository
) : PlaylistContract.Presenter {

    private var playlistSongs: List<Song> = emptyList()
    private var currentPlaylist: Playlist? = null

    override fun loadPlaylistSongs(playlist: Playlist) {
        view.showLoading()
        currentPlaylist = playlist
        try {
            // 加载所有歌曲
            val allSongs = songRepository.getAllSongs()

            // 根据歌单的songIds过滤歌曲
            playlistSongs = allSongs.filter { song ->
                playlist.songIds.contains(song.songId)
            }

            view.showSongs(playlistSongs)
            view.hideLoading()
        } catch (e: Exception) {
            view.hideLoading()
            view.showError("加载歌曲失败: ${e.message}")
        }
    }

    override fun onSongClick(song: Song) {
        // 跳转到播放页面
        view.navigateToPlay(song)
    }

    override fun onPlayAllClick() {
        // 播放全部：播放第一首歌
        if (playlistSongs.isNotEmpty()) {
            view.navigateToPlay(playlistSongs[0])
        }
    }

    override fun onShareClick() {
        // TODO: 分享功能
        view.showError("分享功能待开发")
    }

    override fun onCommentClick() {
        // TODO: 评论功能
        view.showError("评论功能待开发")
    }

    override fun onCollectClick() {
        currentPlaylist?.let { playlist ->
            // 记录收藏到AutoTestHelper
            AutoTestHelper.addCollectedPlaylist(
                playlistId = playlist.playlistId,
                playlistName = playlist.playlistName
            )

            // 保存收藏记录
            val record = CollectionRecord(
                collectionId = collectionRepository.generateCollectionId(),
                contentType = "playlist",
                contentId = playlist.playlistId,
                contentName = playlist.playlistName,
                collectionTime = System.currentTimeMillis(),
                isSuccess = true
            )
            collectionRepository.saveCollectionRecord(record)

            // 显示收藏成功提示
            view.showSuccess("成功收藏《${playlist.playlistName}》")
        }
    }

    override fun onDestroy() {
        // 清理资源
    }
}
