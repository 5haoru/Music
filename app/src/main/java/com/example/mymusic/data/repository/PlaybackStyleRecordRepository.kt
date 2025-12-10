package com.example.mymusic.data.repository

import android.content.Context
import com.example.mymusic.data.model.PlaybackStyleRecord
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets

class PlaybackStyleRecordRepository(private val context: Context) {

    private val gson = Gson()
    private val RECORDS_FILE = "playback_style_records.json"

    fun savePlaybackStyleRecord(record: PlaybackStyleRecord) {
        val records = getAllRecords().toMutableList()
        records.add(record)
        saveAllRecords(records)
    }

    fun getAllRecords(): List<PlaybackStyleRecord> {
        return try {
            val json = context.openFileInput(RECORDS_FILE).bufferedReader().use { it.readText() }
            val type = object : TypeToken<List<PlaybackStyleRecord>>() {}.type
            gson.fromJson(json, type)
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun saveAllRecords(records: List<PlaybackStyleRecord>) {
        val jsonString = gson.toJson(records)
        context.openFileOutput(RECORDS_FILE, Context.MODE_PRIVATE).use {
            OutputStreamWriter(it, StandardCharsets.UTF_8).use { writer ->
                writer.write(jsonString)
            }
        }
    }
    
    fun generateRecordId(): String {
        val records = getAllRecords()
        return if (records.isEmpty()) {
            "PSR001"
        } else {
            val num = records.size + 1
            "PSR" + String.format("%03d", num)
        }
    }
}
