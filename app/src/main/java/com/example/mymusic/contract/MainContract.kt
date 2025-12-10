package com.example.mymusic.contract

import com.example.mymusic.base.BasePresenter
import com.example.mymusic.base.BaseView

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
