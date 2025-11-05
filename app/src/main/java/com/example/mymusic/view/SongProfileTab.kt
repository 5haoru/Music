package com.example.mymusic.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mymusic.data.SongProfile
import com.example.mymusic.presenter.SongProfileContract
import com.example.mymusic.presenter.SongProfilePresenter
import com.example.mymusic.utils.AutoTestHelper

// 浅色主题颜色
private val LightBackground = Color(0xFFFFFFFF)
private val LightCardBackground = Color(0xFFF5F5F5)
private val LightSectionBackground = Color(0xFFE0E0E0)
private val TextPrimary = Color(0xFF000000)
private val TextSecondary = Color(0xFF666666)
private val TextTertiary = Color(0xFF999999)
private val AccentRed = Color(0xFFFF6B6B)

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
            },
            context
        )
    }

    // 加载数据
    LaunchedEffect(songId) {
        presenter.loadSongProfile(songId)
    }

    // 更新AutoTest状态，记录当前查看的歌曲详情页面
    LaunchedEffect(songId) {
        AutoTestHelper.updateCurrentPage("song_detail")
        AutoTestHelper.updateCurrentSongId(songId)
    }

    // 退出时清除歌曲详情状态
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
                // 顶部栏
                SongProfileTopBar(
                    songName = songProfile!!.songName,
                    onBackClick = { presenter.onBackClick() }
                )

                // 滚动内容
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    // 个性化数据卡片
                    PersonalDataCard(profile = songProfile!!)

                    Spacer(modifier = Modifier.height(16.dp))

                    // 歌曲详情区域
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

/**
 * 顶部栏
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SongProfileTopBar(
    songName: String,
    onBackClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "歌曲百科·$songName",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = TextPrimary
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "返回",
                    tint = TextPrimary
                )
            }
        },
        actions = {
            IconButton(onClick = { /* TODO: 更多操作 */ }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "更多",
                    tint = TextPrimary
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = LightBackground
        )
    )
}

/**
 * 个性化数据卡片
 */
@Composable
private fun PersonalDataCard(profile: SongProfile) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = LightCardBackground
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 左侧：头像占位符
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(LightSectionBackground),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🎵",
                    fontSize = 28.sp
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // 中间：第一次听
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "第一次听",
                    fontSize = 13.sp,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = profile.firstListenTime ?: "未知时间",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    lineHeight = 20.sp
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // 右侧：累计播放
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "累计播放",
                    fontSize = 13.sp,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${profile.totalPlayCount}次",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = profile.poeticDescription,
                    fontSize = 12.sp,
                    color = TextTertiary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

/**
 * 歌曲详情区域
 */
@Composable
private fun SongDetailsSection(
    profile: SongProfile,
    onProductionClick: () -> Unit,
    onIntroductionClick: () -> Unit,
    onFilmTvClick: () -> Unit,
    onAwardsClick: () -> Unit,
    onScoresClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = LightCardBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // 标题
            Text(
                text = "歌曲详情",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 曲风
            DetailInfoRow(
                label = "曲风：",
                value = profile.genre,
                valueColor = AccentRed
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 专辑
            DetailInfoRow(
                label = "专辑：",
                value = profile.album
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 语种
            DetailInfoRow(
                label = "语种：",
                value = profile.language
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 发行时间
            DetailInfoRow(
                label = "发行：",
                value = profile.releaseDate
            )

            Spacer(modifier = Modifier.height(16.dp))

            // BPM
            DetailInfoRow(
                label = "BPM：",
                value = profile.bpm.toString()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 制作信息（可点击）
            ExpandableDetailRow(
                label = "制作：",
                value = profile.production,
                onClick = onProductionClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 简介（可点击）
            ExpandableDetailRow(
                label = "简介：",
                value = profile.introduction,
                onClick = onIntroductionClick
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 影综
            ClickableListRow(
                label = "影综：",
                previewText = profile.filmTvList.firstOrNull() ?: "",
                count = profile.filmTvList.size,
                onClick = onFilmTvClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 奖项
            AwardRow(
                awards = profile.awardsList,
                onClick = onAwardsClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 乐谱
            ScoresRow(
                scores = profile.scoresList,
                onClick = onScoresClick
            )
        }
    }
}

/**
 * 详细信息行
 */
@Composable
private fun DetailInfoRow(
    label: String,
    value: String,
    valueColor: Color = TextPrimary
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            fontSize = 15.sp,
            color = TextSecondary,
            modifier = Modifier.width(80.dp)
        )
        Text(
            text = value,
            fontSize = 15.sp,
            color = valueColor,
            modifier = Modifier.weight(1f)
        )
    }
}

/**
 * 可展开的详细信息行
 */
@Composable
private fun ExpandableDetailRow(
    label: String,
    value: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            fontSize = 15.sp,
            color = TextSecondary,
            modifier = Modifier.width(80.dp)
        )
        Text(
            text = value,
            fontSize = 15.sp,
            color = TextPrimary,
            modifier = Modifier.weight(1f),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = TextTertiary,
            modifier = Modifier.size(20.dp)
        )
    }
}

/**
 * 可点击的列表行
 */
@Composable
private fun ClickableListRow(
    label: String,
    previewText: String,
    count: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 15.sp,
            color = TextSecondary,
            modifier = Modifier.width(80.dp)
        )
        Text(
            text = previewText,
            fontSize = 15.sp,
            color = AccentRed,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = "${count}个",
            fontSize = 13.sp,
            color = TextTertiary,
            modifier = Modifier.padding(end = 4.dp)
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = TextTertiary,
            modifier = Modifier.size(20.dp)
        )
    }
}

/**
 * 奖项行（带装饰图标）
 */
@Composable
private fun AwardRow(
    awards: List<String>,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "奖项：",
            fontSize = 15.sp,
            color = TextSecondary,
            modifier = Modifier.width(80.dp)
        )

        // 装饰图标
        Text(text = "🏆", fontSize = 16.sp)

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = awards.firstOrNull() ?: "",
                fontSize = 15.sp,
                color = AccentRed,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (awards.size > 1) {
                Text(
                    text = awards.getOrNull(1) ?: "",
                    fontSize = 13.sp,
                    color = TextTertiary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Text(
            text = "${awards.size}个",
            fontSize = 13.sp,
            color = TextTertiary,
            modifier = Modifier.padding(end = 4.dp)
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = TextTertiary,
            modifier = Modifier.size(20.dp)
        )
    }
}

/**
 * 乐谱行（横向滚动卡片）
 */
@Composable
private fun ScoresRow(
    scores: List<String>,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "乐谱：",
                fontSize = 15.sp,
                color = TextSecondary,
                modifier = Modifier.width(80.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "${scores.size}个",
                fontSize = 13.sp,
                color = TextTertiary,
                modifier = Modifier.padding(end = 4.dp)
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = TextTertiary,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 横向滚动的乐谱卡片
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(start = 80.dp)
        ) {
            items(scores.take(4)) { score ->
                ScoreCard(instrumentName = score)
            }
        }
    }
}

/**
 * 乐谱卡片
 */
@Composable
private fun ScoreCard(instrumentName: String) {
    Surface(
        modifier = Modifier
            .width(80.dp)
            .height(80.dp),
        shape = RoundedCornerShape(8.dp),
        color = LightSectionBackground
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = instrumentName,
                fontSize = 13.sp,
                color = TextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
