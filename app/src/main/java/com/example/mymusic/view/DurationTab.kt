package com.example.mymusic.view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mymusic.data.DayCheckin
import com.example.mymusic.data.ListeningDuration
import com.example.mymusic.data.YearData
import com.example.mymusic.presenter.DurationContract
import com.example.mymusic.presenter.DurationPresenter
import kotlin.math.max

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

    // 状态
    var durationData by remember { mutableStateOf<ListeningDuration?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var selectedTab by remember { mutableStateOf(0) } // 0:周, 1:月, 2:年

    // Presenter
    val presenter = remember {
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
            },
            context
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

/**
 * 顶部导航栏
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DurationTopBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    onBackClick: () -> Unit
) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 周/月/年切换标签
                TabSelector(
                    tabs = listOf("周", "月", "年"),
                    selectedTab = selectedTab,
                    onTabSelected = onTabSelected
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "返回"
                )
            }
        },
        actions = {
            Box {
                TextButton(onClick = { /* TODO: 显示规则 */ }) {
                    Text(text = "规则", fontSize = 16.sp)
                }
                // 红点提示
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .offset(x = 38.dp, y = 8.dp)
                        .clip(CircleShape)
                        .background(Color.Red)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}

/**
 * 标签选择器
 */
@Composable
private fun TabSelector(
    tabs: List<String>,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        tabs.forEachIndexed { index, title ->
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = if (selectedTab == index) FontWeight.Medium else FontWeight.Normal,
                color = if (selectedTab == index)
                    MaterialTheme.colorScheme.onSurface
                else
                    MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        if (selectedTab == index)
                            MaterialTheme.colorScheme.surface
                        else
                            Color.Transparent
                    )
                    .clickable { onTabSelected(index) }
                    .padding(horizontal = 24.dp, vertical = 8.dp)
            )
        }
    }
}

/**
 * 周视图
 */
@Composable
private fun WeeklyView(data: com.example.mymusic.data.WeeklyData) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        // 时间范围
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "上一周",
                    modifier = Modifier.clickable { /* TODO: 切换到上一周 */ }
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "${data.startDate} - ${data.endDate}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "下一周",
                    modifier = Modifier.clickable { /* TODO: 切换到下一周 */ }
                )
            }
        }

        // 勋章区域
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 红心勋章图标
                Box(
                    modifier = Modifier.size(120.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // 绘制红心和唱片
                    HeartBadge()
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 勋章说明
                Text(
                    text = data.badgeDescription,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // 总时长
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "本周收听时长",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "${data.totalHours}",
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "小时",
                        fontSize = 24.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${data.totalMinutes}",
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "分",
                        fontSize = 24.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 已听天数标签
                Text(
                    text = "已听 ${data.listenedDays}/${data.totalDays} 天",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // 柱状图
        item {
            WeeklyBarChart(
                dailyDurations = data.dailyDurations,
                topDate = data.topDate,
                topHours = data.topHours,
                topMinutes = data.topMinutes
            )
        }

        // 底部统计
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // 最晚听歌
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = data.latestTime,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = Color.Red,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "最晚听歌",
                            fontSize = 13.sp,
                            color = Color.Red
                        )
                    }
                }

                // 比上周
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${kotlin.math.abs(data.comparisonMinutes)}分",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (data.comparisonMinutes >= 0)
                                Icons.Default.KeyboardArrowUp
                            else
                                Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = Color.Red,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = if (data.comparisonMinutes >= 0) "比上周多听" else "比上周少听",
                            fontSize = 13.sp,
                            color = Color.Red
                        )
                    }
                }
            }
        }
    }
}

/**
 * 红心勋章组件
 */
@Composable
private fun HeartBadge() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val heartColor = Color(0xFFFF4D6D)
        val vinylColor = Color(0xFF2D2D2D)
        val pinkColor = Color(0xFFFFB3C1)

        // 绘制粉色心形（背景）
        drawCircle(
            color = pinkColor,
            radius = size.minDimension * 0.35f,
            center = Offset(size.width * 0.35f, size.height * 0.35f)
        )

        // 绘制主红心
        val heartPath = Path().apply {
            val centerX = size.width * 0.5f
            val centerY = size.height * 0.5f
            val heartSize = size.minDimension * 0.4f

            moveTo(centerX, centerY + heartSize * 0.3f)

            // 左半边心形
            cubicTo(
                centerX - heartSize * 0.5f, centerY + heartSize * 0.1f,
                centerX - heartSize * 0.5f, centerY - heartSize * 0.3f,
                centerX, centerY - heartSize * 0.1f
            )

            // 右半边心形
            cubicTo(
                centerX + heartSize * 0.5f, centerY - heartSize * 0.3f,
                centerX + heartSize * 0.5f, centerY + heartSize * 0.1f,
                centerX, centerY + heartSize * 0.3f
            )
        }
        drawPath(path = heartPath, color = heartColor, style = Fill)

        // 绘制黑色唱片
        drawCircle(
            color = vinylColor,
            radius = size.minDimension * 0.25f,
            center = Offset(size.width * 0.65f, size.height * 0.35f)
        )
    }
}

/**
 * 周柱状图
 */
