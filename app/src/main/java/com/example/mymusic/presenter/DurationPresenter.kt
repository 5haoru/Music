package com.example.mymusic.presenter

import android.content.Context
import com.example.mymusic.data.ListeningDuration
import com.example.mymusic.utils.DataLoader

/**
 * 听歌时长页面Presenter
 * 处理听歌时长统计的业务逻辑
 */
class DurationPresenter(
    private val view: DurationContract.View,
    private val context: Context
) : DurationContract.Presenter {

    private var durationData: ListeningDuration? = null

    override fun loadData() {
        view.showLoading()
        try {
            durationData = DataLoader.loadDurationData(context)

            if (durationData != null) {
                view.showDurationData(durationData!!)
                view.hideLoading()
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
        // Tab切换逻辑，View层自行切换显示内容
    }

    override fun onBackClick() {
        // 返回由View层处理
    }

    override fun onDestroy() {
        // 清理资源
    }
}
