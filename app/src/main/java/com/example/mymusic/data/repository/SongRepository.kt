package com.example.mymusic.data.repository

import android.content.Context
import com.example.mymusic.data.Song
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SongRepository(private val context: Context) {

    private val gson = Gson()
    private var cachedSongs: List<Song>? = null

    fun getAllSongs(): List<Song> {
        if (cachedSongs == null) {
            val json = context.assets.open("data/songs.json").bufferedReader().use { it.readText() }
            val type = object : TypeToken<List<Song>>() {}.type
            cachedSongs = gson.fromJson(json, type)
        }
        return cachedSongs ?: emptyList()
    }

    fun getSongById(songId: String): Song? {
        return getAllSongs().find { it.songId == songId }
    }
}
