package com.example.mymusic.utils

import android.content.Context
import com.example.mymusic.data.Artist
import com.example.mymusic.data.Comment
import com.example.mymusic.data.CommentRecord
import com.example.mymusic.data.FollowItem
import com.example.mymusic.data.ListeningDuration
import com.example.mymusic.data.Playlist
import com.example.mymusic.data.Song
import com.example.mymusic.data.User
import com.example.mymusic.data.model.ShareRecord
import com.example.mymusic.data.model.CollectionRecord
import com.example.mymusic.data.model.DownloadRecord
import com.example.mymusic.data.model.ArtistFollowRecord
import com.example.mymusic.data.model.FanRecord
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileOutputStream

/**
 * 数据加载工具类
 * 用于从assets/data目录加载JSON数据
 */
object DataLoader {
    private val gson = Gson()

    /**
     * 从assets加载JSON文件
     */
    private fun loadJsonFromAssets(context: Context, fileName: String): String {
        return context.assets.open("data/$fileName").bufferedReader().use { it.readText() }
    }

    /**
     * 从内部存储加载JSON文件 (autotest 目录)
     */
    private fun loadJsonFromInternalStorage(context: Context, fileName: String): String {
        val file = File(context.filesDir, "autotest/$fileName")
        return file.readText()
    }

    /**
     * 加载歌曲列表
     */
    fun loadSongs(context: Context): List<Song> {
        val json = loadJsonFromAssets(context, "songs.json")
        val type = object : TypeToken<List<Song>>() {}.type
        return gson.fromJson(json, type)
    }

    /**
     * 根据ID获取歌曲
     */
    fun getSongById(context: Context, songId: String): Song? {
        return loadSongs(context).find { it.songId == songId }
    }

    /**
     * 加载歌单列表
     */
    fun loadPlaylists(context: Context): List<Playlist> {
        val json = loadJsonFromAssets(context, "playlists.json")
        val type = object : TypeToken<List<Playlist>>() {}.type
        return gson.fromJson(json, type)
    }

    /**
     * 根据ID获取歌单
     * 优先从内部存储加载，确保获取最新的用户修改
     */
    fun getPlaylistById(context: Context, playlistId: String): Playlist? {
        return loadPlaylistsWithCache(context).find { it.playlistId == playlistId }
    }

    /**
     * 加载艺术家列表
     */
    fun loadArtists(context: Context): List<Artist> {
        val json = loadJsonFromAssets(context, "artists.json")
        val type = object : TypeToken<List<Artist>>() {}.type
        return gson.fromJson(json, type)
    }

    /**
     * 加载用户列表
     */
    fun loadUsers(context: Context): List<User> {
        val json = loadJsonFromAssets(context, "users.json")
        val type = object : TypeToken<List<User>>() {}.type
        return gson.fromJson(json, type)
    }

    /**
     * 根据歌单ID获取歌单中的歌曲列表
     */
    fun getSongsInPlaylist(context: Context, playlistId: String): List<Song> {
        val playlist = getPlaylistById(context, playlistId) ?: return emptyList()
        val allSongs = loadSongs(context)
        return playlist.songIds.mapNotNull { songId ->
            allSongs.find { it.songId == songId }
        }
    }

    /**
     * 加载评论列表
     */
    fun loadComments(context: Context): List<Comment> {
        val json = loadJsonFromAssets(context, "comments.json")
        val type = object : TypeToken<List<Comment>>() {}.type
        return gson.fromJson(json, type)
    }

    /**
     * 根据歌曲ID获取评论列表
     */
    fun getCommentsBySongId(context: Context, songId: String): List<Comment> {
        return loadComments(context).filter { it.songId == songId }
    }

