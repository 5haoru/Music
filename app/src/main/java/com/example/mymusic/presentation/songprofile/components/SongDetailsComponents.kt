package com.example.mymusic.presentation.songprofile.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mymusic.data.SongProfile

@Composable
fun SongDetailsSection(
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
            Text(
                text = "歌曲详情",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(20.dp))

            DetailInfoRow(label = "曲风", value = profile.genre, valueColor = AccentRed)
            Spacer(modifier = Modifier.height(16.dp))
            DetailInfoRow(label = "专辑", value = profile.album)
            Spacer(modifier = Modifier.height(16.dp))
            DetailInfoRow(label = "语种", value = profile.language)
            Spacer(modifier = Modifier.height(16.dp))
            DetailInfoRow(label = "发行时间", value = profile.releaseDate)
            Spacer(modifier = Modifier.height(16.dp))
            DetailInfoRow(label = "BPM", value = profile.bpm.toString())

            Spacer(modifier = Modifier.height(20.dp))

            ExpandableDetailRow(label = "制作信息", value = profile.production, onClick = onProductionClick)
            Spacer(modifier = Modifier.height(16.dp))
            ExpandableDetailRow(label = "简介：", value = profile.introduction, onClick = onIntroductionClick)

            Spacer(modifier = Modifier.height(20.dp))

            ClickableListRow(
                label = "影综",
                previewText = profile.filmTvList.firstOrNull() ?: "",
                count = profile.filmTvList.size,
                onClick = onFilmTvClick
            )
            Spacer(modifier = Modifier.height(16.dp))
            AwardRow(awards = profile.awardsList, onClick = onAwardsClick)
            Spacer(modifier = Modifier.height(16.dp))
            ScoresRow(scores = profile.scoresList, onClick = onScoresClick)
        }
    }
}

@Composable
fun DetailInfoRow(
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

@Composable
fun ExpandableDetailRow(
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

@Composable
fun ClickableListRow(
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

@Composable
fun AwardRow(
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
            text = "奖项",
            fontSize = 15.sp,
            color = TextSecondary,
            modifier = Modifier.width(80.dp)
        )

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

@Composable
fun ScoresRow(
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
                text = "乐谱",
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

@Composable
fun ScoreCard(instrumentName: String) {
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
