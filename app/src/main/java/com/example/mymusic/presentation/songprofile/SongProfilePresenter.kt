package com.example.mymusic.presentation.songprofile

import android.content.Context
import com.example.mymusic.data.Song
import com.example.mymusic.data.SongProfile
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 歌曲档案页面Presenter
 */
class SongProfilePresenter(
    private val view: SongProfileContract.View,
    private val context: Context
) : SongProfileContract.Presenter {

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    override fun loadSongProfile(songId: String) {
        scope.launch {
            try {
                view.showLoading()

                // 从assets加载歌曲数据
                val song = withContext(Dispatchers.IO) {
                    loadSongById(songId)
                }

                if (song != null) {
                    // 生成歌曲档案（使用默认数据）
                    val profile = SongProfile.fromSong(song)
                    view.showSongProfile(profile)
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
            val songs: List<Song> = Gson().fromJson(json, type)
            songs.find { it.songId == songId }
        } catch (e: Exception) {
            null
        }
    }

    override fun onBackClick() {
        view.navigateBack()
    }

    override fun onProductionClick() {
        // TODO: 展开制作信息详情
        // 目前只是占位，后续可以实现弹窗或跳转详情《
    }

    override fun onIntroductionClick() {
        // TODO: 展开完整简《
        // 目前只是占位，后续可以实现弹窗或跳转详情《
    }

    override fun onFilmTvClick() {
        // TODO: 显示影综列表
        // 目前只是占位，后续可以实现弹窗或跳转列表《
    }

    override fun onAwardsClick() {
        // TODO: 显示奖项列表
        // 目前只是占位，后续可以实现弹窗或跳转列表《
    }

    override fun onScoresClick() {
        // TODO: 显示乐谱列表
        // 目前只是占位，后续可以实现弹窗或跳转列表《
    }

    override fun onDestroy() {
        job.cancel()
    }
}
