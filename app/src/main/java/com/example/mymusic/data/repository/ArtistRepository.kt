package com.example.mymusic.data.repository

import android.content.Context
import com.example.mymusic.data.Artist
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ArtistRepository(private val context: Context) {

    private val gson = Gson()

    fun getAllArtists(): List<Artist> {
        val json = context.assets.open("data/artists.json").bufferedReader().use { it.readText() }
        val type = object : TypeToken<List<Artist>>() {}.type
        return gson.fromJson(json, type)
    }

    fun getArtistById(artistId: String): Artist? {
        return getAllArtists().find { it.artistId == artistId }
    }
}
