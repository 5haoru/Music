package com.example.mymusic.presenter

import android.content.Context
import com.example.mymusic.utils.DataLoader
import kotlin.random.Random

/**
 * 排行榜页面的 Presenter
 */
class RankListPresenter(
    private val view: RankListContract.View,
    private val context: Context
) : RankListContract.Presenter {

    override fun loadRankData() {
        view.showLoading()
        try {
            // 加载所有歌曲
            val allSongs = DataLoader.loadSongs(context)

            // 创建特色榜单推荐
            val featuredRanks = listOf(
                RankListContract.FeaturedRank(
                    id = "featured_1",
                    name = "硬地原创\n音乐榜",
                    gradientColors = listOf(0xFFFF6B9D, 0xFFFF9A9E)
                ),
                RankListContract.FeaturedRank(
                    id = "featured_2",
                    name = "潮流风向榜",
                    gradientColors = listOf(0xFFFD5E8E, 0xFFFF9AA2)
                ),
                RankListContract.FeaturedRank(
                    id = "featured_3",
                    name = "国风榜",
                    gradientColors = listOf(0xFFFFB74D, 0xFFFFD54F)
                )
            )

            // 创建官方榜单列表
            val officialRanks = listOf(
                createRankData(
                    "rank_soaring",
                    "飙升榜",
                    "每天更新",
                    "cover/1.png",
                    allSongs.subList(0, 3)
                ),
                createRankData(
                    "rank_new",
                    "新歌榜",
                    "每天更新",
                    "cover/2.png",
                    allSongs.subList(3, 6)
                ),
                createRankData(
                    "rank_hot",
                    "热歌榜",
                    "每天更新",
                    "cover/3.png",
                    allSongs.subList(6, 9)
                )
            )

            view.showFeaturedRanks(featuredRanks)
            view.showOfficialRanks(officialRanks)
            view.hideLoading()
        } catch (e: Exception) {
            view.showError("加载榜单失败: ${e.message}")
        }
    }

    /**
     * 创建榜单数据
     */
    private fun createRankData(
        rankId: String,
        rankName: String,
        updateFrequency: String,
        coverUrl: String,
        songs: List<com.example.mymusic.data.Song>
    ): RankListContract.RankData {
        val topSongs = songs.take(3).mapIndexed { index, song ->
            RankListContract.TopSongItem(
                rank = index + 1,
                song = song,
                status = when (Random.nextInt(4)) {
                    0 -> RankListContract.SongStatus.NEW
                    1 -> RankListContract.SongStatus.UP
                    2 -> RankListContract.SongStatus.DOWN
                    else -> RankListContract.SongStatus.STABLE
                }
            )
        }

        return RankListContract.RankData(
            rankId = rankId,
            rankName = rankName,
            updateFrequency = updateFrequency,
            coverUrl = coverUrl,
            topSongs = topSongs
        )
    }

    override fun onRankClick(rankId: String) {
        view.navigateToRankDetail(rankId)
    }

    override fun onSongClick(songId: String) {
        // TODO: 播放歌曲
    }

    override fun onDestroy() {
        // 清理资源
    }
}
