package com.example.mymusic.presentation.listenrecognize

import com.example.mymusic.base.BasePresenter
import com.example.mymusic.base.BaseView
import com.example.mymusic.data.Song
import com.example.mymusic.data.RecognitionRecord

/**
 * 听歌识曲页面契约接口
 */
interface ListenRecognizeContract {
    interface View : BaseView {
        fun showRecordingStatus(isRecording: Boolean)
        fun updateRecordingDuration(seconds: Int)
        fun showFloatingWindowStatus(isEnabled: Boolean)
        fun showRecognizeMode(isListenMode: Boolean)
        fun showRecognizedSong(song: Song?)
        fun showRecognitionHistory(history: List<RecognitionRecord>)
        fun recognizingSong(isRecognizing: Boolean)
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
