package com.example.mymusic.data

/**
 * 自动化测试数据模型
 * 与Python测试脚本的JSON结构保持一致
 */

/**
 * 页面导航记录
 */
data class NavigationRecord(
    val page: String,
    val timestamp: String
)

/**
 * App状态
 */
data class AppState(
    val currentPage: String,
    val navigationHistory: List<NavigationRecord> = emptyList(),
    val currentSongId: String? = null,  // 用于歌曲详情页面
    val currentPlaylistId: String? = null,  // 用于歌单详情页面
    val currentAlbumId: String? = null,  // 用于专辑详情页面
    val showLyrics: Boolean = false,     // 是否显示歌词
    val lastUpdated: String
)

/**
 * 当前播放歌曲信息
 */
data class CurrentSongInfo(
    val songId: String,
    val songName: String,
    val artist: String,
    val source: String = "",            // 歌曲来源,如"daily_recommend", "rank"等
    val sourceDetail: String = ""       // 详细来源,如"第3首"
)

/**
 * 播放历史记录
 */
data class PlaybackHistoryRecord(
    val songId: String,
    val timestamp: String,
    val action: String  // "play", "pause", "stop"
)

/**
 * 播放状态
 */
data class PlaybackState(
    val currentSong: CurrentSongInfo?,
    val isPlaying: Boolean,
    val playbackMode: String,  // "sequential", "single_loop", "shuffle"
    val volume: Int,
    val progress: Int = 0,     // 播放进度(秒)
    val duration: Int = 0,     // 总时长(秒)
    val playbackHistory: List<PlaybackHistoryRecord> = emptyList(),
    val lastUpdated: String
)

/**
 * 收藏的歌曲
 */
data class FavoriteSongRecord(
    val songId: String,
    val songName: String,
    val artist: String,
    val addedTime: String
)

/**
 * 用户收藏
 */
data class UserFavorites(
    val favoriteSongs: List<FavoriteSongRecord>,
    val recentUnfavorited: String? = null,  // 最近取消收藏的歌曲ID
    val lastUpdated: String
)

/**
 * 歌单信息
 */
data class PlaylistRecord(
    val playlistId: String,
    val playlistName: String,
    val songIds: List<String>,
    val songCount: Int,
    val createTime: Long,  // 改为Long类型的时间戳，字段名改为createTime
    val sortOrder: String = "default"  // "default", "time_desc", "time_asc", "name_asc", "name_desc"
)

/**
 * 用户歌单
 */
data class UserPlaylists(
    val playlists: List<PlaylistRecord>,
    val currentViewingPlaylist: String? = null,
    val lastUpdated: String
)

/**
 * 收藏的歌单
 */
data class CollectedPlaylistRecord(
    val playlistId: String,
    val playlistName: String,
    val collectedTime: String
)

/**
 * 收藏的专辑
 */
data class CollectedAlbumRecord(
    val albumId: String,
    val albumName: String,
    val artist: String,
    val artistId: String,
    val collectedTime: String
)

/**
 * 收藏项
 */
data class CollectedItems(
    val collectedPlaylists: List<CollectedPlaylistRecord>,
    val collectedAlbums: List<CollectedAlbumRecord>,
    val lastUpdated: String
)

/**
 * 关注的歌手
 */
data class FollowedArtistRecord(
    val artistId: String,
    val artistName: String,
    val followedTime: String
)

/**
 * 关注的歌手列表
 */
data class FollowedArtists(
    val followedArtists: List<FollowedArtistRecord>,
    val recentlyUnfollowed: String? = null,  // 最近取消关注的项目ID
    val lastUpdated: String
)

/**
 * 搜索记录
 */
data class SearchRecord(
    val searchId: String,
    val query: String,
    val timestamp: String,
    val resultType: String,  // "song", "artist", "album", "playlist"
    val resultId: String,
    val action: String       // "view", "play"
)

/**
 * 搜索历史
 */
data class SearchHistory(
    val searches: List<SearchRecord>,
    val lastUpdated: String
)

/**
 * 评论记录（自动化测试用）
 */
data class AutoTestCommentRecord(
    val commentId: String,
    val songId: String,
    val content: String,
    val timestamp: String
)

/**
 * 用户评论
 */
data class Comments(
    val userComments: List<AutoTestCommentRecord>,
    val lastUpdated: String
)

/**
 * 播放器样式（自动化测试用）
 */
data class AutoTestPlayerStyleRecord(
    val styleId: String,
    val styleName: String,
    val changedTime: String
)

/**
 * 漫游模式
 */
data class StrollModeRecord(
    val isActive: Boolean,
    val scene: String,  // "伤感", "快乐", "伪感"等
    val activatedTime: String
)

/**
 * 播放器设置
 */
data class PlayerSettings(
    val playerStyle: AutoTestPlayerStyleRecord?,
    val strollMode: StrollModeRecord?,
    val lastUpdated: String
)

/**
 * 周统计
 */
data class WeeklyStatsRecord(
    val totalMinutes: Int,
    val weekStartDate: String,
    val weekEndDate: String
)

/**
 * 月统计
 */
data class MonthlyStatsRecord(
    val totalMinutes: Int,
    val month: String,
    val year: Int
)

/**
 * 查看统计记录
 */
data class ViewedStatsRecord(
    val weekly: Boolean = false,
    val monthly: Boolean = false
)

/**
 * 听歌统计
 */
data class ListeningStats(
    val weeklyStats: WeeklyStatsRecord?,
    val monthlyStats: MonthlyStatsRecord?,
    val viewedStats: ViewedStatsRecord,
    val lastUpdated: String
)

/**
 * MV信息
 */
data class CurrentMVInfo(
    val mvId: String,
    val songId: String,
    val songName: String,
    val artist: String,
    val isPlaying: Boolean
)

/**
 * MV播放历史
 */
data class MVHistoryRecord(
    val mvId: String,
    val timestamp: String,
    val action: String  // "play", "pause"
)

/**
 * MV播放状态
 */
data class MVPlayback(
    val currentMV: CurrentMVInfo?,
    val mvHistory: List<MVHistoryRecord>,
    val lastUpdated: String
)

/**
 * 任务详情
 */
data class TaskDetail(
    val currentPage: String? = null,
    val songId: String? = null,
    val playlistId: String? = null,
    val albumId: String? = null
)

/**
 * 任务记录
 */
data class TaskRecord(
    val taskId: String,
    val taskName: String,
    val status: String,  // "TASK_PENDING", "TASK_COMPLETED", "TASK_FAILED"
    val completedTime: String,
    val detail: TaskDetail? = null
)

/**
 * 任务日志
 */
data class TaskLogs(
    val tasks: List<TaskRecord>,
    val lastUpdated: String
)
