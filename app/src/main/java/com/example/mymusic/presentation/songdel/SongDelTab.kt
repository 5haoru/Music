package com.example.mymusic.presentation.songdel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.mymusic.data.Song
import com.example.mymusic.presentation.songdel.SongDelContract
import com.example.mymusic.presentation.songdel.SongDelPresenter
import com.example.mymusic.presentation.songdel.components.*

// 浅色主题颜色
private val LightBackground = Color(0xFFFFFFFF)
/**
 * 歌曲删除操作页面（四级页面）
 * 从歌单歌曲列表项的三个点图标进入
 */
@Composable
fun SongDelTab(
    songId: String,
    playlistId: String,
    onBackClick: () -> Unit = {}
) {
    val context = LocalContext.current

    // 状《
    var currentSong by remember { mutableStateOf<Song?>(null) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Presenter
    val presenter = remember {
        SongDelPresenter(
            object : SongDelContract.View {
                override fun showSongInfo(song: Song) {
                    currentSong = song
                }

                override fun showDeleteConfirmDialog() {
                    showDeleteConfirmDialog = true
                }

                override fun close() {
                    onBackClick()
                }

                override fun showLoading() {
                    isLoading = true
                }

                override fun hideLoading() {
                    isLoading = false
                }

                override fun showError(message: String) {
                    errorMessage = message
                    android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT).show()
                }

                override fun showSuccess(message: String) {
                    android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT).show()
                }
            },
            context
        )
    }

    // 加载数据
    LaunchedEffect(songId) {
        presenter.loadSongInfo(songId, playlistId)
    }

    // 删除确认对话《
    if (showDeleteConfirmDialog && currentSong != null) {
        DeleteConfirmDialog(
            song = currentSong!!,
            onConfirm = {
                showDeleteConfirmDialog = false
                presenter.confirmDelete()
            },
            onDismiss = {
                showDeleteConfirmDialog = false
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightBackground)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Color.Black
            )
        } else if (currentSong != null) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // 顶部《
                SongDelTopBar(
                    onBackClick = { presenter.onBackClick() }
                )

                // 滚动内容
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    // 歌曲信息卡片
                    SongInfoCard(song = currentSong!!)

                    Spacer(modifier = Modifier.height(16.dp))

                    // 操作按钮区域
                    ActionButtonsSection(
                        commentCount = "364197",
                        onCommentClick = { presenter.onCommentClick() },
                        onShareClick = { presenter.onShareClick() },
                        onPurchaseClick = { presenter.onPurchaseClick() }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Divider(color = Color(0xFFE0E0E0))

                    Spacer(modifier = Modifier.height(16.dp))

                    // 详细信息区域
                    DetailInfoSection(song = currentSong!!)

                    Spacer(modifier = Modifier.height(16.dp))

                    Divider(color = Color(0xFFE0E0E0))

                    Spacer(modifier = Modifier.height(16.dp))

                    // 扩展功能区域
                    ExtendedFunctionsSection(
                        onRingtoneClick = { presenter.onRingtoneClick() },
                        onGiftCardClick = { presenter.onGiftCardClick() },
                        onDeleteClick = { presenter.onDeleteClick() }
                    )

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}
