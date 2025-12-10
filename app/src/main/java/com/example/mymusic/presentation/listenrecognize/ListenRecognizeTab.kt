package com.example.mymusic.presentation.listenrecognize

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.mymusic.data.RecognitionRecord
import com.example.mymusic.data.Song
import com.example.mymusic.data.repository.RecognitionHistoryRepository
import com.example.mymusic.data.repository.SongRepository
import com.example.mymusic.presentation.listenrecognize.components.*
import kotlinx.coroutines.delay

/**
 * 听歌识曲页面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListenRecognizeTab(
    onBackClick: () -> Unit,
    onNavigateToPlay: (String) -> Unit = {} // Add navigation to play page
) {
    val context = LocalContext.current

    // 状�?
    var isRecording by remember { mutableStateOf(false) }
    var recordingDuration by remember { mutableIntStateOf(0) }
    var isFloatingWindowEnabled by remember { mutableStateOf(false) }
    var isListenMode by remember { mutableStateOf(true) } // true: 听歌识曲, false: 哼唱识曲
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var recognizedSong by remember { mutableStateOf<Song?>(null) }
    var recognitionHistory by remember { mutableStateOf<List<RecognitionRecord>>(emptyList()) }

    // Presenter
    val presenter = remember {
        val songRepository = SongRepository(context)
        val recognitionHistoryRepository = RecognitionHistoryRepository(context)
        ListenRecognizePresenter(
            object : ListenRecognizeContract.View {
                override fun showRecordingStatus(recording: Boolean) {
                    isRecording = recording
                }

                override fun updateRecordingDuration(seconds: Int) {
                    recordingDuration = seconds
                }

                override fun showFloatingWindowStatus(enabled: Boolean) {
                    isFloatingWindowEnabled = enabled
                }

                override fun showRecognizeMode(listenMode: Boolean) {
                    isListenMode = listenMode
                }

                override fun showLoading() {
                    isLoading = true
                }

                override fun hideLoading() {
                    isLoading = false
                }

                override fun showError(message: String) {
                    errorMessage = message
                    isLoading = false
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }

                override fun showSuccess(message: String) {
                    android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT).show()
                }

                override fun showRecognizedSong(song: Song?) {
                    recognizedSong = song
                    if (song != null) {
                        // Optionally navigate to play after recognition
                        onNavigateToPlay(song.songId)
                    }
                }

                override fun showRecognitionHistory(history: List<RecognitionRecord>) {
                    recognitionHistory = history
                }

                override fun recognizingSong(isRecognizing: Boolean) {
                    // TODO: Show a specific UI state for recognizing
                }
            },
            songRepository,
            recognitionHistoryRepository
        )
    }

    // 加载数据
    LaunchedEffect(Unit) {
        presenter.loadData()
    }

    // 录音计时�?
    LaunchedEffect(isRecording) {
        if (isRecording) {
            while (isRecording) {
                delay(1000)
                recordingDuration++
                presenter.updateDuration(recordingDuration)
            }
        }
    }

    Scaffold(
        topBar = {
            ListenRecognizeTopBar(
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (errorMessage != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(text = errorMessage ?: "")
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // 功能提示�?
                FloatingWindowTipBar(
                    isEnabled = isFloatingWindowEnabled,
                    onToggle = { presenter.onFloatingWindowToggle(it) }
                )

                // 中心录音区域
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    RecordingArea(
                        isRecording = isRecording,
                        duration = recordingDuration,
                        onRecordClick = { presenter.onRecordButtonClick() }
                    )
                }

                // 底部模式切换
                ModeSelector(
                    isListenMode = isListenMode,
                    onModeChange = { presenter.onModeSwitch(it) }
                )
            }
        }
    }
}
