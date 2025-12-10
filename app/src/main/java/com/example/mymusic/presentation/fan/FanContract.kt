package com.example.mymusic.presentation.fan

import com.example.mymusic.base.BasePresenter
import com.example.mymusic.base.BaseView
import com.example.mymusic.data.model.FanRecord

interface FanContract {
    interface View : BaseView {
        fun showFans(fans: List<FanRecord>)
        fun navigateBack()
        fun updateFanCountOnMyPage(count: Int)
    }

    interface Presenter : BasePresenter {
        fun attachView(view: View)
        fun detachView()
        fun loadFans()
        fun onFollowButtonClicked(fan: FanRecord)
    }

    interface Model {
        fun getFans(callback: (List<FanRecord>?, String?) -> Unit)
    }
}
