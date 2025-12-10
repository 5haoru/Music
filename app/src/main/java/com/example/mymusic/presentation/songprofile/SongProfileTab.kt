package com.example.mymusic.presentation.songprofile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.mymusic.data.SongProfile
import com.example.mymusic.presentation.songprofile.SongProfileContract
import com.example.mymusic.presentation.songprofile.SongProfilePresenter
import com.example.mymusic.presentation.songprofile.components.*
import com.example.mymusic.utils.AutoTestHelper

/**
 * 歌曲档案页面（三级页面）
 * 从播放定制页面的"查看歌曲百科"进入
 */
@Composable
fun SongProfileTab(
    songId: String,
    onBackClick: () -> Unit = {}
) {
    val context = LocalContext.current

    // 状态
    var songProfile by remember { mutableStateOf<SongProfile?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Presenter
    val presenter = remember {
        SongProfilePresenter(
            object : SongProfileContract.View {
                override fun showSongProfile(profile: SongProfile) {
                    songProfile = profile
                }

                override fun navigateBack() {
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
                    isLoading = false
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
        presenter.loadSongProfile(songId)
    }

    // 更新AutoTest状态
    LaunchedEffect(songId) {
        AutoTestHelper.updateCurrentPage("song_detail")
        AutoTestHelper.updateCurrentSongId(songId)
    }

    DisposableEffect(Unit) {
        onDispose {
            AutoTestHelper.updateCurrentSongId(null)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightBackground)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = TextPrimary
            )
        } else if (songProfile != null) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                SongProfileTopBar(
                    songName = songProfile!!.songName,
                    onBackClick = { presenter.onBackClick() }
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    PersonalDataCard(profile = songProfile!!)

                    Spacer(modifier = Modifier.height(16.dp))

                    SongDetailsSection(
                        profile = songProfile!!,
                        onProductionClick = { presenter.onProductionClick() },
                        onIntroductionClick = { presenter.onIntroductionClick() },
                        onFilmTvClick = { presenter.onFilmTvClick() },
                        onAwardsClick = { presenter.onAwardsClick() },
                        onScoresClick = { presenter.onScoresClick() }
                    )

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}
