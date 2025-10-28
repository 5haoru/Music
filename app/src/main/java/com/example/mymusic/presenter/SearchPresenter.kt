package com.example.mymusic.presenter

import android.content.Context
import com.example.mymusic.data.Song
import com.example.mymusic.model.SearchRecord
import com.example.mymusic.utils.DataLoader
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

/**
 * 搜索页面Presenter
 */
class SearchPresenter(
    private val view: SearchContract.View,
    private val context: Context
) : SearchContract.Presenter {

    private var allSongs: List<Song> = emptyList()
    private var searchHistory: MutableList<String> = mutableListOf()
    private val searchRecordsFile = File(context.filesDir, "search_records.json")

    override fun loadData() {
        view.showLoading()
        try {
            // 加载歌曲数据
            allSongs = DataLoader.loadSongs(context)

            // 加载搜索历史
            loadSearchHistory()
            view.showSearchHistory(searchHistory.take(3))

            // 显示猜你喜欢（推荐歌曲）
            view.showRecommendedSongs(allSongs.take(6))

            // 显示热搜榜（前10首歌）
            view.showHotSearchList(allSongs.take(10))

            // 显示热歌榜（后10首歌）
            view.showHotSongList(allSongs.takeLast(10).reversed())

            view.hideLoading()
        } catch (e: Exception) {
            view.hideLoading()
            view.showError("加载数据失败: ${e.message}")
        }
    }

    override fun onSearchTextChanged(query: String) {
        if (query.isEmpty()) {
            // 清空搜索结果，显示默认内容
            loadData()
        } else {
            // 搜索歌曲（支持中文、英文、拼音搜索）
            val results = allSongs.filter { song ->
                song.songName.contains(query, ignoreCase = true) ||
                song.artist.contains(query, ignoreCase = true) ||
                song.pinyin?.contains(query, ignoreCase = true) == true ||
                song.artistPinyin?.contains(query, ignoreCase = true) == true
            }
            view.showSearchResults(results)

            // 保存搜索历史
            if (query.length >= 2) {
                saveSearchHistory(query)
            }
        }
    }

    override fun onSearchHistoryItemClick(query: String) {
        onSearchTextChanged(query)
    }

    override fun onClearSearchHistory() {
        searchHistory.clear()
        saveSearchHistoryToFile()
        view.showSearchHistory(emptyList())
    }

    override fun onSongClick(songId: String) {
        val song = allSongs.find { it.songId == songId }
        song?.let {
            view.playSong(it)
        }
    }

    override fun onQuickEntryClick(entryType: String) {
        // TODO: 实现快捷入口功能
        // entryType: "artist", "genre", "zone", "recognize", "audiobook"
    }

    override fun onDestroy() {
        // Clean up resources
    }

    private fun loadSearchHistory() {
        try {
            if (searchRecordsFile.exists()) {
                val json = searchRecordsFile.readText()
                val type = object : TypeToken<List<SearchRecord>>() {}.type
                val records: List<SearchRecord> = Gson().fromJson(json, type)
                searchHistory = records.map { it.keyword }.toMutableList()
            }
        } catch (e: Exception) {
            searchHistory = mutableListOf()
        }
    }

    private fun saveSearchHistory(query: String) {
        // 去重并添加到历史记录最前面
        searchHistory.remove(query)
        searchHistory.add(0, query)

        // 最多保留20条记录
        if (searchHistory.size > 20) {
            searchHistory = searchHistory.take(20).toMutableList()
        }

        saveSearchHistoryToFile()
    }

    private fun saveSearchHistoryToFile() {
        try {
            val records = searchHistory.mapIndexed { index, query ->
                SearchRecord(
                    searchId = "search_${System.currentTimeMillis()}_$index",
                    keyword = query,
                    searchTime = System.currentTimeMillis(),
                    resultCount = allSongs.filter { song ->
                        song.songName.contains(query, ignoreCase = true) ||
                        song.artist.contains(query, ignoreCase = true) ||
                        song.pinyin?.contains(query, ignoreCase = true) == true ||
                        song.artistPinyin?.contains(query, ignoreCase = true) == true
                    }.size,
                    isSuccess = true
                )
            }
            val json = Gson().toJson(records)
            searchRecordsFile.writeText(json)
        } catch (e: Exception) {
            // Ignore save errors
        }
    }
}
