package com.example.mymusic.data.repository

import android.content.Context
import com.example.mymusic.data.model.Lyric
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LyricRepository(private val context: Context) {

    private val gson = Gson()

    fun getLyricForSong(songId: String): Lyric? {
        // In a real app, you would fetch this from a file or network based on songId
        // For this project, we'll just load a single dummy lyric file.
        return try {
            val json = context.assets.open("data/lyrics.json").bufferedReader().use { it.readText() }
            val type = object : TypeToken<Lyric>() {}.type
            gson.fromJson(json, type)
        } catch (e: Exception) {
            null
        }
    }
}
