package com.example.mymusic.data.repository

import android.content.Context
import com.example.mymusic.data.ListeningDuration
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DurationRepository(private val context: Context) {

    private val gson = Gson()

    fun getListeningDuration(): ListeningDuration {
        val json = context.assets.open("data/duration_data.json").bufferedReader().use { it.readText() }
        val type = object : TypeToken<ListeningDuration>() {}.type
        return gson.fromJson(json, type)
    }
}
