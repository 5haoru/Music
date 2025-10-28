package com.example.mymusic.data

/**
 * 歌曲详细信息数据类
 * 用于搜索结果页面显示更丰富的信息
 */
data class SongDetail(
    val songId: String,
    val songName: String,
    val artist: String,
    val album: String,
    val duration: Long,
    val coverUrl: String,
    val lyrics: String,
    val releaseYear: Int,
    // 扩展字段
    val version: String? = null, // 版本说明：Live、翻唱等
    val qualityTags: List<String> = emptyList(), // 音质标识：超清母带、Hi-Res等
    val permissionTags: List<String> = emptyList(), // 权限标识：VIP、原唱等
    val commentCount: String? = null, // 评论数：如"十万评论"
    val hotComment: String? = null, // 热评内容
    val collectionInfo: String? = null // 收藏信息：如"万人收藏 > 悲伤时都在听"
) {
    companion object {
        // 从Song转换为SongDetail
        fun fromSong(song: Song,
                     version: String? = null,
                     qualityTags: List<String> = emptyList(),
                     permissionTags: List<String> = emptyList(),
                     commentCount: String? = null,
                     hotComment: String? = null,
                     collectionInfo: String? = null): SongDetail {
            return SongDetail(
                songId = song.songId,
                songName = song.songName,
                artist = song.artist,
                album = song.album,
                duration = song.duration,
                coverUrl = song.coverUrl,
                lyrics = song.lyrics,
                releaseYear = song.releaseYear,
                version = version,
                qualityTags = qualityTags,
                permissionTags = permissionTags,
                commentCount = commentCount,
                hotComment = hotComment,
                collectionInfo = collectionInfo
            )
        }
    }
}
