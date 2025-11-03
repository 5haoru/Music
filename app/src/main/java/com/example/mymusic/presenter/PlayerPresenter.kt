package com.example.mymusic.presenter

import android.content.Context
import com.example.mymusic.data.PlayerStyle
import com.example.mymusic.data.PlayerStyleRecord
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileWriter

/**
 * 播放器样式选择页面的 Presenter
 */
class PlayerPresenter(
    private val view: PlayerContract.View,
    private val context: Context
) : PlayerContract.Presenter {

    private val gson = Gson()
    private val styleRecordFile = File(context.filesDir, "playback_style_records.json")
    private var currentStyleId: String = "retro_cd" // 默认样式

    override fun loadPlayerStyles() {
        view.showLoading()
        try {
            // 加载当前使用的样式
            val records = loadStyleRecords()
            if (records.isNotEmpty()) {
                currentStyleId = records.last().styleId
            }

            // 加载所有样式并更新isInUse状态
            val allStyles = PlayerStyle.getAllStyles().map { style ->
                style.copy(isInUse = style.styleId == currentStyleId)
            }

            view.showPlayerStyles(allStyles)
            view.showSelectedStyle(currentStyleId)
            view.hideLoading()
        } catch (e: Exception) {
            view.hideLoading()
            view.showError("加载播放器样式失败: ${e.message}")
        }
    }

    override fun onStyleSelected(style: PlayerStyle) {
        // 显示确认对话框
        view.showConfirmDialog(style)
    }

    override fun confirmStyleChange(style: PlayerStyle) {
        try {
            val records = loadStyleRecords().toMutableList()

            // 添加新记录
            val newRecord = PlayerStyleRecord(
                recordId = "style_${System.currentTimeMillis()}",
                styleId = style.styleId,
                timestamp = System.currentTimeMillis()
            )
            records.add(newRecord)

            // 保存到文件
            saveStyleRecords(records)

            currentStyleId = style.styleId

            view.showToast("更改样式成功")

            // 重新加载样式列表以更新UI
            loadPlayerStyles()
        } catch (e: Exception) {
            view.showError("保存样式设置失败: ${e.message}")
        }
    }

    override fun onTabSelected(category: String) {
        // 根据类别筛选样式
        try {
            val filteredStyles = PlayerStyle.getStylesByCategory(category).map { style ->
                style.copy(isInUse = style.styleId == currentStyleId)
            }
            view.showPlayerStyles(filteredStyles)
        } catch (e: Exception) {
            view.showError("加载样式失败: ${e.message}")
        }
    }

    override fun onBackClick() {
        view.navigateBack()
    }

    override fun onDestroy() {
        // 清理资源
    }

    /**
     * 从文件加载样式记录
     */
    private fun loadStyleRecords(): List<PlayerStyleRecord> {
        return try {
            if (styleRecordFile.exists()) {
                val json = styleRecordFile.readText()
                val type = object : TypeToken<List<PlayerStyleRecord>>() {}.type
                gson.fromJson(json, type) ?: emptyList()
            } else {
                // 如果文件不存在，尝试从assets读取
                val json = context.assets.open("data/playback_style_records.json").bufferedReader().use { it.readText() }
                val type = object : TypeToken<List<PlayerStyleRecord>>() {}.type
                gson.fromJson(json, type) ?: emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * 保存样式记录到文件
     */
    private fun saveStyleRecords(records: List<PlayerStyleRecord>) {
        val json = gson.toJson(records)
        FileWriter(styleRecordFile).use { it.write(json) }
    }
}
