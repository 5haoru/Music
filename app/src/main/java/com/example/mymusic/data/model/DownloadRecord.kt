package com.example.mymusic.data.model

/**
 * 下载记录数据类
 * 用于追踪歌曲下载情况，检查下载是否成功
 */
data class DownloadRecord(
    val downloadId: String,        // 下载ID
    val songId: String,            // 歌曲ID
    val songName: String,          // 歌曲名称
    val downloadTime: Long,        // 下载时间（时间戳）
    val quality: String,           // 音质（极高/高/标准）
    val isSuccess: Boolean = false    // 是否下载成功
)
