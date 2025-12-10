package com.example.mymusic.data.model

/**
 * 搜索记录数据类
 * 用于追踪搜索操作情况，检查搜索是否成功
 * 对应任务：13-14
 */
data class SearchRecord(
    val searchId: String,          // 搜索ID
    val keyword: String,           // 搜索关键词
    val searchTime: Long,          // 搜索时间（时间戳）
    val resultCount: Int = 0,      // 搜索结果数量
    val isSuccess: Boolean = false    // 是否搜索成功
)
