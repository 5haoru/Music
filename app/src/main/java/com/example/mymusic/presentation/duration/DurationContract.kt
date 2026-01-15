package com.example.mymusic.presentation.duration

import com.example.mymusic.base.BasePresenter
import com.example.mymusic.base.BaseView
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
        fun onTabSelected(tab: Int) // 0:《 1:《 2:《
        fun onBackClick()
    }
}
