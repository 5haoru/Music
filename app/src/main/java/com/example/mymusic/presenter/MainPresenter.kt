package com.example.mymusic.presenter

import android.content.Context

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

/**
 * Main页面契约接口
 */
interface MainContract {
    interface View : BaseView {
        fun switchTab(index: Int)
    }

    interface Presenter : BasePresenter {
        fun onTabSelected(index: Int)
        fun getCurrentTab(): Int
    }
}
