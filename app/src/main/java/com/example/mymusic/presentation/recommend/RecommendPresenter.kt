package com.example.mymusic.presentation.recommend

import android.content.Context
import com.example.mymusic.data.Playlist
import com.example.mymusic.data.Song
import com.example.mymusic.utils.DataLoader

/**
 * 推荐页面Presenter
 */
class RecommendPresenter(
    private val view: RecommendContract.View,
    private val context: Context
) : RecommendContract.Presenter {

    private var songs: List<Song> = emptyList()
    private var playlists: List<Playlist> = emptyList()

    override fun loadData() {
        view.showLoading()
        try {
            songs = DataLoader.loadSongs(context)
            playlists = DataLoader.loadPlaylists(context)

            // 过滤“我喜欢的音乐”，它只在“我的”页面显示
            val displayPlaylists = playlists.filter { it.playlistId != "my_favorites" }

            val rankingPlaylistNames = listOf("ACG榜", "日语榜", "国风榜")
            val rankingPlaylists = displayPlaylists.filter { it.playlistName in rankingPlaylistNames }

            // 其他歌单作为精选歌单 (这里简单取非排行榜的前3个作为示例)
            val featuredPlaylists = displayPlaylists.filter { it.playlistName !in rankingPlaylistNames }.take(3)


            view.hideLoading()
            view.showRecommendedSongs(songs.take(5))
            view.showDailyRecommendPlaylists(displayPlaylists)
            view.showFeaturedPlaylists(featuredPlaylists)
            view.showRankingPlaylists(rankingPlaylists)
        } catch (e: Exception) {
            view.hideLoading()
            view.showError("加载数据失败: ${e.message}")
        }
    }

    override fun onSongClick(songId: String) {
        val song = songs.find { it.songId == songId }
        song?.let {
            view.playSong(it)
        }
    }

    override fun onPlaylistClick(playlist: Playlist) {
        // 判断是否是排行榜
        val rankingPlaylistMap = mapOf(
            "playlist_009" to "rank_guofeng",
            "playlist_010" to "rank_acg",
            "playlist_011" to "rank_japanese"
        )

        val rankId = rankingPlaylistMap[playlist.playlistId]
        if (rankId != null) {
            // 是排行榜，打开排行榜详情页
            view.openRank(rankId)
        } else {
            // 是普通歌单，打开歌单详情页
            view.openPlaylist(playlist)
        }
    }

    override fun onSearchClick() {
        view.openSearch()
    }

    override fun onMicClick() {
        view.openListenRecognize()
    }

    override fun onDestroy() {
        // 清理资源
    }
}