@Composable
private fun WeeklyBarChart(
    dailyDurations: List<com.example.mymusic.data.DailyDuration>,
    topDate: String,
    topHours: Int,
    topMinutes: Int
) {
    Column {
        // TOP标注
        val maxDuration = dailyDurations.maxOfOrNull { it.hours * 60 + it.minutes } ?: 1
        val topIndex = dailyDurations.indexOfFirst { it.hours == topHours && it.minutes == topMinutes }

        if (topIndex >= 0) {
            Text(
                text = "TOP  $topDate",
                fontSize = 12.sp,
                color = Color.Red,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = (16 + topIndex * 45).dp)
            )
            Text(
                text = "听歌 ${topHours}小时${topMinutes}分",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = (16 + topIndex * 45).dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 柱状图
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        ) {
            val barWidth = size.width / dailyDurations.size * 0.6f
            val maxHeight = size.height * 0.8f
            val barColor = Color(0xFFFF4D6D)
            val inactiveBarColor = Color(0xFFFFE0E6)

            dailyDurations.forEachIndexed { index, duration ->
                val durationMinutes = duration.hours * 60 + duration.minutes
                val barHeight = if (durationMinutes > 0) {
                    (durationMinutes.toFloat() / maxDuration * maxHeight)
                } else {
                    maxHeight * 0.05f // 最小高度
                }

                val xOffset = (size.width / dailyDurations.size) * (index + 0.5f)
                val yOffset = size.height - barHeight - 30.dp.toPx()

                // 绘制柱子
                drawRoundRect(
                    color = if (durationMinutes > 0) barColor else inactiveBarColor,
                    topLeft = Offset(xOffset - barWidth / 2, yOffset),
                    size = Size(barWidth, barHeight),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(
                        barWidth / 2,
                        barWidth / 2
                    )
                )
            }
        }

        // 日期标签
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            dailyDurations.forEach { duration ->
                Text(
                    text = duration.dayLabel,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        }

        // Y轴刻度标签
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = "8小时", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(60.dp))
                Text(text = "4小时", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(60.dp))
                Text(text = "0分钟", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

/**
 * 月视图
 */
@Composable
private fun MonthlyView(data: com.example.mymusic.data.MonthlyData) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        // 时间范围
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "上个月"
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "${data.startDate} - ${data.endDate}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "下个月"
                )
            }
        }

        // 勋章区域
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 勋章图标
                Box(
                    modifier = Modifier.size(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    FullAttendanceBadge()
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = data.badgeDescription,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // 总时长
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "九月收听时长",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = "${data.totalHours}",
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = "小时", fontSize = 24.sp)
                    Text(
                        text = "${data.totalMinutes}",
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = "分", fontSize = 24.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "已听 ${data.listenedDays}/${data.totalDays} 天",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // 日历打卡网格
        item {
            MonthlyCalendar(dailyCheckins = data.dailyCheckins)
        }

        // 底部统计
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = data.latestTime,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = Color.Red,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(text = "最晚听歌", fontSize = 13.sp, color = Color.Red)
                    }
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${data.comparisonHours}小时${data.comparisonMinutes}分",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowUp,
                            contentDescription = null,
                            tint = Color.Red,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(text = "比上月多听", fontSize = 13.sp, color = Color.Red)
                    }
                }
            }
        }
    }
}

/**
 * 全勤奖勋章
 */
@Composable
private fun FullAttendanceBadge() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val badgeColor = Color(0xFFFF4D6D)
        val starColor = Color(0xFFFF80A0)

        // 绘制勋章主体（多边形）
        val radius = size.minDimension * 0.4f
        val centerX = size.width / 2
        val centerY = size.height / 2

        drawCircle(
            color = badgeColor,
            radius = radius,
            center = Offset(centerX, centerY)
        )

        // 绘制星星装饰
        drawCircle(
            color = starColor,
            radius = radius * 0.2f,
            center = Offset(centerX - radius * 0.6f, centerY - radius * 0.6f)
        )
    }
}

/**
 * 月历打卡日历
 */
@Composable
private fun MonthlyCalendar(dailyCheckins: List<DayCheckin>) {
    Column {
        // 星期标题
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("日", "一", "二", "三", "四", "五", "六").forEach { day ->
                Text(
                    text = day,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 日历网格
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.height(240.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(dailyCheckins) { checkin ->
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(
                            if (checkin.hasListened)
                                Color(0xFFFF4D6D)
                            else
                                Color(0xFFFFE0E6)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${checkin.day}",
                        fontSize = 14.sp,
                        color = if (checkin.hasListened) Color.White else Color(0xFFFF4D6D),
                        fontWeight = if (checkin.hasListened) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}

/**
 * 年视图
 */
@Composable
private fun YearlyView(years: List<YearData>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        // 标题区域
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "年度听歌足迹",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "年数据随每年听歌报告发布",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // 时间轴装饰（可选）
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
            ) {
                // 简单的时间轴装饰
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val path = Path().apply {
                        moveTo(0f, size.height / 2)
                        lineTo(size.width, size.height / 2)
                    }
                    drawPath(
                        path = path,
                        color = Color.Gray.copy(alpha = 0.3f),
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2f)
                    )
                }
                Text(
                    text = "2025",
                    fontSize = 14.sp,
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.TopEnd)
                )
            }
        }

        // 年度卡片列表
        items(years.sortedByDescending { it.year }) { year ->
            YearCard(yearData = year)
        }
    }
}

/**
 * 年度卡片
 */
@Composable
private fun YearCard(yearData: YearData) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // 年份
        Text(
            text = "${yearData.year}",
            fontSize = 56.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFF4D6D)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 数据卡片
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "听歌时长",
                            fontSize = 13.sp,
                            color = Color.Red
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${yearData.totalHours}小时",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "听歌总数",
                            fontSize = 13.sp,
                            color = Color.Red
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${yearData.totalSongs}首",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // 年度报告（如果有）
                if (yearData.reportTitle != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // 报告缩略图占位
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFFFB3C1))
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = yearData.reportTitle,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                if (yearData.reportSubtitle != null) {
                                    Text(
                                        text = yearData.reportSubtitle,
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
