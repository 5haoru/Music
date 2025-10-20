package com.example.mymusic.utils

import android.content.Context
import com.example.mymusic.data.Artist
import com.example.mymusic.data.Playlist
import com.example.mymusic.data.Song
import com.example.mymusic.data.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

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
     */
    fun getPlaylistById(context: Context, playlistId: String): Playlist? {
        return loadPlaylists(context).find { it.playlistId == playlistId }
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
}
