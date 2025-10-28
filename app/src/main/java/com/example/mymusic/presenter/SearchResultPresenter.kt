package com.example.mymusic.presenter

import android.content.Context
import com.example.mymusic.data.Artist
import com.example.mymusic.data.MusicVideo
import com.example.mymusic.data.Song
import com.example.mymusic.data.SongDetail
import com.example.mymusic.utils.DataLoader
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * 搜索结果页面Presenter
 */
class SearchResultPresenter(
    private val view: SearchResultContract.View,
    private val context: Context
) : SearchResultContract.Presenter {

    private var allSongs: List<Song> = emptyList()
    private var allArtists: List<Artist> = emptyList()
    private var allMVs: List<MusicVideo> = emptyList()
    private var isFollowing = false

    override fun loadSearchResults(query: String) {
        view.showLoading()
        try {
            // 加载所有数据
            allSongs = DataLoader.loadSongs(context)
            allArtists = DataLoader.loadArtists(context)
            allMVs = loadMusicVideos()

            // 搜索匹配的歌手
            val matchedArtist = allArtists.find { artist ->
                artist.artistName.contains(query, ignoreCase = true) ||
                allSongs.any { song ->
                    song.songName.contains(query, ignoreCase = true) && song.artist == artist.artistName
                }
            }
            view.showArtist(matchedArtist)

            // 搜索匹配的MV
            val matchedMV = allMVs.find { mv ->
                mv.title.contains(query, ignoreCase = true)
            }
            view.showMusicVideo(matchedMV)

            // 搜索匹配的歌曲并转换为SongDetail
            val matchedSongs = allSongs.filter { song ->
                song.songName.contains(query, ignoreCase = true)
            }

            // 为歌曲添加丰富的展示信息
            val songDetails = matchedSongs.mapIndexed { index, song ->
                when (index) {
                    0 -> SongDetail.fromSong(
                        song,
                        version = null,
                        qualityTags = listOf("超清母带"),
                        permissionTags = listOf("绅士"),
                        commentCount = "十万评论",
                        hotComment = "我的爱如台风过境，身处台风中心的你一无所知",
                        collectionInfo = "最近爱听 · 万人收藏"
                    )
                    1 -> SongDetail.fromSong(
                        song,
                        version = "（翻自：${song.artist}）",
                        qualityTags = listOf("超清母带"),
                        permissionTags = emptyList(),
                        commentCount = "万人收藏",
                        collectionInfo = "悲伤时都在听"
                    )
                    else -> SongDetail.fromSong(
                        song,
                        version = if (index % 2 == 0) "(Live)" else null,
                        qualityTags = if (index % 3 == 0) listOf("超清母带") else emptyList(),
                        permissionTags = if (index % 2 == 0) listOf("VIP") else emptyList()
                    )
                }
            }

            view.showSongResults(songDetails)
            view.hideLoading()
        } catch (e: Exception) {
            view.hideLoading()
            view.showError("加载搜索结果失败: ${e.message}")
        }
    }

    override fun onCategorySelected(category: String) {
        // TODO: 实现分类筛选功能
    }

    override fun onFollowArtist(artistId: String) {
        isFollowing = !isFollowing
        view.updateFollowStatus(isFollowing)
        // TODO: 保存关注状态到JSON
    }

    override fun onMVClick(mvId: String) {
        // TODO: 播放MV
    }

    override fun onSongClick(songId: String) {
        // TODO: 播放歌曲
    }

    override fun onBackClick() {
        view.navigateBack()
    }

    private fun loadMusicVideos(): List<MusicVideo> {
        return try {
            val json = context.assets.open("data/music_videos.json").bufferedReader().use { it.readText() }
            val type = object : TypeToken<List<MusicVideo>>() {}.type
            Gson().fromJson(json, type)
        } catch (e: Exception) {
            emptyList()
        }
    }
}
