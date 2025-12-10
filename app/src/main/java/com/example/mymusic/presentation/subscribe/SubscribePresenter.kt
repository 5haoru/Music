package com.example.mymusic.presentation.subscribe

import android.content.Context
import com.example.mymusic.data.FollowItem
import com.example.mymusic.utils.AutoTestHelper
import com.example.mymusic.utils.DataLoader

/**
 * 关注页面Presenter
 * 处理关注列表的业务逻辑
 */
class SubscribePresenter(
    private val view: SubscribeContract.View,
    private val context: Context
) : SubscribeContract.Presenter {

    private var allFollowItems: List<FollowItem> = emptyList()
    private var currentFilterType: String = "all"

    override fun loadData() {
        view.showLoading()
        try {
            allFollowItems = DataLoader.loadFollowItems(context)

            if (allFollowItems.isNotEmpty()) {
                view.showFollowItems(allFollowItems)

                // 检查是否有艺术家更新，显示聚合提示
                val artistsWithUpdates = allFollowItems.filter {
                    it.type == "artist" && it.activityType != null
                }
                if (artistsWithUpdates.size >= 3) {
                    val artistNames = artistsWithUpdates.take(3).map { it.name }
                    view.showAggregatedUpdate(artistNames, artistsWithUpdates.size)
                }

                view.hideLoading()
            } else {
                view.hideLoading()
                view.showError("暂无关注")
            }
        } catch (e: Exception) {
            view.hideLoading()
            view.showError("加载失败: ${e.message}")
        }
    }

    override fun filterByType(type: String) {
        currentFilterType = type
        val filteredItems = when (type) {
            "all" -> allFollowItems
            "artist" -> allFollowItems.filter { it.type == "artist" }
            "user" -> allFollowItems.filter { it.type == "user" }
            else -> allFollowItems
        }
        view.updateFilteredItems(filteredItems)
    }

    override fun sortByTime() {
        // 按关注时间排序（降序，最近关注的在前面）
        val sortedItems = when (currentFilterType) {
            "all" -> allFollowItems.sortedByDescending { it.followTime }
            "artist" -> allFollowItems.filter { it.type == "artist" }.sortedByDescending { it.followTime }
            "user" -> allFollowItems.filter { it.type == "user" }.sortedByDescending { it.followTime }
            else -> allFollowItems.sortedByDescending { it.followTime }
        }
        view.updateFilteredItems(sortedItems)
    }

    override fun onBackClick() {
        // 返回由View层处理
    }

    override fun onSearchClick() {
        // TODO: 实现搜索功能
    }

    override fun onAggregatedUpdateClick() {
        // TODO: 跳转到聚合更新页面
    }

    override fun onFollowItemClick(item: FollowItem) {
        // TODO: 跳转到用户/艺术家主页
    }

    override fun onFollowItemMoreClick(item: FollowItem) {
        // 更多按钮点击，由View层显示取消关注对话框
    }

    override fun unfollowItem(item: FollowItem) {
        // 从列表中移除该关注项
        allFollowItems = allFollowItems.filter { it.id != item.id }

        // 如果是歌手类型，同步到AutoTest
        // 注意：这里使用item.id作为artistId，用于测试验证
        if (item.type == "artist") {
            AutoTestHelper.removeFollowedArtist(item.id)
        }

        // 根据当前筛选条件更新显示
        filterByType(currentFilterType)

        // 显示成功提示
        view.showUnfollowSuccess(item.name)
    }

    override fun onDestroy() {
        // 清理资源
    }
}
