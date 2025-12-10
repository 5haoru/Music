package com.example.mymusic.presentation.rank

import android.content.Context
import com.example.mymusic.data.repository.PlaylistRepository
import com.example.mymusic.data.repository.SongRepository
import com.example.mymusic.utils.AutoTestHelper
import kotlin.random.Random

/**
 * 榜单详情页面的Presenter
 */
class RankPresenter(
    private val view: RankContract.View,
    private val songRepository: SongRepository,
    private val playlistRepository: PlaylistRepository
) : RankContract.Presenter {

    private var currentRankDetail: RankContract.RankDetail? = null
    private var isLiked = false

    override fun loadRankDetail(rankId: String) {
        view.showLoading()
        try {
            // 加载所有歌曲和收藏列表
            val allSongs = songRepository.getAllSongs()
            val favoritesPlaylist = playlistRepository.getAllPlaylists().find { it.playlistId == "my_favorites" }
            val favoriteSongIds = favoritesPlaylist?.songIds.orEmpty()

            // 生成100首歌曲
            val rankSongs = (1..100).map { i ->
                val song = allSongs[(i - 1) % allSongs.size]
                val (rankChange, changeValue) = generateRankChange(i)
                RankContract.RankSongItem(
                    rank = i,
                    song = song,
                    isFavorite = favoriteSongIds.contains(song.songId),
                    hasQualityTag = i % 3 == 0,
                    isNew = rankChange == RankContract.RankChange.NEW,
                    rankChange = rankChange,
                    changeValue = changeValue
                )
            }

            // 根据rankId定制榜单信息
            val (rankName, description) = getRankInfo(rankId)

            val rankDetail = RankContract.RankDetail(
                rankId = rankId,
                rankName = rankName,
                playCount = 40000000L,  // 4000万
                officialName = "网易云音乐",
                updateDate = "10月28日更新",
                description = description,
                coverUrl = "cover/3.png",
                shareCount = 474,
                commentCount = 3091,
                likeCount = 116000L,  // 11.6万
                isFollowing = false,
                songs = rankSongs
            )

            currentRankDetail = rankDetail
            view.showRankDetail(rankDetail)
            view.hideLoading()
        } catch (e: Exception) {
            view.hideLoading()
            view.showError("加载榜单失败: ${e.message}")
        }
    }

    /**
     * 生成排名变化
     * @return Pair(RankChange, changeValue)
     */
    private fun generateRankChange(rank: Int): Pair<RankContract.RankChange, Int> {
        val random = Random.nextInt(100)
        return when {
            rank <= 5 && random < 10 -> {
                // �?名有10%概率是新上榜
                Pair(RankContract.RankChange.NEW, 0)
            }
            random < 30 -> {
                // 30%概率上升
                Pair(RankContract.RankChange.UP, Random.nextInt(1, 6))
            }
            random < 60 -> {
                // 30%概率下降
                Pair(RankContract.RankChange.DOWN, Random.nextInt(1, 6))
            }
            else -> {
                // 40%概率稳定
                Pair(RankContract.RankChange.STABLE, 0)
            }
        }
    }

    /**
     * 根据rankId获取榜单信息
     */
    private fun getRankInfo(rankId: String): Pair<String, String> {
        return when (rankId) {
            "rank_soaring" -> Pair(
                "飙升榜",
                "网易云用户一周内收听所有歌曲官方飙升TOP排行榜，每天更新"
            )
            "rank_new" -> Pair(
                "新歌榜",
                "网易云用户一周内收听所有新歌官方TOP排行榜，每天更新"
            )
            "rank_hot" -> Pair(
                "热歌榜",
                "网易云用户一周内收听所有热门歌曲官方TOP排行榜，每天更新"
            )
            "featured_1" -> Pair(
                "硬地原创音乐榜",
                "网易云原创音乐排行榜，每周更新"
            )
            "featured_2" -> Pair(
                "潮流风向榜",
                "最新最潮流的音乐排行榜，每周更新"
            )
            "featured_3" -> Pair(
                "国风榜",
                "精选国风音乐排行榜，每周更新"
            )
            else -> Pair(
                "网易云日语榜",
                "网易云用户一周内收听所有日语歌曲官方TOP排行榜，每周更新"
            )
        }
    }

    override fun onFollowClick() {
        currentRankDetail?.let { detail ->
            val newFollowStatus = !detail.isFollowing
            currentRankDetail = detail.copy(isFollowing = newFollowStatus)
            view.updateFollowStatus(newFollowStatus)
            view.showSuccess(if (newFollowStatus) "已关注" else "已取消关注")
        }
    }

    override fun onShareClick() {
        view.showSuccess("分享功能待开发")
    }

    override fun onCommentClick() {
        view.showSuccess("评论功能待开发")
    }

    override fun onLikeClick() {
        currentRankDetail?.let { detail ->
            isLiked = !isLiked
            val newLikeCount = if (isLiked) detail.likeCount + 1 else detail.likeCount - 1
            view.updateLikeStatus(isLiked, newLikeCount)
        }
    }

    override fun onPlayAllClick() {
        currentRankDetail?.let { detail ->
            if (detail.songs.isNotEmpty()) {
                val firstSong = detail.songs[0]
                // 记录播放来源到AutoTestHelper
                AutoTestHelper.updatePlayback(
                    songId = firstSong.song.songId,
                    songName = firstSong.song.songName,
                    artist = firstSong.song.artist,
                    source = "rank_list",
                    sourceDetail = "${detail.rankName} - 播放全部"
                )
                view.navigateToPlay(firstSong.song.songId)
            }
        }
    }

    override fun onSongClick(songId: String) {
        currentRankDetail?.let { detail ->
            // 获取歌曲信息
            val song = detail.songs.find { it.song.songId == songId }
            song?.let {
                // 记录播放来源到AutoTestHelper
                AutoTestHelper.updatePlayback(
                    songId = it.song.songId,
                    songName = it.song.songName,
                    artist = it.song.artist,
                    source = "rank_list",
                    sourceDetail = "${detail.rankName} - 第${it.rank}名"
                )
            }
        }
        view.navigateToPlay(songId)
    }

    override fun onFavoriteClick(songId: String) {
        try {
            val favoritesPlaylist = playlistRepository.getAllPlaylists().find { it.playlistId == "my_favorites" }
            val isFavorite = favoritesPlaylist?.songIds?.contains(songId) == true

            if (isFavorite) {
                view.showSuccess("已在我喜欢的音乐中")
            } else {
                val success = playlistRepository.addSongToPlaylist("my_favorites", songId)
                if (success) {
                    view.updateSongFavoriteStatus(songId, true)
                    view.showSuccess("成功收藏")
                } else {
                    view.showError("收藏失败")
                }
            }
        } catch (e: Exception) {
            view.showError("操作失败: ${e.message}")
        }
    }

    override fun onSongMoreClick(songId: String) {
        view.showSuccess("更多功能待开发")
    }

    override fun onDestroy() {
        // 清理资源
    }
}
