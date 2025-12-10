package com.example.mymusic.presentation.fan

import com.example.mymusic.data.model.FanRecord
import com.example.mymusic.data.repository.FanRepository

class FanPresenter(
    private val view: FanContract.View,
    private val fanRepository: FanRepository
) : FanContract.Presenter {

    private var allFans: List<FanRecord> = emptyList()

    override fun loadFans() {
        view.showLoading()
        try {
            val fanData = fanRepository.getFanData()
            allFans = fanData.fans
            view.showFans(allFans)
            view.hideLoading()
        } catch (e: Exception) {
            view.showError(e.message ?: "Unknown error loading fans")
            view.hideLoading()
        }
    }

    override fun onFollowButtonClicked(fan: FanRecord) {
        val currentFanCount = allFans.size
        view.updateFanCountOnMyPage(currentFanCount + 1)
    }

    fun onBackClick() {
        view.navigateBack()
    }

    override fun onDestroy() {
        // Clean up resources if needed
    }

    override fun attachView(view: FanContract.View) {
        // No-op
    }

    override fun detachView() {
        // No-op
    }
}
