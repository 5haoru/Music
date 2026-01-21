package com.example.mymusic.data.repository

import android.content.Context
import com.example.mymusic.data.model.ArtistFollowRecord
import com.example.mymusic.utils.AutoTestHelper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets

class ArtistFollowRecordRepository(private val context: Context) {

    private val gson = Gson()
    private val RECORDS_FILE = "artist_follow_records.json"

    fun addFollowRecord(artistId: String, artistName: String) {
        val record = ArtistFollowRecord(
            artistId = artistId,
            artistName = artistName,
            operationType = "follow",
            operationTime = System.currentTimeMillis(),
            isSuccess = true
        )
        saveArtistFollowRecord(record)

        // 同步到AutoTestHelper
        AutoTestHelper.addFollowedArtist(artistId, artistName)
    }

    fun removeFollowRecord(artistId: String) {
        val records = getAllRecords().filterNot { it.artistId == artistId }
        saveAllRecords(records)

        // 同步到AutoTestHelper
        AutoTestHelper.removeFollowedArtist(artistId)
    }

    fun saveArtistFollowRecord(record: ArtistFollowRecord) {
        val records = getAllRecords().toMutableList()
        records.add(record)
        saveAllRecords(records)
    }

    private fun getAllRecords(): List<ArtistFollowRecord> {
        return try {
            val json = context.openFileInput(RECORDS_FILE).bufferedReader().use { it.readText() }
            val type = object : TypeToken<List<ArtistFollowRecord>>() {}.type
            gson.fromJson(json, type)
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun saveAllRecords(records: List<ArtistFollowRecord>) {
        val jsonString = gson.toJson(records)
        context.openFileOutput(RECORDS_FILE, Context.MODE_PRIVATE).use {
            OutputStreamWriter(it, StandardCharsets.UTF_8).use { writer ->
                writer.write(jsonString)
            }
        }
    }
}