    /**
     * 保存新评论到JSON文件（写入应用内部存储）
     */
    fun saveComment(context: Context, comment: Comment) {
        try {
            // 获取现有评论
            val existingComments = loadComments(context).toMutableList()
            // 添加新评论
            existingComments.add(comment)
            // 转换为JSON
            val json = gson.toJson(existingComments)

            // 保存到应用内部存储的files目录
            val file = File(context.filesDir, "data/comments.json")
            file.parentFile?.mkdirs()

            FileOutputStream(file).use { output ->
                output.write(json.toByteArray())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 保存评论记录到JSON文件（写入应用内部存储）
     */
    fun saveCommentRecord(context: Context, record: CommentRecord) {
        try {
            // 获取现有记录
            val existingRecords = loadCommentRecords(context).toMutableList()
            // 添加新记录
            existingRecords.add(record)
            // 转换为JSON
            val json = gson.toJson(existingRecords)

            // 保存到应用内部存储的files目录
            val file = File(context.filesDir, "data/comment_records.json")
            file.parentFile?.mkdirs()

            FileOutputStream(file).use { output ->
                output.write(json.toByteArray())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 加载评论记录列表
     */
    fun loadCommentRecords(context: Context): List<CommentRecord> {
        return try {
            val file = File(context.filesDir, "data/comment_records.json")
            if (!file.exists()) {
                // 如果内部存储文件不存在，尝试从assets加载初始数据
                val json = loadJsonFromAssets(context, "comment_records.json")
                val type = object : TypeToken<List<CommentRecord>>() {}.type
                return gson.fromJson(json, type) ?: emptyList()
            }

            // 从内部存储读取
            val json = file.readText()
            val type = object : TypeToken<List<CommentRecord>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    /**
     * 生成唯一评论ID
     */
    fun generateCommentId(context: Context): String {
        val existingComments = loadComments(context)
        val maxId = existingComments.mapNotNull { comment ->
            try {
                comment.commentId.replace("comment_", "").toInt()
            } catch (e: Exception) {
                null
            }
        }.maxOrNull() ?: 0
        return "comment_${String.format("%03d", maxId + 1)}"
    }

    /**
     * 生成唯一记录ID
     */
    fun generateRecordId(context: Context): String {
        val existingRecords = loadCommentRecords(context)
        val maxId = existingRecords.mapNotNull { record ->
            try {
                record.recordId.replace("record_", "").toInt()
            } catch (e: Exception) {
                null
            }
        }.maxOrNull() ?: 0
        return "record_${String.format("%03d", maxId + 1)}"
    }

    /**
     * 保存分享记录到JSON文件（写入应用内部存储）
     */
    fun saveShareRecord(context: Context, record: ShareRecord) {
        try {
            // 获取现有记录
            val existingRecords = loadShareRecords(context).toMutableList()
            // 添加新记录
            existingRecords.add(record)
            // 转换为JSON
            val json = gson.toJson(existingRecords)

            // 保存到应用内部存储的files目录
            val file = File(context.filesDir, "data/share_records.json")
            file.parentFile?.mkdirs()

            FileOutputStream(file).use { output ->
                output.write(json.toByteArray())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 加载分享记录列表
     */
    fun loadShareRecords(context: Context): List<ShareRecord> {
        return try {
            val file = File(context.filesDir, "data/share_records.json")
            if (!file.exists()) {
                // 如果内部存储文件不存在，尝试从assets加载初始数据
                try {
                    val json = loadJsonFromAssets(context, "share_records.json")
                    val type = object : TypeToken<List<ShareRecord>>() {}.type
                    return gson.fromJson(json, type) ?: emptyList()
                } catch (e: Exception) {
                    return emptyList()
                }
            }

            // 从内部存储读取
            val json = file.readText()
            val type = object : TypeToken<List<ShareRecord>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    /**
     * 生成唯一分享ID
     */
    fun generateShareId(context: Context): String {
        val existingRecords = loadShareRecords(context)
        val maxId = existingRecords.mapNotNull { record ->
            try {
                record.shareId.replace("share_", "").toInt()
            } catch (e: Exception) {
                null
            }
        }.maxOrNull() ?: 0
        return "share_${String.format("%03d", maxId + 1)}"
    }

    /**
     * 保存收藏记录到JSON文件（写入应用内部存储）
     */
    fun saveCollectionRecord(context: Context, record: CollectionRecord) {
        try {
            val existingRecords = loadCollectionRecords(context).toMutableList()
            existingRecords.add(record)
            val json = gson.toJson(existingRecords)
            val file = File(context.filesDir, "data/collection_records.json")
            file.parentFile?.mkdirs()
            FileOutputStream(file).use { output ->
                output.write(json.toByteArray())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 加载收藏记录列表
     */
    fun loadCollectionRecords(context: Context): List<CollectionRecord> {
        return try {
            val file = File(context.filesDir, "data/collection_records.json")
            if (!file.exists()) {
                try {
                    val json = loadJsonFromAssets(context, "collection_records.json")
                    val type = object : TypeToken<List<CollectionRecord>>() {}.type
                    return gson.fromJson(json, type) ?: emptyList()
                } catch (e: Exception) {
                    return emptyList()
                }
            }
            val json = file.readText()
            val type = object : TypeToken<List<CollectionRecord>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    /**
     * 生成唯一收藏ID
     */
    fun generateCollectionId(context: Context): String {
        val existingRecords = loadCollectionRecords(context)
        val maxId = existingRecords.mapNotNull { record ->
            try {
                record.collectionId.replace("collection_", "").toInt()
            } catch (e: Exception) {
                null
            }
        }.maxOrNull() ?: 0
        return "collection_${String.format("%03d", maxId + 1)}"
    }

    /**
     * 保存下载记录到JSON文件（写入应用内部存储）
     */
    fun saveDownloadRecord(context: Context, record: DownloadRecord) {
        try {
            val existingRecords = loadDownloadRecords(context).toMutableList()
            existingRecords.add(record)
            val json = gson.toJson(existingRecords)
            val file = File(context.filesDir, "data/download_records.json")
            file.parentFile?.mkdirs()
            FileOutputStream(file).use { output ->
                output.write(json.toByteArray())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 加载下载记录列表
     */
    fun loadDownloadRecords(context: Context): List<DownloadRecord> {
        return try {
            val file = File(context.filesDir, "data/download_records.json")
            if (!file.exists()) {
                try {
                    val json = loadJsonFromAssets(context, "download_records.json")
                    val type = object : TypeToken<List<DownloadRecord>>() {}.type
                    return gson.fromJson(json, type) ?: emptyList()
                } catch (e: Exception) {
                    return emptyList()
                }
            }
            val json = file.readText()
            val type = object : TypeToken<List<DownloadRecord>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    /**
     * 生成唯一下载ID
     */
    fun generateDownloadId(context: Context): String {
        val existingRecords = loadDownloadRecords(context)
        val maxId = existingRecords.mapNotNull { record ->
            try {
                record.downloadId.replace("download_", "").toInt()
            } catch (e: Exception) {
                null
            }
        }.maxOrNull() ?: 0
        return "download_${String.format("%03d", maxId + 1)}"
    }

    /**
     * 保存歌手关注记录到JSON文件（写入应用内部存储）
     */
    fun saveArtistFollowRecord(context: Context, record: ArtistFollowRecord) {
        try {
            val existingRecords = loadArtistFollowRecords(context).toMutableList()
            existingRecords.add(record)
            val json = gson.toJson(existingRecords)
            val file = File(context.filesDir, "data/artist_follow_records.json")
            file.parentFile?.mkdirs()
            FileOutputStream(file).use { output ->
                output.write(json.toByteArray())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 加载歌手关注记录列表
     */
    fun loadArtistFollowRecords(context: Context): List<ArtistFollowRecord> {
        return try {
            val file = File(context.filesDir, "data/artist_follow_records.json")
            if (!file.exists()) {
                try {
                    val json = loadJsonFromAssets(context, "artist_follow_records.json")
                    val type = object : TypeToken<List<ArtistFollowRecord>>() {}.type
                    return gson.fromJson(json, type) ?: emptyList()
                } catch (e: Exception) {
                    return emptyList()
                }
            }
            val json = file.readText()
            val type = object : TypeToken<List<ArtistFollowRecord>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    /**
     * 加载关注列表
     */
    fun loadFollowItems(context: Context): List<FollowItem> {
        val json = loadJsonFromAssets(context, "follow_items.json")
        val type = object : TypeToken<List<FollowItem>>() {}.type
        return gson.fromJson(json, type)
    }

    /**
     * 加载粉丝列表
     */
    fun loadFanItems(context: Context): List<FanRecord> {
        val json = loadJsonFromInternalStorage(context, "fan_items.json")
        val type = object : TypeToken<List<FanRecord>>() {}.type
        return gson.fromJson(json, type)
    }

    /**
     * 初始化粉丝数据文件，如果内部存储中不存在，则从assets复制
     */
    fun initFanItems(context: Context) {
        val autoTestDir = File(context.filesDir, "autotest")
        if (!autoTestDir.exists()) {
            autoTestDir.mkdirs()
        }

        val fanFile = File(autoTestDir, "fan_items.json")
        if (!fanFile.exists()) {
            try {
                context.assets.open("data/fan_items.json").use { inputStream ->
                    FileOutputStream(fanFile).use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 加载听歌时长数据
     */
    fun loadDurationData(context: Context): ListeningDuration {
        val json = loadJsonFromAssets(context, "duration_data.json")
        return gson.fromJson(json, ListeningDuration::class.java)
    }

    /**
     * 保存歌单列表（保存到内部存储）
     */
    fun savePlaylists(context: Context, playlists: List<Playlist>) {
        try {
            val dataDir = File(context.filesDir, "data")
            if (!dataDir.exists()) {
                dataDir.mkdirs()
            }

            val playlistsFile = File(dataDir, "playlists.json")
            val json = gson.toJson(playlists)

            FileOutputStream(playlistsFile).use { fos ->
                fos.write(json.toByteArray())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 加载歌单列表（优先从内部存储加载，如不存在则从assets加载）
     */
    fun loadPlaylistsWithCache(context: Context): List<Playlist> {
        try {
            // 始终从assets加载以确保数据最新
            val json = loadJsonFromAssets(context, "playlists.json")
            val type = object : TypeToken<List<Playlist>>() {}.type
            return gson.fromJson(json, type)
        } catch (e: Exception) {
            e.printStackTrace()
            return emptyList()
        }
    }

    /**
     * 生成新的歌单ID
     */
    fun generatePlaylistId(): String {
        return "playlist_${System.currentTimeMillis()}"
    }

    /**
     * 添加歌曲到指定歌单
     * @param context 上下文
     * @param playlistId 歌单ID
     * @param songId 歌曲ID
     * @return 是否成功添加（如果歌曲已存在则返回false）
     */
    fun addSongToPlaylist(context: Context, playlistId: String, songId: String): Boolean {
        try {
            // 加载歌单列表（优先从内部存储）
            val playlists = loadPlaylistsWithCache(context).toMutableList()

            // 查找目标歌单
            val playlistIndex = playlists.indexOfFirst { it.playlistId == playlistId }
            if (playlistIndex == -1) {
                return false // 歌单不存在
            }

            val playlist = playlists[playlistIndex]

            // 检查歌曲是否已在歌单中
            if (playlist.songIds.contains(songId)) {
                return false // 歌曲已存在
            }

            // 添加歌曲到歌单
            val updatedSongIds = playlist.songIds.toMutableList()
            updatedSongIds.add(songId)

            // 更新歌单
            val updatedPlaylist = playlist.copy(
                songIds = updatedSongIds,
                songCount = updatedSongIds.size
            )
            playlists[playlistIndex] = updatedPlaylist

            // 保存更新后的歌单列表
            savePlaylists(context, playlists)

            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    /**
     * 从指定歌单移除歌曲
     * @param context 上下文
     * @param playlistId 歌单ID
     * @param songId 歌曲ID
     * @return 是否成功移除
     */
    fun removeSongFromPlaylist(context: Context, playlistId: String, songId: String): Boolean {
        try {
            // 加载歌单列表（优先从内部存储）
            val playlists = loadPlaylistsWithCache(context).toMutableList()

            // 查找目标歌单
            val playlistIndex = playlists.indexOfFirst { it.playlistId == playlistId }
            if (playlistIndex == -1) {
                return false // 歌单不存在
            }

            val playlist = playlists[playlistIndex]

            // 检查歌曲是否在歌单中
            if (!playlist.songIds.contains(songId)) {
                return false // 歌曲不在歌单中
            }

            // 从歌单移除歌曲
            val updatedSongIds = playlist.songIds.filter { it != songId }

            // 更新歌单
            val updatedPlaylist = playlist.copy(
                songIds = updatedSongIds,
                songCount = updatedSongIds.size
            )
            playlists[playlistIndex] = updatedPlaylist

            // 保存更新后的歌单列表
            savePlaylists(context, playlists)

            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    /**
     * 检查歌曲是否已在指定歌单中
     * @param context 上下文
     * @param playlistId 歌单ID
     * @param songId 歌曲ID
     * @return 是否已存在
     */
    fun isSongInPlaylist(context: Context, playlistId: String, songId: String): Boolean {
        return try {
            val playlists = loadPlaylistsWithCache(context)
            val playlist = playlists.find { it.playlistId == playlistId }
            playlist?.songIds?.contains(songId) ?: false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 加载专辑列表
     */
    fun loadAlbums(context: Context): List<com.example.mymusic.data.Album> {
        val json = loadJsonFromAssets(context, "albums.json")
        val type = object : TypeToken<List<com.example.mymusic.data.Album>>() {}.type
        return gson.fromJson(json, type)
    }

    /**
     * 根据ID获取专辑
     */
    fun getAlbumById(context: Context, albumId: String): com.example.mymusic.data.Album? {
        return loadAlbums(context).find { it.albumId == albumId }
    }

    /**
     * 根据艺术家ID获取专辑列表
     */
    fun getAlbumsByArtist(context: Context, artistId: String): List<com.example.mymusic.data.Album> {
        return loadAlbums(context).filter { it.artistId == artistId }
    }

    /**
     * 根据专辑ID获取专辑中的歌曲列表
     */
    fun getSongsInAlbum(context: Context, albumId: String): List<Song> {
        val album = getAlbumById(context, albumId) ?: return emptyList()
        val allSongs = loadSongs(context)
        return album.songIds.mapNotNull { songId ->
            allSongs.find { it.songId == songId }
        }
    }
}
