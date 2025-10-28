package com.example.mymusic.data

/**
 * 关注对象数据类
 * 用于表示关注列表中的项目（可以是歌手或用户）
 * 对应 assets/data/follow_items.json
 */
data class FollowItem(
    val id: String,                    // 关注对象ID
    val name: String,                  // 名称（歌手名或用户名）
    val type: String,                  // 类型: "artist" 或 "user"
    val avatarUrl: String,             // 头像URL
    val subtitle: String? = null,      // 副标题（昵称等）
    val vipType: String? = null,       // VIP类型: "svip", "black_svip", "vip" 等
    val activityType: String? = null,  // 动态类型: "mv", "post", null（无动态）
    val activityText: String? = null,  // 动态文本: "发布了MV", "发布了新动态" 等
    val timestamp: String? = null,     // 时间戳文本: "最近", "10小时前" 等
    val followTime: Long = 0           // 关注时间（用于排序）
)
