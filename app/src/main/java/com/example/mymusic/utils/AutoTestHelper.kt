package com.example.mymusic.utils

import android.content.Context
import android.util.Log
import com.example.mymusic.data.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * 自动化测试辅助工具类
 * 负责记录App运行状态到JSON文件,供Python测试脚本读取验证
 */
object AutoTestHelper {

    private const val TAG = "AutoTestHelper"
    private lateinit var autotestDir: File
    private val gson: Gson = GsonBuilder().setPrettyPrinting().create()
    private var isEnabled = true  // 可以通过BuildConfig控制是否启用

    /**
     * 初始化自动化测试目录
     */
    fun init(context: Context) {
        try {
            autotestDir = File(context.filesDir, "autotest")
            if (!autotestDir.exists()) {
                autotestDir.mkdirs()
                Log.d(TAG, "创建autotest目录: ${autotestDir.absolutePath}")
            }

            // 初始化默认文件
            initDefaultFiles()
        } catch (e: Exception) {
            Log.e(TAG, "初始化失败", e)
        }
    }

    /**
     * 初始化默认JSON文件
     */
    private fun initDefaultFiles() {
        try {
            // app_state.json
            if (!getFile("app_state.json").exists()) {
                val defaultAppState = AppState(
                    currentPage = "unknown",
                    navigationHistory = emptyList(),
                    lastUpdated = getCurrentTimestamp()
                )
                saveAppState(defaultAppState)
            }

            // playback_state.json
            if (!getFile("playback_state.json").exists()) {
                val defaultPlaybackState = PlaybackState(
                    currentSong = null,
                    isPlaying = false,
                    playbackMode = "sequential",
                    volume = 50,
                    playbackHistory = emptyList(),
                    lastUpdated = getCurrentTimestamp()
                )
                savePlaybackState(defaultPlaybackState)
            }

            // user_favorites.json
            if (!getFile("user_favorites.json").exists()) {
                val defaultFavorites = UserFavorites(
                    favoriteSongs = emptyList(),
                    lastUpdated = getCurrentTimestamp()
                )
                saveUserFavorites(defaultFavorites)
            }

            // user_playlists.json
            if (!getFile("user_playlists.json").exists()) {
                val defaultPlaylists = UserPlaylists(
                    playlists = emptyList(),
                    currentViewingPlaylist = null,
                    lastUpdated = getCurrentTimestamp()
                )
                saveUserPlaylists(defaultPlaylists)
            }

            // collected_items.json
            if (!getFile("collected_items.json").exists()) {
                val defaultCollected = CollectedItems(
                    collectedPlaylists = emptyList(),
                    collectedAlbums = emptyList(),
                    lastUpdated = getCurrentTimestamp()
                )
                saveCollectedItems(defaultCollected)
            }

            // followed_artists.json
            if (!getFile("followed_artists.json").exists()) {
                val defaultFollowed = FollowedArtists(
                    followedArtists = emptyList(),
                    lastUpdated = getCurrentTimestamp()
                )
                saveFollowedArtists(defaultFollowed)
            }

            // search_history.json
            if (!getFile("search_history.json").exists()) {
                val defaultSearch = SearchHistory(
                    searches = emptyList(),
                    lastUpdated = getCurrentTimestamp()
                )
                saveSearchHistory(defaultSearch)
            }

            // comments.json
            if (!getFile("comments.json").exists()) {
                val defaultComments = Comments(
                    userComments = emptyList(),
                    lastUpdated = getCurrentTimestamp()
                )
                saveComments(defaultComments)
            }

            // player_settings.json
            if (!getFile("player_settings.json").exists()) {
                val defaultSettings = PlayerSettings(
                    playerStyle = null,
                    strollMode = null,
                    lastUpdated = getCurrentTimestamp()
                )
                savePlayerSettings(defaultSettings)
            }

            // listening_stats.json
            if (!getFile("listening_stats.json").exists()) {
                val defaultStats = ListeningStats(
                    weeklyStats = null,
                    monthlyStats = null,
                    viewedStats = ViewedStatsRecord(),
                    lastUpdated = getCurrentTimestamp()
                )
                saveListeningStats(defaultStats)
            }

            // mv_playback.json
            if (!getFile("mv_playback.json").exists()) {
                val defaultMV = MVPlayback(
                    currentMV = null,
                    mvHistory = emptyList(),
                    lastUpdated = getCurrentTimestamp()
                )
                saveMVPlayback(defaultMV)
            }

            // task_logs.json
            if (!getFile("task_logs.json").exists()) {
                val defaultLogs = TaskLogs(
                    tasks = emptyList(),
                    lastUpdated = getCurrentTimestamp()
                )
                saveTaskLogs(defaultLogs)
            }

            Log.d(TAG, "默认文件初始化完成")
        } catch (e: Exception) {
            Log.e(TAG, "初始化默认文件失败", e)
        }
    }

