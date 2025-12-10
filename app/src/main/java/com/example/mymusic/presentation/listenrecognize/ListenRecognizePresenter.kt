package com.example.mymusic.presentation.listenrecognize

import com.example.mymusic.data.RecognitionRecord
import com.example.mymusic.data.repository.RecognitionHistoryRepository
import com.example.mymusic.data.repository.SongRepository

class ListenRecognizePresenter(
    private val view: ListenRecognizeContract.View,
    private val songRepository: SongRepository,
    private val recognitionHistoryRepository: RecognitionHistoryRepository
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

            // 加载识别历史
            val history = recognitionHistoryRepository.getRecognitionHistory()
            view.showRecognitionHistory(history)

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

    override fun startRecording() {
        isRecording = true
        recordingDuration = 0
        view.showRecordingStatus(true)
        view.updateRecordingDuration(recordingDuration)
        // 模拟识别过程
        view.recognizingSong(true)
    }

    override fun stopRecording() {
        isRecording = false
        recordingDuration = 0
        view.showRecordingStatus(false)
        view.updateRecordingDuration(recordingDuration)
        view.recognizingSong(false)

        // 模拟识别结果并保存到历史记录
        val songs = songRepository.getAllSongs()
        if (songs.isNotEmpty()) {
            val recognizedSong = songs.random()
            val record = RecognitionRecord(
                recordId = System.currentTimeMillis().toString(),
                songName = recognizedSong.songName,
                artist = recognizedSong.artist,
                coverUrl = recognizedSong.coverUrl,
                recognitionTime = System.currentTimeMillis()
            )
            recognitionHistoryRepository.addRecognitionRecord(record)
            view.showRecognizedSong(recognizedSong)
        }

        // 重新加载历史记录
        val history = recognitionHistoryRepository.getRecognitionHistory()
        view.showRecognitionHistory(history)
    }

    override fun onFloatingWindowToggle(enabled: Boolean) {
        isFloatingWindowEnabled = enabled
        view.showFloatingWindowStatus(isFloatingWindowEnabled)
    }

    override fun onModeSwitch(isListenMode: Boolean) {
        this.isListenMode = isListenMode
        view.showRecognizeMode(this.isListenMode)
    }

    override fun onDestroy() {
        stopRecording()
    }

    fun updateDuration(seconds: Int) {
        recordingDuration = seconds
        view.updateRecordingDuration(seconds)
    }
}
