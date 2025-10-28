package com.example.mymusic.presenter

import android.content.Context
import com.example.mymusic.presenter.ModeSelectionContract.*

/**
 * 模式选择页面的Presenter
 */
class ModeSelectionPresenter(
    private val view: ModeSelectionContract.View,
    private val context: Context
) : ModeSelectionContract.Presenter {

    override fun loadModes() {
        // 加载主要漫游模式
        val modes = listOf(
            ModeItem(
                id = "default",
                name = "默认模式",
                description = "沿着目前喜好继续聆听",
                isSelected = true
            ),
            ModeItem(
                id = "familiar",
                name = "熟悉模式",
                description = "喜欢过的歌曲与相似推荐"
            ),
            ModeItem(
                id = "explore",
                name = "探索模式",
                description = "偏好曲风与潜力好歌"
            ),
            ModeItem(
                id = "puzzle",
                name = "拼图模式",
                description = "边听歌边拼歌曲封面"
            )
        )
        view.showModes(modes)

        // 加载场景模式
        val sceneModes = listOf(
            // 第一行
            SceneModeItem(id = "sad", name = "伤感", icon = "😢"),
            SceneModeItem(id = "exercise", name = "运动", icon = "🏃"),
            SceneModeItem(id = "sleep", name = "助眠", icon = "😴"),
            SceneModeItem(id = "relax", name = "放松", icon = "🎋"),
            // 第二行
            SceneModeItem(id = "happy", name = "欢快", icon = "🎉"),
            SceneModeItem(id = "lyric", name = "抒情", icon = "💝"),
            SceneModeItem(id = "heal", name = "治愈", icon = "🧘"),
            SceneModeItem(id = "focus", name = "专注", icon = "📖"),
            // 第三行
            SceneModeItem(id = "romantic", name = "浪漫情歌", icon = "🌙"),
            SceneModeItem(id = "rnb", name = "R&B", icon = "🎤", isNew = true),
            SceneModeItem(id = "rainy", name = "下雨天", icon = "☔", isNew = true),
            SceneModeItem(id = "gaming", name = "打游戏", icon = "🎮"),
            // 第四行
            SceneModeItem(id = "rap", name = "说唱", icon = "🎙️"),
            SceneModeItem(id = "kpop", name = "K-Pop", icon = "한"),
            SceneModeItem(id = "indie", name = "宝藏原创", icon = "💎"),
            SceneModeItem(id = "electronic", name = "电音", icon = "🎹"),
            // 第五行
            SceneModeItem(id = "travel", name = "出行", icon = "🚌"),
            SceneModeItem(id = "bath", name = "洗澡", icon = "🛁"),
            SceneModeItem(id = "cafe", name = "咖啡馆", icon = "☕"),
            SceneModeItem(id = "shake", name = "摇滚", icon = "🤘"),
            // 第六行
            SceneModeItem(id = "motivation", name = "励志", icon = "💪")
        )
        view.showSceneModes(sceneModes)
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
            "cafe" -> "咖啡馆"
            "shake" -> "摇滚"
            "motivation" -> "励志"
            else -> "伤感"
        }
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
}
