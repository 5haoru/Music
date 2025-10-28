package com.example.mymusic.presenter

import android.content.Context

/**
 * 听歌识曲页面Presenter
 */
class ListenRecognizePresenter(
    private val view: ListenRecognizeContract.View,
    private val context: Context
) : ListenRecognizeContract.Presenter {

    private var isRecording = false
    private var recordingDuration = 0
    private var isFloatingWindowEnabled = false
    private var isListenMode = true // true: 听歌识曲, false: 哼唱识曲

    override fun loadData() {
        view.showLoading()
        try {
            // 初始化状态
            view.showFloatingWindowStatus(isFloatingWindowEnabled)
            view.showRecognizeMode(isListenMode)

            // 进入页面自动开始录音
            startRecording()

            view.hideLoading()
        } catch (e: Exception) {
            view.hideLoading()
            view.showError("加载失败: ${e.message}")
        }
    }

    override fun onRecordButtonClick() {
        if (isRecording) {
            stopRecording()
        } else {
            startRecording()
        }
    }

    override fun onFloatingWindowToggle(enabled: Boolean) {
        isFloatingWindowEnabled = enabled
        view.showFloatingWindowStatus(enabled)
    }

    override fun onModeSwitch(isListenMode: Boolean) {
        this.isListenMode = isListenMode
        view.showRecognizeMode(isListenMode)

        // 切换模式时重新开始录音
        if (isRecording) {
            stopRecording()
        }
        startRecording()
    }

    override fun startRecording() {
        isRecording = true
        recordingDuration = 0
        view.showRecordingStatus(true)
        view.updateRecordingDuration(recordingDuration)
    }

    override fun stopRecording() {
        isRecording = false
        recordingDuration = 0
        view.showRecordingStatus(false)
        view.updateRecordingDuration(recordingDuration)
    }

    override fun onDestroy() {
        stopRecording()
    }

    /**
     * 更新录音时长（由View层的计时器调用）
     */
    fun updateDuration(seconds: Int) {
        recordingDuration = seconds
        view.updateRecordingDuration(seconds)
    }
}
