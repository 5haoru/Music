package com.example.mymusic.presenter

import android.content.Context
import com.example.mymusic.data.Playlist
import com.example.mymusic.data.Song
import com.example.mymusic.utils.DataLoader

/**
 * 歌单详情页面Presenter
 * 处理歌单歌曲列表的业务逻辑
 */
class PlaylistPresenter(
    private val view: PlaylistContract.View,
    private val context: Context
) : PlaylistContract.Presenter {

    private var playlistSongs: List<Song> = emptyList()
    private var currentPlaylist: Playlist? = null

    override fun loadPlaylistSongs(playlist: Playlist) {
        view.showLoading()
        currentPlaylist = playlist
        try {
            // 加载所有歌曲
            val allSongs = DataLoader.loadSongs(context)

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
            // 显示收藏成功提示
            android.widget.Toast.makeText(
                context,
                "成功收藏《${playlist.playlistName}》",
                android.widget.Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroy() {
        // 清理资源
    }
}
