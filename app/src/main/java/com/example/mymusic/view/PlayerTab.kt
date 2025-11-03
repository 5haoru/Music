package com.example.mymusic.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mymusic.data.PlayerStyle
import com.example.mymusic.presenter.PlayerContract
import com.example.mymusic.presenter.PlayerPresenter

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
            },
            context
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

/**
 * 顶部栏
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlayerTopBar(onBackClick: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = "播放器样式",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "返回",
                    tint = Color.Black
                )
            }
        },
        actions = {
            IconButton(onClick = { /* TODO: 自定义功能 */ }) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "自定义",
                    tint = Color.Black
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White
        )
    )
}

/**
 * 选项卡行
 */
@Composable
private fun PlayerTabRow(
    selectedCategory: String,
    onTabSelected: (String) -> Unit
) {
    val categories = listOf(
        PlayerStyle.CATEGORY_CLASSIC,
        PlayerStyle.CATEGORY_RETRO,
        PlayerStyle.CATEGORY_CREATIVE,
        PlayerStyle.CATEGORY_ARTIST,
        PlayerStyle.CATEGORY_COLLAB
    )

    ScrollableTabRow(
        selectedTabIndex = categories.indexOf(selectedCategory),
        containerColor = Color.White,
        contentColor = Color.Black,
        edgePadding = 16.dp
    ) {
        categories.forEach { category ->
            Tab(
                selected = selectedCategory == category,
                onClick = { onTabSelected(category) },
                text = {
                    Text(
                        text = category,
                        fontSize = 15.sp,
                        fontWeight = if (selectedCategory == category) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedCategory == category) Color.Black else Color.Gray
                    )
                }
            )
        }
    }
}

/**
 * 播放器样式列表
 */
@Composable
private fun PlayerStyleList(
    category: String,
    styles: List<PlayerStyle>,
    currentStyleId: String,
    onStyleClick: (PlayerStyle) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(top = 16.dp)
    ) {
        // 类别标题
        Text(
            text = category,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        // 横向滑动的样式卡片列表
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(styles) { style ->
                PlayerStyleCard(
                    style = style,
                    isInUse = style.styleId == currentStyleId,
                    onClick = { onStyleClick(style) }
                )
            }
        }
    }
}

/**
 * 播放器样式卡片
 */
@Composable
private fun PlayerStyleCard(
    style: PlayerStyle,
    isInUse: Boolean,
    onClick: () -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .width(240.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 样式预览图
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(380.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data("file:///android_asset/${style.imageUrl}")
                    .crossfade(true)
                    .build(),
                contentDescription = style.styleName,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            // 使用中标签
            if (isInUse) {
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp),
                    shape = RoundedCornerShape(4.dp),
                    color = Color(0xFF4CAF50)
                ) {
                    Text(
                        text = "使用中",
                        fontSize = 12.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 样式名称
        Text(
            text = style.styleName,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        // 样式描述
        if (style.description.isNotEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = style.description,
                fontSize = 13.sp,
                color = Color.Gray,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * 确认样式对话框
 */
@Composable
private fun ConfirmStyleDialog(
    style: PlayerStyle,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "你确定使用该样式吗？",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "样式：${style.styleName}",
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // 取消按钮
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("取消")
                    }

                    // 确定按钮
                    Button(
                        onClick = onConfirm,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF6B6B)
                        )
                    ) {
                        Text("确定")
                    }
                }
            }
        }
    }
}
