package com.example.mymusic.presentation.modeselection

import com.example.mymusic.base.BaseView
import com.example.mymusic.presentation.modeselection.ModeSelectionContract
import com.example.mymusic.data.repository.PlayerStyleRepository
import com.example.mymusic.presentation.modeselection.ModeSelectionContract.*
import com.example.mymusic.utils.AutoTestHelper

/**
 * 模式选择页面的Presenter
 */
class ModeSelectionPresenter(
    private val view: ModeSelectionContract.View,
    private val playerStyleRepository: PlayerStyleRepository
) : ModeSelectionContract.Presenter {

    override fun loadModes() {
        try {
            val playerStyles = playerStyleRepository.getPlayerStyles()
            val modes = playerStyles.map {
                ModeItem(
                    id = it.styleId,
                    name = it.styleName,
                    description = it.description,
                    isSelected = it.isInUse
                )
            }
            view.showModes(modes)

            val sceneModes = listOf(
                SceneModeItem(id = "sad", name = "伤感", icon = "😢"),
                SceneModeItem(id = "exercise", name = "运动", icon = "🏃"),
                SceneModeItem(id = "sleep", name = "助眠", icon = "😴"),
                SceneModeItem(id = "relax", name = "放松", icon = "🎋"),
                SceneModeItem(id = "happy", name = "欢快", icon = "🎉"),
                SceneModeItem(id = "lyric", name = "抒情", icon = "💝"),
                SceneModeItem(id = "heal", name = "治愈", icon = "🧘"),
                SceneModeItem(id = "focus", name = "专注", icon = "📖"),
                SceneModeItem(id = "romantic", name = "浪漫情歌", icon = "🌙"),
                SceneModeItem(id = "rnb", name = "R&B", icon = "🎤", isNew = true),
                SceneModeItem(id = "rainy", name = "下雨天", icon = "🌧️", isNew = true),
                SceneModeItem(id = "gaming", name = "打游戏", icon = "🎮"),
                SceneModeItem(id = "rap", name = "说唱", icon = "🎙️"),
                SceneModeItem(id = "kpop", name = "K-Pop", icon = "🇰🇷"),
                SceneModeItem(id = "indie", name = "宝藏原创", icon = "💎"),
                SceneModeItem(id = "electronic", name = "电音", icon = "🎹"),
                SceneModeItem(id = "travel", name = "出行", icon = "🚌"),
                SceneModeItem(id = "bath", name = "洗澡", icon = "🛁"),
                SceneModeItem(id = "cafe", name = "咖啡厅", icon = "☕️"),
                SceneModeItem(id = "shake", name = "摇滚", icon = "🤘"),
                SceneModeItem(id = "motivation", name = "励志", icon = "💪")
            )
            view.showSceneModes(sceneModes)
        } catch (e: Exception) {
            view.showError("加载模式失败: ${e.message}")
        }
    }

    override fun onModeSelected(modeId: String) {
        // 根据模式ID找到对应的模式名称
        val modeName = when (modeId) {
            "default" -> "默认模式"
            "familiar" -> "熟悉模式"
            "explore" -> "探索模式"
            "puzzle" -> "拼图模式"
            else -> "默认模式"
        }
        // 回调通知View层模式已选择
        view.onModeSelectedCallback(modeName)
        // 返回上一页
        view.navigateBack()
    }

    override fun onSceneModeSelected(sceneModeId: String) {
        // 根据场景模式ID找到对应的名称
        val sceneName = when (sceneModeId) {
            "sad" -> "伤感"
            "exercise" -> "运动"
            "sleep" -> "助眠"
            "relax" -> "放松"
            "happy" -> "欢快"
            "lyric" -> "抒情"
            "heal" -> "治愈"
            "focus" -> "专注"
            "romantic" -> "浪漫情歌"
            "rnb" -> "R&B"
            "rainy" -> "下雨天"
            "gaming" -> "打游戏"
            "rap" -> "说唱"
            "kpop" -> "K-Pop"
            "indie" -> "宝藏原创"
            "electronic" -> "电音"
            "travel" -> "出行"
            "bath" -> "洗澡"
            "cafe" -> "咖啡厅"
            "shake" -> "摇滚"
            "motivation" -> "励志"
            else -> "伤感"
        }

        // 记录漫游场景设置
        AutoTestHelper.updateStrollMode(sceneName, true)

        // 回调通知View层场景模式已选择
        view.onModeSelectedCallback(sceneName)
        // 返回上一页
        view.navigateBack()
    }

    override fun onBackClick() {
        view.navigateBack()
    }

    override fun onSettingsClick() {
        // TODO: 打开设置页面
    }

    override fun onDestroy() {
        
    }
}
