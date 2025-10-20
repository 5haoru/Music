package com.example.mymusic.data

/**
 * 用户数据类
 * 对应 assets/data/users.json
 */
data class User(
    val userId: String,
    val username: String,
    val avatarUrl: String,
    val level: Int,
    val listenCount: Int
)
