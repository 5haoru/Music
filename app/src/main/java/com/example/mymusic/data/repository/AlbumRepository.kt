package com.example.mymusic.data.repository

import android.content.Context
import com.example.mymusic.data.Album
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AlbumRepository(private val context: Context) {

    private val gson = Gson()

    fun getAllAlbums(): List<Album> {
        val json = context.assets.open("data/albums.json").bufferedReader().use { it.readText() }
        val type = object : TypeToken<List<Album>>() {}.type
        return gson.fromJson(json, type)
    }

    fun getAlbumById(albumId: String): Album? {
        return getAllAlbums().find { it.albumId == albumId }
    }

    fun getAlbumsByArtist(artistId: String): List<Album> {
        return getAllAlbums().filter { it.artistId == artistId }
    }
}
