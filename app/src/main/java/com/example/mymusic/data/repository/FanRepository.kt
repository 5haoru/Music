package com.example.mymusic.data.repository

import android.content.Context
import com.example.mymusic.data.model.FanRecord
import com.example.mymusic.utils.DataLoader

class FanRepository(private val context: Context) {

    fun getFanData(): FanData {
        return FanData(DataLoader.loadFanItems(context))
    }
}

data class FanData(val fans: List<FanRecord>)
