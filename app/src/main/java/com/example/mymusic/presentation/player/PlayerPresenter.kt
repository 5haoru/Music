package com.example.mymusic.presentation.player

import com.example.mymusic.data.PlayerStyle
import com.example.mymusic.data.PlayerStyleRecord
import com.example.mymusic.data.repository.PlaybackStyleRecordRepository
import com.example.mymusic.data.repository.PlayerStyleRepository
import com.example.mymusic.utils.AutoTestHelper

class PlayerPresenter(
    private val view: PlayerContract.View,
    private val playerStyleRepository: PlayerStyleRepository,
    private val playbackStyleRecordRepository: PlaybackStyleRecordRepository
) : PlayerContract.Presenter {

    private var currentStyleId: String = "retro_cd" // Default style

    override fun loadPlayerStyles() {
        view.showLoading()
        try {
            val records = playbackStyleRecordRepository.getAllRecords()
            if (records.isNotEmpty()) {
                currentStyleId = records.last().styleType
            }

            val allStyles = playerStyleRepository.getPlayerStyles().map { style ->
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

    override fun confirmStyleChange(style: PlayerStyle) {
        try {
            val newRecord = com.example.mymusic.data.model.PlaybackStyleRecord(
                styleType = style.styleId,
                changeTime = System.currentTimeMillis(),
                isSuccess = true
            )
            playbackStyleRecordRepository.savePlaybackStyleRecord(newRecord)

            currentStyleId = style.styleId
            AutoTestHelper.updatePlayerStyle(style.styleId, style.styleName)
            view.showToast("更改样式成功")
            loadPlayerStyles()
        } catch (e: Exception) {
            view.showError("保存样式设置失败: ${e.message}")
        }
    }
    
    // Other methods remain largely the same
    override fun onStyleSelected(style: PlayerStyle) { view.showConfirmDialog(style) }
    override fun onTabSelected(category: String) {
        try {
            val allStyles = playerStyleRepository.getPlayerStyles()
            val filteredStyles = if (category == "全部") allStyles else allStyles.filter { it.category == category }
            view.showPlayerStyles(filteredStyles.map { it.copy(isInUse = it.styleId == currentStyleId) })
        } catch (e: Exception) {
            view.showError("加载样式失败: ${e.message}")
        }
    }
    override fun onBackClick() { view.navigateBack() }
    override fun onDestroy() {}
}
