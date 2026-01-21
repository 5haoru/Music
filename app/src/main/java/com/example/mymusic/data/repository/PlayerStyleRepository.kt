package com.example.mymusic.data.repository

import android.content.Context
import com.example.mymusic.data.PlayerStyle
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PlayerStyleRepository(private val context: Context) {

    private val gson = Gson()

    fun getPlayerStyles(): List<PlayerStyle> {
        return try {
            val json = context.assets.open("data/player_styles.json").bufferedReader().use { it.readText() }
            val type = object : TypeToken<List<PlayerStyle>>() {}.type
            gson.fromJson(json, type)
        } catch (e: Exception) {
            // 如果文件不存在，返回预设样式
            PlayerStyle.getAllStyles()
        }
    }
}
