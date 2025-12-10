package com.example.mymusic.presentation.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.mymusic.data.PlayerStyle
import com.example.mymusic.data.repository.PlaybackStyleRecordRepository
import com.example.mymusic.data.repository.PlayerStyleRepository
import com.example.mymusic.presentation.player.PlayerContract
import com.example.mymusic.presentation.player.PlayerPresenter
import com.example.mymusic.presentation.player.components.*

/**
 * 播放器样式选择页面Tab（三级页面）
 * 从播放定制页点击"播放器样式"进入
 */
@Composable
fun PlayerTab(
    onBackClick: () -> Unit = {}
) {
    val context = LocalContext.current

    // 状态
    var playerStyles by remember { mutableStateOf<List<PlayerStyle>>(emptyList()) }
    var selectedCategory by remember { mutableStateOf(PlayerStyle.CATEGORY_CLASSIC) }
    var currentStyleId by remember { mutableStateOf("retro_cd") }
    var showConfirmDialog by remember { mutableStateOf(false) }
    var selectedStyle by remember { mutableStateOf<PlayerStyle?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Presenter
    val presenter = remember {
        val playerStyleRepository = PlayerStyleRepository(context)
        val playbackStyleRecordRepository = PlaybackStyleRecordRepository(context)
        PlayerPresenter(
            object : PlayerContract.View {
                override fun showPlayerStyles(styles: List<PlayerStyle>) {
                    playerStyles = styles
                }

                override fun showSelectedStyle(styleId: String) {
                    currentStyleId = styleId
                }

                override fun showConfirmDialog(style: PlayerStyle) {
                    selectedStyle = style
                    showConfirmDialog = true
                }

                override fun navigateBack() {
                    onBackClick()
                }

                override fun showToast(message: String) {
                    android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT).show()
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
            playerStyleRepository,
            playbackStyleRecordRepository
        )
    }

    // 加载播放器样式数据
    LaunchedEffect(Unit) {
        presenter.loadPlayerStyles()
        presenter.onTabSelected(selectedCategory)
    }

    // 确认对话框
    if (showConfirmDialog && selectedStyle != null) {
        ConfirmStyleDialog(
            style = selectedStyle!!,
            onConfirm = {
                presenter.confirmStyleChange(selectedStyle!!)
                showConfirmDialog = false
            },
            onDismiss = {
                showConfirmDialog = false
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Color.Black
            )
        } else {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // 顶部栏
                PlayerTopBar(
                    onBackClick = { presenter.onBackClick() }
                )

                // 选项卡
                PlayerTabRow(
                    selectedCategory = selectedCategory,
                    onTabSelected = { category ->
                        selectedCategory = category
                        presenter.onTabSelected(category)
                    }
                )

                // 样式列表区域
                PlayerStyleList(
                    category = selectedCategory,
                    styles = playerStyles,
                    currentStyleId = currentStyleId,
                    onStyleClick = { style ->
                        presenter.onStyleSelected(style)
                    }
                )
            }
        }
    }
}
