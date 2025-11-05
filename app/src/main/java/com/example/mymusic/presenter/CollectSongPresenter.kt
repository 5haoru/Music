package com.example.mymusic.presenter

import android.content.Context
import com.example.mymusic.data.Playlist
import com.example.mymusic.model.CollectionRecord
import com.example.mymusic.utils.AutoTestHelper
import com.example.mymusic.utils.DataLoader

/**
 * 收藏到歌单页面Presenter
 * 处理歌单列表加载和歌曲收藏操作的业务逻辑
 */
class CollectSongPresenter(
    private val view: CollectSongContract.View,
    private val context: Context
) : CollectSongContract.Presenter {

    private var currentSongId: String? = null
    private var playlists: List<Playlist> = emptyList()
    private val addedPlaylistIds = mutableSetOf<String>()

    override fun loadPlaylists(songId: String) {
        view.showLoading()
        try {
            currentSongId = songId

            // 加载所有歌单（优先从缓存加载用户创建的歌单）
            playlists = DataLoader.loadPlaylistsWithCache(context)

            // 检查歌曲是否已在各个歌单中
            checkSongInPlaylists(songId)

            view.showPlaylists(playlists)
            view.hideLoading()
        } catch (e: Exception) {
            view.hideLoading()
            view.showError("加载歌单失败: ${e.message}")
        }
    }

    /**
     * 检查歌曲是否已经在各个歌单中
     */
    private fun checkSongInPlaylists(songId: String) {
        addedPlaylistIds.clear()
        for (playlist in playlists) {
            if (playlist.songIds.contains(songId)) {
                addedPlaylistIds.add(playlist.playlistId)
                view.updatePlaylistAddedState(playlist.playlistId, true)
            }
        }
    }

    override fun onPlaylistClick(playlistId: String, playlistName: String) {
        val songId = currentSongId ?: return

        try {
            // 查找对应的歌单
            val playlist = playlists.find { it.playlistId == playlistId }
            if (playlist == null) {
                view.showError("未找到歌单")
                return
            }

            // 检查歌曲是否已在歌单中
            if (addedPlaylistIds.contains(playlistId)) {
                view.showSuccess("歌曲已在「$playlistName」中")
                return
            }

            // 将歌曲添加到歌单
            val addResult = DataLoader.addSongToPlaylist(context, playlistId, songId)
            if (!addResult) {
                view.showError("添加歌曲到歌单失败")
                return
            }

            // 保存收藏记录
            val collectionId = DataLoader.generateCollectionId(context)
            val song = DataLoader.getSongById(context, songId)
            val record = CollectionRecord(
                collectionId = collectionId,
                contentType = "song_to_playlist",
                contentId = songId,
                contentName = song?.songName ?: "未知歌曲",
                collectionTime = System.currentTimeMillis(),
                isSuccess = true
            )
            DataLoader.saveCollectionRecord(context, record)

            // 重新加载歌单列表以获取最新数据
            playlists = DataLoader.loadPlaylistsWithCache(context)

            // 同步到AutoTestHelper用于自动化测试
            val updatedPlaylist = playlists.find { it.playlistId == playlistId }
            updatedPlaylist?.let {
                AutoTestHelper.updatePlaylistSongs(playlistId, it.songIds)
            }

            // 更新UI状态
            addedPlaylistIds.add(playlistId)
            view.updatePlaylistAddedState(playlistId, true)

            // 显示成功提示
            view.showSuccess("已成功收藏到「$playlistName」")
        } catch (e: Exception) {
            view.showError("收藏失败: ${e.message}")
        }
    }

    override fun onCreatePlaylistClick() {
        view.navigateToCreatePlaylist()
    }

    override fun onBackClick() {
        view.close()
    }

    override fun onCommonClick() {
        // 常用功能：可以按使用频率排序歌单
        view.showSuccess("常用功能")
    }

    override fun onMultiSelectClick() {
        // 多选功能：允许同时将歌曲添加到多个歌单
        view.showSuccess("多选功能")
    }

    override fun onDestroy() {
        // 清理资源
    }
}
