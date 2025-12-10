package com.example.mymusic.data.repository

import android.content.Context
import com.example.mymusic.data.MusicVideo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MusicVideoRepository(private val context: Context) {

    private val gson = Gson()

    fun getAllMusicVideos(): List<MusicVideo> {
        return try {
            val json = context.assets.open("data/music_videos.json").bufferedReader().use { it.readText() }
            val type = object : TypeToken<List<MusicVideo>>() {}.type
            gson.fromJson(json, type)
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun getMusicVideoById(mvId: String): MusicVideo? {
        return try {
            val json = context.assets.open("data/music_videos.json").bufferedReader().use { it.readText() }
            val type = object : TypeToken<List<MusicVideo>>() {}.type
            val videos: List<MusicVideo> = gson.fromJson(json, type)
            videos.find { it.mvId == mvId }
        } catch (e: Exception) {
            null
        }
    }
}