    /**
     * 获取文件对象
     */
    private fun getFile(filename: String): File {
        return File(autotestDir, filename)
    }

    /**
     * 获取当前时间戳
     */
    private fun getCurrentTimestamp(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }

    // ==================== 通用读写方法 ====================

    private inline fun <reified T> readJson(filename: String, default: T): T {
        if (!isEnabled) return default
        return try {
            val file = getFile(filename)
            if (file.exists()) {
                val json = file.readText()
                gson.fromJson(json, T::class.java) ?: default
            } else {
                default
            }
        } catch (e: Exception) {
            Log.e(TAG, "读取文件失败: $filename", e)
            default
        }
    }

    private fun <T> writeJson(filename: String, data: T) {
        if (!isEnabled) return
        try {
            val file = getFile(filename)
            val json = gson.toJson(data)
            file.writeText(json)
            Log.d(TAG, "写入文件成功: $filename")
        } catch (e: Exception) {
            Log.e(TAG, "写入文件失败: $filename", e)
        }
    }

    // ==================== App状态 ====================

    fun getAppState(): AppState {
        return readJson("app_state.json", AppState("unknown", emptyList(), null, false, getCurrentTimestamp()))
    }

    fun saveAppState(state: AppState) {
        writeJson("app_state.json", state)
    }

    fun updateCurrentPage(page: String) {
        val state = getAppState()
        val newHistory = state.navigationHistory.toMutableList()
        newHistory.add(NavigationRecord(page, getCurrentTimestamp()))
        val newState = state.copy(
            currentPage = page,
            navigationHistory = newHistory,
            lastUpdated = getCurrentTimestamp()
        )
        saveAppState(newState)
    }

    fun updateShowLyrics(show: Boolean) {
        val state = getAppState()
        saveAppState(state.copy(showLyrics = show, lastUpdated = getCurrentTimestamp()))
    }

    fun updateCurrentSongId(songId: String?) {
        val state = getAppState()
        saveAppState(state.copy(currentSongId = songId, lastUpdated = getCurrentTimestamp()))
    }

    // ==================== 播放状态 ====================

    fun getPlaybackState(): PlaybackState {
        return readJson("playback_state.json", PlaybackState(null, false, "sequential", 50, lastUpdated = getCurrentTimestamp()))
    }

    fun savePlaybackState(state: PlaybackState) {
        writeJson("playback_state.json", state)
    }

    fun updatePlayback(
        songId: String? = null,
        songName: String? = null,
        artist: String? = null,
        isPlaying: Boolean? = null,
        playbackMode: String? = null,
        volume: Int? = null,
        source: String? = null,
        sourceDetail: String? = null
    ) {
        val state = getPlaybackState()
        val newSong = if (songId != null && songName != null && artist != null) {
            CurrentSongInfo(songId, songName, artist, source ?: "", sourceDetail ?: "")
        } else {
            state.currentSong
        }

        val newState = state.copy(
            currentSong = newSong,
            isPlaying = isPlaying ?: state.isPlaying,
            playbackMode = playbackMode ?: state.playbackMode,
            volume = volume ?: state.volume,
            lastUpdated = getCurrentTimestamp()
        )
        savePlaybackState(newState)
    }

    fun addPlaybackHistory(songId: String, action: String) {
        val state = getPlaybackState()
        val newHistory = state.playbackHistory.toMutableList()
        newHistory.add(PlaybackHistoryRecord(songId, getCurrentTimestamp(), action))
        savePlaybackState(state.copy(playbackHistory = newHistory, lastUpdated = getCurrentTimestamp()))
    }

    // ==================== 用户收藏 ====================

    fun getUserFavorites(): UserFavorites {
        return readJson("user_favorites.json", UserFavorites(emptyList(), getCurrentTimestamp()))
    }

    fun saveUserFavorites(favorites: UserFavorites) {
        writeJson("user_favorites.json", favorites)
    }

