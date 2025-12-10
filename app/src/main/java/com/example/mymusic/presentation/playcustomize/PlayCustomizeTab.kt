package com.example.mymusic.presentation.playcustomize

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.mymusic.data.Song
import com.example.mymusic.data.repository.RepositoryProvider
import com.example.mymusic.presentation.playcustomize.components.*

/**
 * 播放定制页面（浮层Overlay）
 * 从播放页面底部"更多"按钮进入
 */
@Composable
fun PlayCustomizeTab(
    song: Song,
    onCloseClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
    onNavigateToPlayerStyle: () -> Unit = {},
    onNavigateToSongProfile: () -> Unit = {},
    onNavigateToCollectSong: () -> Unit = {}
) {
    val context = LocalContext.current

    // 状态
    var currentSong by remember { mutableStateOf<Song?>(null) }
    var collected by remember { mutableStateOf(false) }
    var downloaded by remember { mutableStateOf(false) }
    var followed by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    // Presenter
    val presenter = remember {
        RepositoryProvider.initialize(context)
        PlayCustomizePresenter(
            object : PlayCustomizeContract.View {
                override fun showSong(song: Song) {
                    currentSong = song
                }

                override fun updateCollectionState(isCollected: Boolean) {
                    collected = isCollected
                }

                override fun updateDownloadState(isDownloaded: Boolean) {
                    downloaded = isDownloaded
                }

                override fun updateFollowState(isFollowed: Boolean) {
                    followed = isFollowed
                }

                override fun showSuccess(message: String) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }

                override fun close() {
                    onCloseClick()
                }

                override fun navigateToPlayerStyle() {
                    onNavigateToPlayerStyle()
                }

                override fun navigateToSongProfile() {
                    onNavigateToSongProfile()
                }

                override fun navigateToCollectSong() {
                    onNavigateToCollectSong()
                }

                override fun showLoading() {
                    isLoading = true
                }

                override fun hideLoading() {
                    isLoading = false
                }

                override fun showError(message: String) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            },
            RepositoryProvider.getSongRepository(),
            RepositoryProvider.getDownloadRecordRepository(),
            RepositoryProvider.getArtistFollowRecordRepository()
        )
    }

    // 加载数据
    LaunchedEffect(song.songId) {
        presenter.loadSong(song.songId)
    }

    // 背景遮罩 + 内容卡片
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f))
            .clickable { onCloseClick() }, // 点击背景关闭
        contentAlignment = Alignment.BottomCenter
    ) {
        // 内容卡片（点击不关闭）
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp)
                .clickable(enabled = false) {}, // 阻止点击事件传递到背景
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // 顶部歌曲信息区域
                SongInfoSection(song = song)

                Spacer(modifier = Modifier.height(24.dp))

                // 第一层：核心功能按钮
                CoreFunctionsRow(
                    isCollected = collected,
                    isDownloaded = downloaded,
                    onCollectionClick = { presenter.onCollectionClick() },
                    onDownloadClick = { presenter.onDownloadClick() },
                    onShareClick = {
                        onCloseClick()
                        onShareClick()
                    },
                    onListenTogetherClick = { presenter.onListenTogetherClick() }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 第二层：详细信息
                DetailInfoSection(
                    song = song,
                    isFollowed = followed,
                    onFollowClick = { presenter.onFollowClick() }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Divider()

                Spacer(modifier = Modifier.height(16.dp))

                // 第三层：扩展功能
                ExtendedFunctionsSection(
                    onEncyclopediaClick = { presenter.onSongEncyclopediaClick() },
                    onSimilarStrollClick = { presenter.onSimilarStrollClick() },
                    onPurchaseClick = { presenter.onPurchaseClick() }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Divider()

                Spacer(modifier = Modifier.height(16.dp))

                // 第四层：播放设置
                PlaybackSettingsSection(
                    onQualityClick = { presenter.onQualityClick() },
                    onAudioEffectClick = { presenter.onAudioEffectClick() },
                    onPlayerStyleClick = { presenter.onPlayerStyleClick() }
                )

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}