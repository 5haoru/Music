package com.example.mymusic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.mymusic.presenter.MainContract
import com.example.mymusic.presenter.MainPresenter
import com.example.mymusic.ui.theme.MyMusicTheme
import com.example.mymusic.view.*

class MainActivity : ComponentActivity(), MainContract.View {

    private lateinit var presenter: MainPresenter
    private var selectedTab by mutableStateOf(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = MainPresenter(this, this)

        enableEdgeToEdge()
        setContent {
            MyMusicTheme {
                MainScreen()
            }
        }
    }

    @Composable
    fun MainScreen() {
        var currentTab by remember { mutableStateOf(0) }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                NavigationBar {
                    bottomNavItems.forEachIndexed { index, item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                            selected = currentTab == index,
                            onClick = {
                                currentTab = index
                                presenter.onTabSelected(index)
                            }
                        )
                    }
                }
            }
        ) { innerPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (currentTab) {
                    0 -> RecommendTab()
                    1 -> StrollTab()
                    2 -> PlayTab()
                    3 -> MeTab()
                }
            }
        }
    }

    override fun switchTab(index: Int) {
        selectedTab = index
    }

    override fun showLoading() {
        // 显示加载中
    }

    override fun hideLoading() {
        // 隐藏加载中
    }

    override fun showError(message: String) {
        // 显示错误信息
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    private data class BottomNavItem(
        val label: String,
        val icon: ImageVector
    )

    private val bottomNavItems = listOf(
        BottomNavItem("推荐", Icons.Default.Home),
        BottomNavItem("漫游", Icons.Default.Refresh),
        BottomNavItem("播放", Icons.Default.PlayArrow),
        BottomNavItem("我的", Icons.Default.Person)
    )
}
