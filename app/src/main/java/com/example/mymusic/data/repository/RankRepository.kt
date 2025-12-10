package com.example.mymusic.data.repository

import android.content.Context
import com.example.mymusic.data.model.Rank
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RankRepository(private val context: Context) {

    private val gson = Gson()

    fun getAllRanks(): List<Rank> {
        return try {
            val json = context.assets.open("data/ranks.json").bufferedReader().use { it.readText() }
            val type = object : TypeToken<List<Rank>>() {}.type
            gson.fromJson(json, type)
        } catch (e: Exception) {
            emptyList()
        }
    }
}
