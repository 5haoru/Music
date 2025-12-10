package com.example.mymusic.presentation.singer

import com.example.mymusic.data.Artist
import com.example.mymusic.data.MusicVideo
import com.example.mymusic.data.Song
import com.example.mymusic.data.SongDetail
import com.example.mymusic.data.repository.ArtistFollowRecordRepository
import com.example.mymusic.data.repository.ArtistRepository
import com.example.mymusic.data.repository.MusicVideoRepository
import com.example.mymusic.data.repository.SongRepository
import kotlin.random.Random

/**
 * 歌手详情页面�?Presenter
 */
class SingerPresenter(
    private val view: SingerContract.View,
    private val artistRepository: ArtistRepository,
    private val songRepository: SongRepository,
    private val musicVideoRepository: MusicVideoRepository,
    private val artistFollowRecordRepository: ArtistFollowRecordRepository
) : SingerContract.Presenter {

    private var currentArtist: Artist? = null
    private var isFollowing = false

    override fun loadSingerData(artistId: String) {
        view.showLoading()
        try {
            // 加载歌手信息
            val artists = artistRepository.getAllArtists()
            currentArtist = artists.find { it.artistId == artistId }

            if (currentArtist == null) {
                view.showError("未找到歌手信息")
                return
            }

            view.showSingerInfo(currentArtist!!)

            // 加载该歌手的所有歌曲
            val allSongs = songRepository.getAllSongs()
            val artistSongs = allSongs.filter { song ->
                song.artist == currentArtist!!.artistName
            }

            // 将Song转换为SongDetail，添加丰富的展示信息
            val songDetails = artistSongs.mapIndexed { index, song ->
                generateSongDetail(song, index)
            }

            view.showSongs(songDetails)

            // 加载该歌手的MV列表
            loadMVs(currentArtist!!.artistName, musicVideoRepository)

            view.hideLoading()
        } catch (e: Exception) {
            view.hideLoading()
            view.showError("加载数据失败: ${e.message}")
        }
    }

    /**
     * 加载歌手的MV列表
     */
    private fun loadMVs(artistName: String, musicVideoRepository: MusicVideoRepository) {
        try {
            // 从Repository获取所有MV并按艺术家过滤
            val mvs = musicVideoRepository.getAllMusicVideos().filter { it.artist == artistName }
            view.showMVs(mvs)
        } catch (e: Exception) {
            // MV加载失败不影响主流程，显示空列表
            view.showMVs(emptyList())
        }
    }

    /**
     * 生成丰富的歌曲详情信�?
     */
    private fun generateSongDetail(song: Song, index: Int): SongDetail {
        // 根据索引生成不同的展示信�?
        return when (index) {
            0 -> SongDetail.fromSong(
                song,
                version = "(${song.album})",
                qualityTags = listOf("超清母带"),
                permissionTags = listOf("VIP", "原唱"),
                commentCount = "十万评论",
                hotComment = "热评：开头这几句实在是真的绝...",
                collectionInfo = "曾经听过"
            )
            1 -> SongDetail.fromSong(
                song,
                version = "(动画电影�?{song.album}�?..)",
                qualityTags = listOf("超清母带"),
                permissionTags = listOf("VIP", "原唱"),
                commentCount = null,
                hotComment = "最近爱�?· 万人收藏"
            )
            2 -> SongDetail.fromSong(
                song,
                version = "(电视剧�?{song.album}》主...)",
                qualityTags = listOf("超清母带"),
                permissionTags = listOf("VIP", "原唱"),
                commentCount = null,
                hotComment = "曾经听过 · 悲伤时都在听"
            )
            else -> {
                // 随机生成其他歌曲的展示信�?
                val hasVIP = index % 2 == 0
                val hasQuality = index % 3 == 0
                val commentTexts = listOf(
                    "热评：这首歌真的太好听了...",
                    "曾经听过",
                    "最近爱�?· 万人收藏",
                    "悲伤时都在听 · 十万评论",
                    null
                )

                SongDetail.fromSong(
                    song,
                    version = if (index % 4 == 0) "(${song.album})" else null,
                    qualityTags = if (hasQuality) listOf("超清母带") else emptyList(),
                    permissionTags = if (hasVIP) listOf("VIP", "原唱") else listOf("原唱"),
                    commentCount = if (index % 5 == 0) "万人收藏" else null,
                    hotComment = commentTexts.getOrNull(index % commentTexts.size)
                )
            }
        }
    }

    override fun onFollowClick() {
        currentArtist?.let { artist ->
            isFollowing = !isFollowing
            view.updateFollowStatus(isFollowing)

            val message = if (isFollowing) {
                artistFollowRecordRepository.addFollowRecord(artist.artistId, artist.artistName)
                "已加入${artist.artistName}的乐迷团"
            } else {
                artistFollowRecordRepository.removeFollowRecord(artist.artistId)
                "已退出乐迷团"
            }
            view.showSuccess(message)
        }
    }

    override fun onSongClick(songId: String) {
        view.navigateToPlay(songId)
    }

    override fun onMVClick(mvId: String) {
        view.navigateToMVPlayer(mvId)
    }

    override fun onBackClick() {
        view.navigateBack()
    }

    override fun onDestroy() {
        // 清理资源
    }
}
