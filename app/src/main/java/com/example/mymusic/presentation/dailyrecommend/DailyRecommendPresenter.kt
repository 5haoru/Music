package com.example.mymusic.presentation.dailyrecommend

import com.example.mymusic.data.Song
import com.example.mymusic.data.repository.SongRepository
import com.example.mymusic.utils.AutoTestHelper

/**
 * 每日推荐页面《Presenter
 */
class DailyRecommendPresenter(
    private val view: DailyRecommendContract.View,
    private val songRepository: SongRepository
) : DailyRecommendContract.Presenter {

    private var dailyRecommendedSongs: List<Song> = emptyList()

    override fun loadDailyRecommendData() {
        view.showLoading()
        try {
            val allSongs = songRepository.getAllSongs()
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
                sourceDetail = "daily_recommend_item_${songIndex}"
            )        }
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
