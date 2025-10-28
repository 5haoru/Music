package com.example.mymusic.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mymusic.presenter.ListenRecognizeContract
import com.example.mymusic.presenter.ListenRecognizePresenter
import kotlinx.coroutines.delay

/**
 * 听歌识曲页面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListenRecognizeTab(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    // 状态
    var isRecording by remember { mutableStateOf(false) }
    var recordingDuration by remember { mutableIntStateOf(0) }
    var isFloatingWindowEnabled by remember { mutableStateOf(false) }
    var isListenMode by remember { mutableStateOf(true) } // true: 听歌识曲, false: 哼唱识曲
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Presenter
    val presenter = remember {
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
                }
            },
            context
        )
    }

    // 加载数据
    LaunchedEffect(Unit) {
        presenter.loadData()
    }

    // 录音计时器
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
                // 功能提示条
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ListenRecognizeTopBar(
    onBackClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "听歌识曲",
                style = MaterialTheme.typography.titleLarge
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "返回"
                )
            }
        },
        actions = {
            IconButton(onClick = { /* TODO: 历史记录 */ }) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = "历史"
                )
            }
            IconButton(onClick = { /* TODO: 更多 */ }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "更多"
                )
            }
        }
    )
}

@Composable
private fun FloatingWindowTipBar(
    isEnabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "开启桌面悬浮窗，边刷视频边识曲",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Switch(
                checked = isEnabled,
                onCheckedChange = onToggle
            )
        }
    }
}

@Composable
private fun RecordingArea(
    isRecording: Boolean,
    duration: Int,
    onRecordClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 录音按钮（带光晕效果）
        Box(
            contentAlignment = Alignment.Center
        ) {
            // 外层粉色光晕
            Box(
                modifier = Modifier
                    .size(300.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFE0E0).copy(alpha = 0.3f))
            )

            // 红色录音按钮
            Surface(
                modifier = Modifier
                    .size(200.dp)
                    .clickable(onClick = onRecordClick),
                shape = CircleShape,
                color = Color(0xFFFF4444)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "麦克风",
                        modifier = Modifier.size(80.dp),
                        tint = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 状态文字
        if (isRecording) {
            Text(
                text = "录音中 (${duration}s)",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "点击停止识曲",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            Text(
                text = "点击开始识曲",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun ModeSelector(
    isListenMode: Boolean,
    onModeChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        // 听歌识曲按钮
        Surface(
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .clickable { onModeChange(true) },
            shape = RoundedCornerShape(24.dp),
            color = if (isListenMode) MaterialTheme.colorScheme.primary else Color.Transparent
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "听歌识曲",
                    color = if (isListenMode) Color.White else MaterialTheme.colorScheme.onSurface,
                    fontWeight = if (isListenMode) FontWeight.Bold else FontWeight.Normal
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // 哼唱识曲按钮
        Surface(
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .clickable { onModeChange(false) },
            shape = RoundedCornerShape(24.dp),
            color = if (!isListenMode) MaterialTheme.colorScheme.primary else Color.Transparent
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "哼唱识曲",
                    color = if (!isListenMode) Color.White else MaterialTheme.colorScheme.onSurface,
                    fontWeight = if (!isListenMode) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}
