package com.example.mymusic.data.repository

import android.content.Context
import com.example.mymusic.data.Playlist
import com.example.mymusic.data.Song
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets

class PlaylistRepository(private val context: Context) {

    private val gson = Gson()
    private var cachedPlaylists: List<Playlist>? = null

    fun getAllPlaylists(): List<Playlist> {
        if (cachedPlaylists == null) {
            val json = context.assets.open("data/playlists.json").bufferedReader().use { it.readText() }
            val type = object : TypeToken<List<Playlist>>() {}.type
            cachedPlaylists = gson.fromJson(json, type)
        }
        return cachedPlaylists ?: emptyList()
    }

    fun addSongToPlaylist(playlistId: String, songId: String): Boolean {
        val currentPlaylists = getAllPlaylists().toMutableList()
        val playlistToUpdate = currentPlaylists.find { it.playlistId == playlistId }

        if (playlistToUpdate == null || playlistToUpdate.songIds.contains(songId)) {
            return false
        }

        val updatedSongIds = playlistToUpdate.songIds.toMutableList()
        updatedSongIds.add(songId)
        val newPlaylist = playlistToUpdate.copy(songIds = updatedSongIds)

        val index = currentPlaylists.indexOfFirst { it.playlistId == playlistId }
        if (index != -1) {
            currentPlaylists[index] = newPlaylist
            savePlaylists(currentPlaylists)
            return true
        }
        return false
    }

    fun removeSongFromPlaylist(playlistId: String, songId: String): Boolean {
        val currentPlaylists = getAllPlaylists().toMutableList()
        val playlistToUpdate = currentPlaylists.find { it.playlistId == playlistId }

        if (playlistToUpdate == null || !playlistToUpdate.songIds.contains(songId)) {
            return false
        }

        val updatedSongIds = playlistToUpdate.songIds.toMutableList()
        updatedSongIds.remove(songId)
        val newPlaylist = playlistToUpdate.copy(songIds = updatedSongIds)

        val index = currentPlaylists.indexOfFirst { it.playlistId == playlistId }
        if (index != -1) {
            currentPlaylists[index] = newPlaylist
            savePlaylists(currentPlaylists)
            return true
        }
        return false
    }

    fun savePlaylists(playlists: List<Playlist>) {
        val jsonString = gson.toJson(playlists)
        context.openFileOutput("playlists.json", Context.MODE_PRIVATE).use {
            OutputStreamWriter(it, StandardCharsets.UTF_8).use { writer ->
                writer.write(jsonString)
            }
        }
        cachedPlaylists = playlists
    }
}
