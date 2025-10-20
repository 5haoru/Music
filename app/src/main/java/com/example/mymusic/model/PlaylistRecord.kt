package com.example.mymusic.model

/**
 * 歌单管理记录数据类
 * 用于追踪歌单创建和操作情况，检查歌单操作是否成功
 * 对应任务：12, 21
 */
data class PlaylistRecord(
    val playlistId: String,        // 歌单ID
    val playlistName: String,      // 歌单名称
    val operationType: String,     // 操作类型（create/add_song/delete_song）
    val songId: String? = null,    // 相关歌曲ID（添加/删除歌曲时使用）
    val operationTime: Long,       // 操作时间（时间戳）
    val isSuccess: Boolean = false    // 是否操作成功
)
