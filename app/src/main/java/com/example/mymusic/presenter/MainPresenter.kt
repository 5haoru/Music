package com.example.mymusic.presenter

import android.content.Context
import com.example.mymusic.contract.MainContract

/**
 * 主页面Presenter
 */
class MainPresenter(
    private val view: MainContract.View,
    private val context: Context
) : MainContract.Presenter {

    private var currentTab = 0

    override fun onTabSelected(index: Int) {
        currentTab = index
        view.switchTab(index)
    }

    override fun getCurrentTab(): Int = currentTab

    override fun onDestroy() {
        // Clean up resources if needed
    }
}
