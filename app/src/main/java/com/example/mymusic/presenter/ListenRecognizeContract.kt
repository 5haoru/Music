package com.example.mymusic.presenter

/**
 * 听歌识曲页面契约接口
 */
interface ListenRecognizeContract {
    interface View : BaseView {
        fun showRecordingStatus(isRecording: Boolean)
        fun updateRecordingDuration(seconds: Int)
        fun showFloatingWindowStatus(isEnabled: Boolean)
        fun showRecognizeMode(isListenMode: Boolean)
    }

    interface Presenter : BasePresenter {
        fun loadData()
        fun onRecordButtonClick()
        fun onFloatingWindowToggle(enabled: Boolean)
        fun onModeSwitch(isListenMode: Boolean)
        fun startRecording()
        fun stopRecording()
    }
}
