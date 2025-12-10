package com.example.mymusic.data.repository

import android.content.Context
import com.example.mymusic.data.model.DownloadRecord
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets

class DownloadRecordRepository(private val context: Context) {

    private val gson = Gson()
    private val RECORDS_FILE = "download_records.json"

    fun saveDownloadRecord(record: DownloadRecord) {
        val records = getAllRecords().toMutableList()
        records.add(record)
        saveAllRecords(records)
    }

    private fun getAllRecords(): List<DownloadRecord> {
        return try {
            val json = context.openFileInput(RECORDS_FILE).bufferedReader().use { it.readText() }
            val type = object : TypeToken<List<DownloadRecord>>() {}.type
            gson.fromJson(json, type)
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun saveAllRecords(records: List<DownloadRecord>) {
        val jsonString = gson.toJson(records)
        context.openFileOutput(RECORDS_FILE, Context.MODE_PRIVATE).use {
            OutputStreamWriter(it, StandardCharsets.UTF_8).use { writer ->
                writer.write(jsonString)
            }
        }
    }

    fun generateDownloadId(): String {
        val records = getAllRecords()
        return if (records.isEmpty()) {
            "DR001"
        } else {
            val lastId = records.last().downloadId
            val num = lastId.substring(2).toInt() + 1
            "DR" + String.format("%03d", num)
        }
    }
}
