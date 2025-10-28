package com.example.mymusic.presenter

import com.example.mymusic.data.FollowItem

/**
 * 关注页面契约接口
 */
interface SubscribeContract {
    interface View : BaseView {
        fun showFollowItems(items: List<FollowItem>)
        fun updateFilteredItems(items: List<FollowItem>)
        fun showAggregatedUpdate(artistNames: List<String>, count: Int)
    }

    interface Presenter : BasePresenter {
        fun loadData()
        fun filterByType(type: String) // "all", "artist", "user"
        fun sortByTime()
        fun onBackClick()
        fun onSearchClick()
        fun onAggregatedUpdateClick()
        fun onFollowItemClick(item: FollowItem)
        fun onFollowItemMoreClick(item: FollowItem)
        fun unfollowItem(item: FollowItem)
    }
}