    fun addFavoriteSong(songId: String, songName: String, artist: String) {
        val favorites = getUserFavorites()
        val songs = favorites.favoriteSongs.toMutableList()

        // 检查是否已收藏
        if (songs.none { it.songId == songId }) {
            songs.add(FavoriteSongRecord(songId, songName, artist, getCurrentTimestamp()))
            saveUserFavorites(UserFavorites(songs, getCurrentTimestamp()))
        }
    }

    fun removeFavoriteSong(songId: String) {
        val favorites = getUserFavorites()
        val songs = favorites.favoriteSongs.filter { it.songId != songId }
        saveUserFavorites(UserFavorites(songs, getCurrentTimestamp()))
    }

    // ==================== 用户歌单 ====================

    fun getUserPlaylists(): UserPlaylists {
        return readJson("user_playlists.json", UserPlaylists(emptyList(), null, getCurrentTimestamp()))
    }

    fun saveUserPlaylists(playlists: UserPlaylists) {
        writeJson("user_playlists.json", playlists)
    }

    fun addPlaylist(playlistId: String, playlistName: String, songIds: List<String>) {
        val playlists = getUserPlaylists()
        val list = playlists.playlists.toMutableList()
        list.add(PlaylistRecord(playlistId, playlistName, songIds, songIds.size, getCurrentTimestamp()))
        saveUserPlaylists(UserPlaylists(list, playlists.currentViewingPlaylist, getCurrentTimestamp()))
    }

    fun updateCurrentViewingPlaylist(playlistId: String?) {
        val playlists = getUserPlaylists()
        saveUserPlaylists(playlists.copy(currentViewingPlaylist = playlistId, lastUpdated = getCurrentTimestamp()))
    }

    fun updatePlaylistSortOrder(playlistId: String, sortOrder: String) {
        val playlists = getUserPlaylists()
        val list = playlists.playlists.map {
            if (it.playlistId == playlistId) {
                it.copy(sortOrder = sortOrder)
            } else {
                it
            }
        }
        saveUserPlaylists(playlists.copy(playlists = list, lastUpdated = getCurrentTimestamp()))
    }

    // ==================== 收藏项 ====================

    fun getCollectedItems(): CollectedItems {
        return readJson("collected_items.json", CollectedItems(emptyList(), emptyList(), getCurrentTimestamp()))
    }

    fun saveCollectedItems(items: CollectedItems) {
        writeJson("collected_items.json", items)
    }

    fun addCollectedPlaylist(playlistId: String, playlistName: String) {
        val items = getCollectedItems()
        val playlists = items.collectedPlaylists.toMutableList()
        if (playlists.none { it.playlistId == playlistId }) {
            playlists.add(CollectedPlaylistRecord(playlistId, playlistName, getCurrentTimestamp()))
            saveCollectedItems(items.copy(collectedPlaylists = playlists, lastUpdated = getCurrentTimestamp()))
        }
    }

    fun addCollectedAlbum(albumId: String, albumName: String, artist: String, artistId: String) {
        val items = getCollectedItems()
        val albums = items.collectedAlbums.toMutableList()
        if (albums.none { it.albumId == albumId }) {
            albums.add(CollectedAlbumRecord(albumId, albumName, artist, artistId, getCurrentTimestamp()))
            saveCollectedItems(items.copy(collectedAlbums = albums, lastUpdated = getCurrentTimestamp()))
        }
    }

    // ==================== 关注歌手 ====================

    fun getFollowedArtists(): FollowedArtists {
        return readJson("followed_artists.json", FollowedArtists(emptyList(), getCurrentTimestamp()))
    }

    fun saveFollowedArtists(artists: FollowedArtists) {
        writeJson("followed_artists.json", artists)
    }

    fun addFollowedArtist(artistId: String, artistName: String) {
        val followed = getFollowedArtists()
        val artists = followed.followedArtists.toMutableList()
        if (artists.none { it.artistId == artistId }) {
            artists.add(FollowedArtistRecord(artistId, artistName, getCurrentTimestamp()))
            saveFollowedArtists(FollowedArtists(artists, getCurrentTimestamp()))
        }
    }

    fun removeFollowedArtist(artistId: String) {
        val followed = getFollowedArtists()
        val artists = followed.followedArtists.filter { it.artistId != artistId }
        saveFollowedArtists(FollowedArtists(artists, getCurrentTimestamp()))
    }

