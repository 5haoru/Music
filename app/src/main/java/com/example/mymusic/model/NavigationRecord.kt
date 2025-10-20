package com.example.mymusic.model

/**
 * 导航记录数据类
 * 用于追踪页面导航情况，检查页面跳转是否成功
 * 对应任务：1-3, 10-11, 13-16
 */
data class NavigationRecord(
    val fromPage: String,          // 来源页面
    val toPage: String,            // 目标页面
    val navigationTime: Long,      // 导航时间（时间戳）
    val isSuccess: Boolean = false    // 是否导航成功
)
