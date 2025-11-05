package com.example.mymusic.presenter

import android.content.Context
import com.example.mymusic.data.Song
import com.example.mymusic.utils.AutoTestHelper
import com.example.mymusic.utils.DataLoader

/**
 * 每日推荐页面的 Presenter
 */
class DailyRecommendPresenter(
    private val view: DailyRecommendContract.View,
    private val context: Context
) : DailyRecommendContract.Presenter {

    private var allSongs: List<Song> = emptyList()
    private var dailyRecommendedSongs: List<Song> = emptyList()

    override fun loadDailyRecommendData() {
        view.showLoading()
        try {
            // 加载所有歌曲
            allSongs = DataLoader.loadSongs(context)

            // 选择前10首作为每日推荐（实际应用中可以基于日期生成不同的推荐）
            dailyRecommendedSongs = allSongs.take(10)

            view.showDailyRecommendedSongs(dailyRecommendedSongs)
            view.hideLoading()
        } catch (e: Exception) {
            view.showError("加载数据失败: ${e.message}")
        }
    }

    override fun onSongClick(songId: String, songIndex: Int) {
        // 获取歌曲信息并记录到AutoTestHelper
        val song = dailyRecommendedSongs.find { it.songId == songId }
        song?.let {
            AutoTestHelper.updatePlayback(
                songId = it.songId,
                songName = it.songName,
                artist = it.artist,
                source = "daily_recommend",
                sourceDetail = "第${songIndex}首"
            )
        }
        view.navigateToPlay(songId)
    }

    override fun onPlayAllClick() {
        if (dailyRecommendedSongs.isNotEmpty()) {
            view.playAllSongs(dailyRecommendedSongs)
        }
    }

    override fun onHistoryClick() {
        // TODO: 打开历史日推页面
    }

    override fun onTodayFortuneClick() {
        // TODO: 打开今日运势页面
    }

    override fun onDestroy() {
        // 清理资源
    }
}
