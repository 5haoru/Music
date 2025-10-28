package com.example.mymusic.data

/**
 * 听歌时长数据类
 * 包含周、月、年三种时间维度的听歌统计数据
 */
data class ListeningDuration(
    val weekly: WeeklyData,
    val monthly: MonthlyData,
    val yearly: YearlyData
)

/**
 * 周数据
 */
data class WeeklyData(
    val startDate: String,           // 开始日期 "10.19"
    val endDate: String,              // 结束日期 "10.25"
    val badgeTitle: String,           // 勋章标题 "红心收藏家"
    val badgeDescription: String,     // 勋章描述 "本周82%听歌时长来自红心歌单"
    val totalHours: Int,              // 总小时数 24
    val totalMinutes: Int,            // 总分钟数 29
    val listenedDays: Int,            // 已听天数 7
    val totalDays: Int,               // 总天数 7
    val dailyDurations: List<DailyDuration>,  // 每日时长数据（7天）
    val topDate: String,              // TOP日期 "2025.10.23"
    val topHours: Int,                // TOP小时 6
    val topMinutes: Int,              // TOP分钟 50
    val latestTime: String,           // 最晚听歌时间 "02:30"
    val comparisonMinutes: Int        // 与上周对比分钟数 -6（负数表示少听）
)

/**
 * 每日时长数据
 */
data class DailyDuration(
    val dayLabel: String,             // 日期标签 "日", "一", "二"...
    val hours: Int,                   // 小时数
    val minutes: Int                  // 分钟数
)

/**
 * 月数据
 */
data class MonthlyData(
    val startDate: String,            // 开始日期 "9.1"
    val endDate: String,              // 结束日期 "9.30"
    val badgeTitle: String,           // 勋章标题 "听歌全勤奖"
    val badgeDescription: String,     // 勋章描述 "本月听歌天数达30天!"
    val totalHours: Int,              // 总小时数 75
    val totalMinutes: Int,            // 总分钟数 30
    val listenedDays: Int,            // 已听天数 30
    val totalDays: Int,               // 总天数 30
    val dailyCheckins: List<DayCheckin>,  // 每日打卡数据（最多31天）
    val latestTime: String,           // 最晚听歌时间 "04:55"
    val comparisonHours: Int,         // 与上月对比小时数 26（正数表示多听）
    val comparisonMinutes: Int        // 与上月对比分钟数 12
)

/**
 * 每日打卡数据
 */
data class DayCheckin(
    val day: Int,                     // 日期数字 1-31
    val hasListened: Boolean          // 是否有听歌
)

/**
 * 年度数据
 */
data class YearlyData(
    val years: List<YearData>         // 多年数据列表
)

/**
 * 单年数据
 */
data class YearData(
    val year: Int,                    // 年份 2024
    val totalHours: Int,              // 总小时数 894
    val totalSongs: Int,              // 总歌曲数 1731
    val reportTitle: String?,         // 年度报告标题（可选）
    val reportSubtitle: String?       // 年度报告副标题（可选）
)