    // ==================== 搜索历史 ====================

    fun getSearchHistory(): SearchHistory {
        return readJson("search_history.json", SearchHistory(emptyList(), getCurrentTimestamp()))
    }

    fun saveSearchHistory(history: SearchHistory) {
        writeJson("search_history.json", history)
    }

    fun addSearchRecord(query: String, resultType: String, resultId: String, action: String) {
        val history = getSearchHistory()
        val searches = history.searches.toMutableList()
        val searchId = "search_${System.currentTimeMillis()}"
        searches.add(SearchRecord(searchId, query, getCurrentTimestamp(), resultType, resultId, action))
        saveSearchHistory(SearchHistory(searches, getCurrentTimestamp()))
    }

    // ==================== 评论 ====================

    fun getComments(): Comments {
        return readJson("comments.json", Comments(emptyList(), getCurrentTimestamp()))
    }

    fun saveComments(comments: Comments) {
        writeJson("comments.json", comments)
    }

    fun addComment(songId: String, content: String) {
        val comments = getComments()
        val list = comments.userComments.toMutableList()
        val commentId = "comment_${System.currentTimeMillis()}"
        list.add(AutoTestCommentRecord(commentId, songId, content, getCurrentTimestamp()))
        saveComments(Comments(list, getCurrentTimestamp()))
    }

    // ==================== 播放器设置 ====================

    fun getPlayerSettings(): PlayerSettings {
        return readJson("player_settings.json", PlayerSettings(null, null, getCurrentTimestamp()))
    }

    fun savePlayerSettings(settings: PlayerSettings) {
        writeJson("player_settings.json", settings)
    }

    fun updatePlayerStyle(styleId: String, styleName: String) {
        val settings = getPlayerSettings()
        val style = AutoTestPlayerStyleRecord(styleId, styleName, getCurrentTimestamp())
        savePlayerSettings(settings.copy(playerStyle = style, lastUpdated = getCurrentTimestamp()))
    }

    fun updateStrollMode(scene: String, isActive: Boolean = true) {
        val settings = getPlayerSettings()
        val mode = StrollModeRecord(isActive, scene, getCurrentTimestamp())
        savePlayerSettings(settings.copy(strollMode = mode, lastUpdated = getCurrentTimestamp()))
    }

    // ==================== 听歌统计 ====================

    fun getListeningStats(): ListeningStats {
        return readJson("listening_stats.json", ListeningStats(null, null, ViewedStatsRecord(), getCurrentTimestamp()))
    }

    fun saveListeningStats(stats: ListeningStats) {
        writeJson("listening_stats.json", stats)
    }

    fun updateViewedStats(statsType: String) {
        val stats = getListeningStats()
        val viewed = when (statsType) {
            "weekly" -> stats.viewedStats.copy(weekly = true)
            "monthly" -> stats.viewedStats.copy(monthly = true)
            else -> stats.viewedStats
        }
        saveListeningStats(stats.copy(viewedStats = viewed, lastUpdated = getCurrentTimestamp()))
    }

    // ==================== MV播放 ====================

    fun getMVPlayback(): MVPlayback {
        return readJson("mv_playback.json", MVPlayback(null, emptyList(), getCurrentTimestamp()))
    }

    fun saveMVPlayback(playback: MVPlayback) {
        writeJson("mv_playback.json", playback)
    }

    fun updateMVPlayback(mvId: String, songId: String, songName: String, artist: String, isPlaying: Boolean) {
        val playback = getMVPlayback()
        val mv = CurrentMVInfo(mvId, songId, songName, artist, isPlaying)
        saveMVPlayback(playback.copy(currentMV = mv, lastUpdated = getCurrentTimestamp()))
    }

    // ==================== 任务日志 ====================

    fun getTaskLogs(): TaskLogs {
        return readJson("task_logs.json", TaskLogs(emptyList(), getCurrentTimestamp()))
    }

    fun saveTaskLogs(logs: TaskLogs) {
        writeJson("task_logs.json", logs)
    }

    fun logTaskCompleted(taskId: String, taskName: String, detail: TaskDetail? = null) {
        val logs = getTaskLogs()
        val tasks = logs.tasks.toMutableList()
        tasks.add(TaskRecord(taskId, taskName, "TASK_COMPLETED", getCurrentTimestamp(), detail))
        saveTaskLogs(TaskLogs(tasks, getCurrentTimestamp()))
    }
}
