package com.example.mymusic.presenter

import android.content.Context
import com.example.mymusic.data.Playlist
import com.example.mymusic.data.Song
import com.example.mymusic.data.User
import com.example.mymusic.utils.DataLoader

/**
 * 我的页面Presenter
 */
class MePresenter(
    private val view: MeContract.View,
    private val context: Context
) : MeContract.Presenter {

    private var currentUser: User? = null
    private var userPlaylists: List<Playlist> = emptyList()
    private var currentPlayingSong: Song? = null

    override fun loadData() {
        view.showLoading()
        try {
            // 加载用户数据（使用第一个用户）
            val users = DataLoader.loadUsers(context)
            if (users.isNotEmpty()) {
                currentUser = users[0]
                currentUser?.let { view.showUser(it) }
            }

            // 加载歌单数据（优先从缓存加载）
            userPlaylists = DataLoader.loadPlaylistsWithCache(context)
            view.showPlaylists(userPlaylists)

            // 加载当前播放歌曲（使用第一首歌）
            val songs = DataLoader.loadSongs(context)
            if (songs.isNotEmpty()) {
                currentPlayingSong = songs[0]
                currentPlayingSong?.let { view.showCurrentSong(it) }
            }

            view.hideLoading()
        } catch (e: Exception) {
            view.hideLoading()
            view.showError("加载数据失败: ${e.message}")
        }
    }

    override fun onFollowClick() {
        // TODO: 处理关注逻辑
    }

    override fun onCreatePlaylistClick() {
        // 由View层显示创建歌单对话框
    }

    override fun createPlaylist(title: String, isPrivate: Boolean, isMusic: Boolean) {
        try {
            // 生成新歌单
            val newPlaylist = Playlist(
                playlistId = DataLoader.generatePlaylistId(),
                playlistName = title,
                description = if (isPrivate) "隐私歌单" else "共享歌单",
                coverUrl = "cover/1.png", // 默认封面
                songIds = emptyList(),
                createTime = System.currentTimeMillis(),
                songCount = 0
            )

            // 添加到歌单列表
            val updatedPlaylists = userPlaylists.toMutableList()
            updatedPlaylists.add(newPlaylist)
            userPlaylists = updatedPlaylists

            // 保存到本地
            DataLoader.savePlaylists(context, userPlaylists)

            // 刷新UI
            view.showPlaylists(userPlaylists)
        } catch (e: Exception) {
            view.showError("创建歌单失败: ${e.message}")
        }
    }

    override fun onPlaylistClick(playlist: Playlist) {
        view.navigateToPlaylist(playlist)
    }

    override fun onMiniPlayerClick() {
        // TODO: 跳转到播放页面
        view.navigateToPlayTab()
    }

    override fun onMiniPlayerPlayPauseClick() {
        // TODO: 播放/暂停
    }

    override fun onDestroy() {
        // Clean up resources
    }
}

/**
 * 我的页面契约接口
 */
interface MeContract {
    interface View : BaseView {
        fun showUser(user: User)
        fun showPlaylists(playlists: List<Playlist>)
        fun showCurrentSong(song: Song)
        fun navigateToPlayTab()
        fun navigateToPlaylist(playlist: Playlist)
    }

    interface Presenter : BasePresenter {
        fun loadData()
        fun onFollowClick()
        fun onCreatePlaylistClick()
        fun createPlaylist(title: String, isPrivate: Boolean, isMusic: Boolean)
        fun onPlaylistClick(playlist: Playlist)
        fun onMiniPlayerClick()
        fun onMiniPlayerPlayPauseClick()
    }
}
