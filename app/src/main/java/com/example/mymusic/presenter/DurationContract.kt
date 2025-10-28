package com.example.mymusic.presenter

import com.example.mymusic.data.ListeningDuration

/**
 * 听歌时长页面契约接口
 */
interface DurationContract {
    interface View : BaseView {
        fun showDurationData(data: ListeningDuration)
    }

    interface Presenter : BasePresenter {
        fun loadData()
        fun onTabSelected(tab: Int) // 0:周, 1:月, 2:年
        fun onBackClick()
    }
}
