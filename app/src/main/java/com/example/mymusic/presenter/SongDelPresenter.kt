package com.example.mymusic.presenter

import android.content.Context
import com.example.mymusic.data.Song
import com.example.mymusic.data.SongDeletionRecord
import com.example.mymusic.utils.AutoTestHelper
import com.example.mymusic.utils.DataLoader
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID

/**
 * 歌曲删除操作页面Presenter
 */
class SongDelPresenter(
    private val view: SongDelContract.View,
    private val context: Context
) : SongDelContract.Presenter {

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)
    private val gson = Gson()

    private var currentSongId: String = ""
    private var currentPlaylistId: String = ""

    override fun loadSongInfo(songId: String, playlistId: String) {
        currentSongId = songId
        currentPlaylistId = playlistId

        scope.launch {
            try {
                view.showLoading()

                // 从assets加载歌曲数据
                val song = withContext(Dispatchers.IO) {
                    loadSongById(songId)
                }

                if (song != null) {
                    view.showSongInfo(song)
                } else {
                    view.showError("未找到歌曲信息")
                }

                view.hideLoading()
            } catch (e: Exception) {
                view.hideLoading()
                view.showError("加载失败: ${e.message}")
            }
        }
    }

    /**
     * 从assets/data/songs.json加载歌曲数据
     */
    private fun loadSongById(songId: String): Song? {
        return try {
            val json = context.assets.open("data/songs.json").bufferedReader().use { it.readText() }
            val type = object : TypeToken<List<Song>>() {}.type
            val songs: List<Song> = gson.fromJson(json, type)
            songs.find { it.songId == songId }
        } catch (e: Exception) {
            null
        }
    }

    override fun onBackClick() {
        view.close()
    }

    override fun onCommentClick() {
        // TODO: 打开评论页面
        view.showError("评论功能")
    }

    override fun onShareClick() {
        // TODO: 打开分享页面
        view.showError("分享功能")
    }

    override fun onPurchaseClick() {
        // TODO: 打开单曲购买页面
        view.showError("单曲购买功能")
    }

    override fun onDeleteClick() {
        view.showDeleteConfirmDialog()
    }

    override fun confirmDelete() {
        scope.launch {
            try {
                view.showLoading()

                // 1. 实际从歌单中删除歌曲
                val deleteSuccess = withContext(Dispatchers.IO) {
                    DataLoader.removeSongFromPlaylist(context, currentPlaylistId, currentSongId)
                }

                if (!deleteSuccess) {
                    view.hideLoading()
                    view.showError("删除失败：歌曲不在歌单中")
                    return@launch
                }

                // 2. 同步到AutoTest
                withContext(Dispatchers.IO) {
                    val updatedPlaylist = DataLoader.getPlaylistById(context, currentPlaylistId)
                    updatedPlaylist?.let {
                        AutoTestHelper.updatePlaylistSongs(currentPlaylistId, it.songIds)
                    }
                }

                // 3. 保存删除记录
                withContext(Dispatchers.IO) {
                    saveDeletionRecord()
                }

                view.hideLoading()
                android.widget.Toast.makeText(context, "删除成功", android.widget.Toast.LENGTH_SHORT).show()
                view.close()
            } catch (e: Exception) {
                view.hideLoading()
                view.showError("删除失败: ${e.message}")
            }
        }
    }

    /**
     * 保存删除记录到song_deletion_records.json
     */
    private fun saveDeletionRecord() {
        try {
            val file = File(context.filesDir, "song_deletion_records.json")

            // 读取现有记录
            val existingRecords = if (file.exists()) {
                val json = file.readText()
                val type = object : TypeToken<List<SongDeletionRecord>>() {}.type
                gson.fromJson<List<SongDeletionRecord>>(json, type).toMutableList()
            } else {
                mutableListOf()
            }

            // 添加新记录
            val newRecord = SongDeletionRecord(
                recordId = UUID.randomUUID().toString(),
                playlistId = currentPlaylistId,
                songId = currentSongId,
                timestamp = System.currentTimeMillis()
            )
            existingRecords.add(newRecord)

            // 保存到文件
            val json = gson.toJson(existingRecords)
            file.writeText(json)
        } catch (e: Exception) {
            throw e
        }
    }

    override fun onRingtoneClick() {
        view.showError("设置铃声功能")
    }

    override fun onGiftCardClick() {
        view.showError("音乐礼品卡功能")
    }

    override fun onDestroy() {
        job.cancel()
    }
}
