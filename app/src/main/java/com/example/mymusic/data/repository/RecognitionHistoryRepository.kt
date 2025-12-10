package com.example.mymusic.data.repository

import android.content.Context
import com.example.mymusic.data.RecognitionRecord
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RecognitionHistoryRepository(private val context: Context) {

    private val gson = Gson()
    private val PREF_NAME = "recognition_history"
    private val KEY_RECORDS = "records"

    fun getRecognitionHistory(): List<RecognitionRecord> {
        val sharedPrefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val json = sharedPrefs.getString(KEY_RECORDS, null)
        return if (json != null) {
            gson.fromJson(json, object : TypeToken<List<RecognitionRecord>>() {}.type)
        } else {
            emptyList()
        }
    }

    fun addRecognitionRecord(record: RecognitionRecord) {
        val history = getRecognitionHistory().toMutableList()
        history.add(0, record) // Add to the beginning
        saveRecognitionHistory(history)
    }

    private fun saveRecognitionHistory(history: List<RecognitionRecord>) {
        val sharedPrefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        val json = gson.toJson(history)
        editor.putString(KEY_RECORDS, json)
        editor.apply()
    }
}
