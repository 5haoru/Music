package com.example.mymusic.presentation.modeselection

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mymusic.data.repository.RepositoryProvider
import com.example.mymusic.presentation.modeselection.ModeSelectionContract
import com.example.mymusic.presentation.modeselection.ModeSelectionContract.*
import com.example.mymusic.presentation.modeselection.ModeSelectionPresenter

/**
 * 模式选择页面
 */
@Composable
fun ModeSelectionTab(
    currentMode: String = "伤感",
    onBackClick: () -> Unit,
    onModeSelected: (String) -> Unit
) {
    val context = LocalContext.current

    // 状态
    var modes by remember { mutableStateOf<List<ModeItem>>(emptyList()) }
    var sceneModes by remember { mutableStateOf<List<SceneModeItem>>(emptyList()) }

    // Presenter
    val presenter = remember {
        RepositoryProvider.initialize(context)
        ModeSelectionPresenter(
            object : ModeSelectionContract.View {
                override fun showModes(modeList: List<ModeItem>) {
                    modes = modeList
                }

                override fun showSceneModes(sceneModeList: List<SceneModeItem>) {
                    sceneModes = sceneModeList
                }

                override fun navigateBack() {
                    onBackClick()
                }

                override fun onModeSelectedCallback(modeName: String) {
                    onModeSelected(modeName)
                }

                override fun showLoading() {
                }

                override fun hideLoading() {
                }

                override fun showError(message: String) {
                }

                override fun showSuccess(message: String) {
                    android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT).show()
                }
            },
            RepositoryProvider.getPlayerStyleRepository()
        )
    }

    // 加载数据
    LaunchedEffect(Unit) {
        presenter.loadModes()
    }

    // 主背景：深红色渐变
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF8B3A3A), // 深红色
                        Color(0xFF5C2626)  // 更深的红色
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // 顶部栏
            ModeSelectionTopBar(
                currentMode = currentMode,
                onBackClick = { presenter.onBackClick() },
                onSettingsClick = { presenter.onSettingsClick() }
            )

            // 滚动内容区
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 主要模式卡片列表
                items(modes) { mode ->
                    ModeCard(
                        mode = mode,
                        onClick = { presenter.onModeSelected(mode.id) }
                    )
                }

                // 场景模式标题
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "场景模式",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                // 场景模式网格
                item {
                    SceneModeGrid(
                        sceneModes = sceneModes,
                        onSceneModeClick = { presenter.onSceneModeSelected(it.id) }
                    )
                }
            }
        }
    }
}

/**
 * 顶部栏
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ModeSelectionTopBar(
    currentMode: String,
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "私人漫游 · $currentMode",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "返回",
                    tint = Color.White
                )
            }
        },
        actions = {
            TextButton(onClick = onSettingsClick) {
                Text(
                    text = "设置",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )
}

/**
 * 模式卡片
 */
@Composable
private fun ModeCard(
    mode: ModeItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.15f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = mode.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 22.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = mode.description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp
            )
        }
    }
}

/**
 * 场景模式网格
 */
@Composable
private fun SceneModeGrid(
    sceneModes: List<SceneModeItem>,
    onSceneModeClick: (SceneModeItem) -> Unit
) {
    val rows = (sceneModes.size + 3) / 4 // 计算行数（向上取整）
    val gridHeight = (rows * 100).dp // 每行100dp高度

    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = Modifier.height(gridHeight),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        userScrollEnabled = false // 禁用内部滚动
    ) {
        items(sceneModes) { sceneMode ->
            SceneModeItem(
                sceneMode = sceneMode,
                onClick = { onSceneModeClick(sceneMode) }
            )
        }
    }
}

/**
 * 场景模式单项
 */
@Composable
private fun SceneModeItem(
    sceneMode: SceneModeItem,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(80.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 图标区域
            Box(
                contentAlignment = Alignment.TopEnd
            ) {
                Text(
                    text = sceneMode.icon,
                    fontSize = 32.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                // NEW标签
                if (sceneMode.isNew) {
                    Text(
                        text = "NEW",
                        fontSize = 8.sp,
                        color = Color.White.copy(alpha = 0.6f),
                        modifier = Modifier
                            .padding(end = 2.dp)
                            .alpha(0.8f)
                    )
                }
            }

            // 文字
            Text(
                text = sceneMode.name,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}
