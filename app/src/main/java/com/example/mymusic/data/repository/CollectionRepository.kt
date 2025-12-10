package com.example.mymusic.data.repository

import android.content.Context
import com.example.mymusic.data.model.CollectionRecord
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets

class CollectionRepository(private val context: Context) {

    private val gson = Gson()
    private var cachedCollectionRecords: List<CollectionRecord>? = null
    private val COLLECTION_RECORDS_FILE = "collection_records.json"

    fun getAllCollectionRecords(): List<CollectionRecord> {
        if (cachedCollectionRecords == null) {
            cachedCollectionRecords = try {
                val json = context.openFileInput(COLLECTION_RECORDS_FILE).bufferedReader().use { it.readText() }
                val type = object : TypeToken<List<CollectionRecord>>() {}.type
                gson.fromJson(json, type)
            } catch (e: Exception) {
                emptyList()
            }
        }
        return cachedCollectionRecords ?: emptyList()
    }

    fun saveCollectionRecord(record: CollectionRecord) {
        val currentRecords = getAllCollectionRecords().toMutableList()
        currentRecords.add(record)
        saveAllCollectionRecords(currentRecords)
    }

    private fun saveAllCollectionRecords(records: List<CollectionRecord>) {
        val jsonString = gson.toJson(records)
        context.openFileOutput(COLLECTION_RECORDS_FILE, Context.MODE_PRIVATE).use {
            OutputStreamWriter(it, StandardCharsets.UTF_8).use { writer ->
                writer.write(jsonString)
            }
        }
        cachedCollectionRecords = records
    }

    fun generateCollectionId(): String {
        val currentRecords = getAllCollectionRecords()
        return if (currentRecords.isEmpty()) {
            "CR001"
        } else {
            val lastId = currentRecords.last().collectionId
            val num = lastId.substring(2).toInt() + 1
            "CR" + String.format("%03d", num)
        }
    }
}
