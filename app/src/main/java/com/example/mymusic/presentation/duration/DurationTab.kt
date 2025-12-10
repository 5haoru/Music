package com.example.mymusic.presentation.duration

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.mymusic.data.ListeningDuration
import com.example.mymusic.data.repository.RepositoryProvider
import com.example.mymusic.presentation.duration.DurationContract
import com.example.mymusic.presentation.duration.DurationPresenter
import com.example.mymusic.presentation.duration.components.*

/**
 * 听歌时长页面Tab（二级页面）
 * 从我的页面的"时长"入口进入
 * 包含周、月、年三种视图
 */
@Composable
fun DurationTab(
    onBackClick: () -> Unit = {}
) {
    val context = LocalContext.current

    // 状�?
    var durationData by remember { mutableStateOf<ListeningDuration?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var selectedTab by remember { mutableStateOf(0) } // 0:�? 1:�? 2:�?

    // Presenter
    val presenter = remember {
        RepositoryProvider.initialize(context)
        DurationPresenter(
            object : DurationContract.View {
                override fun showDurationData(data: ListeningDuration) {
                    durationData = data
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

                override fun showSuccess(message: String) {
                    android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT).show()
                }
            },
            RepositoryProvider.getDurationRepository()
        )
    }

    // 加载数据
    LaunchedEffect(Unit) {
        presenter.loadData()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // 顶部导航栏
        DurationTopBar(
            selectedTab = selectedTab,
            onTabSelected = {
                selectedTab = it
                presenter.onTabSelected(it)
            },
            onBackClick = onBackClick
        )

        // 内容区域
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (errorMessage != null) {
                Text(
                    text = errorMessage ?: "",
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.error
                )
            } else if (durationData != null) {
                when (selectedTab) {
                    0 -> WeeklyView(durationData!!.weekly)
                    1 -> MonthlyView(durationData!!.monthly)
                    2 -> YearlyView(durationData!!.yearly.years)
                }
            }
        }
    }
}
