package com.example.mymusic.presentation.songsort

import android.content.Context
import com.example.mymusic.data.SortOrderRecord
import com.example.mymusic.utils.AutoTestHelper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileWriter

/**
 * 歌曲排序选择页面�?Presenter
 */
class SongSortPresenter(
    private val view: SongSortContract.View,
    private val context: Context
) : SongSortContract.Presenter {

    private val gson = Gson()
    private val sortOrderFile = File(context.filesDir, "sort_order_records.json")

    override fun loadCurrentSortOrder(playlistId: String) {
        view.showLoading()
        try {
            val records = loadSortOrderRecords()
            val currentRecord = records.find { it.playlistId == playlistId }

            // 默认按收藏时间从新到旧排序
            val sortType = currentRecord?.sortType ?: SortOrderRecord.SORT_TIME_DESC
            view.showCurrentSortOrder(sortType)
            view.hideLoading()
        } catch (e: Exception) {
            view.hideLoading()
            view.showError("加载排序设置失败: ${e.message}")
        }
    }

    override fun onSortOptionSelected(playlistId: String, sortType: String) {
        try {
            val records = loadSortOrderRecords().toMutableList()

            // 移除该歌单的旧记录
            records.removeAll { it.playlistId == playlistId }

            // 添加新记录
            val newRecord = SortOrderRecord(
                recordId = "sort_${System.currentTimeMillis()}",
                playlistId = playlistId,
                sortType = sortType,
                timestamp = System.currentTimeMillis()
            )
            records.add(newRecord)

            // 保存到文件
            saveSortOrderRecords(records)

            // 更新AutoTestHelper中的歌单排序顺序
            AutoTestHelper.updatePlaylistSortOrder(playlistId, sortType)

            view.showToast("已切换为：$sortType")
            view.navigateBack()
        } catch (e: Exception) {
            view.showError("保存排序设置失败: ${e.message}")
        }
    }

    override fun onBackClick() {
        view.navigateBack()
    }

    override fun onDestroy() {
        // 清理资源
    }

    /**
     * 从文件加载排序记录
     */
    private fun loadSortOrderRecords(): List<SortOrderRecord> {
        return try {
            if (sortOrderFile.exists()) {
                val json = sortOrderFile.readText()
                val type = object : TypeToken<List<SortOrderRecord>>() {}.type
                gson.fromJson(json, type) ?: emptyList()
            } else {
                // 如果文件不存在，尝试从assets读取
                val json = context.assets.open("data/sort_order_records.json").bufferedReader().use { it.readText() }
                val type = object : TypeToken<List<SortOrderRecord>>() {}.type
                gson.fromJson(json, type) ?: emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * 保存排序记录到文件
     */
    private fun saveSortOrderRecords(records: List<SortOrderRecord>) {
        val json = gson.toJson(records)
        FileWriter(sortOrderFile).use { it.write(json) }
    }
}
