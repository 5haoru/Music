package com.example.mymusic.presenter

/**
 * 模式选择页面的Contract接口
 */
interface ModeSelectionContract {
    interface View {
        fun showModes(modes: List<ModeItem>)
        fun showSceneModes(sceneModes: List<SceneModeItem>)
        fun navigateBack()
        fun onModeSelectedCallback(modeName: String)
    }

    interface Presenter {
        fun loadModes()
        fun onModeSelected(modeId: String)
        fun onSceneModeSelected(sceneModeId: String)
        fun onBackClick()
        fun onSettingsClick()
    }

    /**
     * 主要漫游模式数据类
     */
    data class ModeItem(
        val id: String,
        val name: String,
        val description: String,
        val isSelected: Boolean = false
    )

    /**
     * 场景模式数据类
     */
    data class SceneModeItem(
        val id: String,
        val name: String,
        val icon: String, // 使用emoji或图标名称
        val isNew: Boolean = false
    )
}
