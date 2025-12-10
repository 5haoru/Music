package com.example.mymusic.presentation.duration

import com.example.mymusic.data.ListeningDuration
import com.example.mymusic.data.repository.DurationRepository
import com.example.mymusic.utils.AutoTestHelper

/**
 * 听歌时长页面Presenter
 * 处理听歌时长统计的业务逻辑
 */
class DurationPresenter(
    private val view: DurationContract.View,
    private val durationRepository: DurationRepository
) : DurationContract.Presenter {

    private var durationData: ListeningDuration? = null

    override fun loadData() {
        view.showLoading()
        try {
            durationData = durationRepository.getListeningDuration()

            if (durationData != null) {
                view.showDurationData(durationData!!)
                view.hideLoading()
                AutoTestHelper.updateViewedStats("weekly")
            } else {
                view.hideLoading()
                view.showError("暂无听歌数据")
            }
        } catch (e: Exception) {
            view.hideLoading()
            view.showError("加载失败: ${e.message}")
        }
    }

    override fun onTabSelected(tab: Int) {
        // 记录用户查看的统计类型到AutoTest
        when (tab) {
            0 -> AutoTestHelper.updateViewedStats("weekly")   // 周统�?
            1 -> AutoTestHelper.updateViewedStats("monthly")  // 月统�?
            // tab=2 是年度统计，测试任务不需要记�?
        }
    }

    override fun onBackClick() {
        // 返回由View层处�?
    }

    override fun onDestroy() {
        // 清理资源
    }
}
