package com.example.mymusic.presenter

import android.content.Context
import com.example.mymusic.data.Comment
import com.example.mymusic.data.CommentRecord
import com.example.mymusic.data.Song
import com.example.mymusic.utils.DataLoader

/**
 * 评论页面Presenter
 */
class CommentPresenter(
    private val view: CommentContract.View,
    private val context: Context
) : CommentContract.Presenter {

    private var allComments: List<Comment> = emptyList()
    private var displayedComments: List<Comment> = emptyList()
    private var currentSong: Song? = null
    private var currentSortMode: CommentSortMode = CommentSortMode.RECOMMENDED
    private val likedComments = mutableSetOf<String>()

    override fun loadData(songId: String) {
        view.showLoading()
        try {
            // 加载歌曲信息
            val songs = DataLoader.loadSongs(context)
            currentSong = songs.find { it.songId == songId }

            if (currentSong == null) {
                view.hideLoading()
                view.showError("未找到歌曲信息")
                return
            }

            // 加载评论数据
            allComments = DataLoader.loadComments(context)
                .filter { it.songId == songId }

            // 显示歌曲信息
            currentSong?.let { view.showSong(it) }

            // 显示评论总数
            view.showCommentCount(allComments.size)

            // 显示笔记数（模拟数据）
            view.showNotesCount(310)

            // 应用默认排序
            applySortMode(currentSortMode)

            view.hideLoading()
        } catch (e: Exception) {
            view.hideLoading()
            view.showError("加载数据失败: ${e.message}")
        }
    }

    override fun onSortModeChange(mode: CommentSortMode) {
        currentSortMode = mode
        view.updateSortMode(mode)
        applySortMode(mode)
    }

    private fun applySortMode(mode: CommentSortMode) {
        displayedComments = when (mode) {
            CommentSortMode.RECOMMENDED -> {
                // 推荐排序：按点赞数和回复数综合排序
                allComments.sortedByDescending {
                    it.likeCount * 0.7 + it.replyCount * 0.3
                }
            }
            CommentSortMode.HOTTEST -> {
                // 最热排序：按点赞数排序
                allComments.sortedByDescending { it.likeCount }
            }
            CommentSortMode.NEWEST -> {
                // 最新排序：按时间排序
                allComments.sortedByDescending { it.timestamp }
            }
        }
        view.showComments(displayedComments)
    }

    override fun onCommentLike(commentId: String) {
        val isLiked = likedComments.contains(commentId)
        val comment = displayedComments.find { it.commentId == commentId } ?: return

        if (isLiked) {
            // 取消点赞
            likedComments.remove(commentId)
            view.updateCommentLikeState(commentId, false, comment.likeCount)
        } else {
            // 点赞
            likedComments.add(commentId)
            view.updateCommentLikeState(commentId, true, comment.likeCount + 1)
        }

        // TODO: 保存到comment_records.json
    }

    override fun onCommentExpand(commentId: String) {
        // TODO: 处理评论展开/折叠逻辑
    }

    override fun onReplyClick(commentId: String) {
        // TODO: 打开回复页面或弹窗
    }

    override fun onBackClick() {
        view.navigateBack()
    }

    override fun onSendComment(content: String) {
        if (content.isBlank()) {
            view.showError("评论内容不能为空")
            return
        }

        if (currentSong == null) {
            view.showError("歌曲信息不存在")
            return
        }

        try {
            // 生成新的评论ID
            val commentId = DataLoader.generateCommentId(context)

            // 创建新评论对象
            val newComment = Comment(
                commentId = commentId,
                songId = currentSong!!.songId,
                userId = "user_current", // 当前用户ID，实际应用中应从用户登录信息获取
                username = "我", // 当前用户名，实际应用中应从用户登录信息获取
                avatarUrl = "avatar/1.png", // 当前用户头像，实际应用中应从用户登录信息获取
                content = content,
                timestamp = System.currentTimeMillis(),
                likeCount = 0,
                replyCount = 0,
                isLiked = false,
                userLevel = 1,
                isLongComment = content.length > 100,
                isCollapsed = content.length > 100
            )

            // 保存评论到数据文件
            DataLoader.saveComment(context, newComment)

            // 创建评论记录
            val recordId = DataLoader.generateRecordId(context)
            val commentRecord = CommentRecord(
                recordId = recordId,
                userId = "user_current",
                username = "我",
                songId = currentSong!!.songId,
                songName = currentSong!!.songName,
                artist = currentSong!!.artist,
                commentContent = content,
                timestamp = System.currentTimeMillis(),
                commentId = commentId
            )

            // 保存评论记录
            DataLoader.saveCommentRecord(context, commentRecord)

            // 更新本地评论列表
            allComments = allComments + newComment

            // 重新应用排序
            applySortMode(currentSortMode)

            // 更新评论总数
            view.showCommentCount(allComments.size)

            // 显示成功提示
            view.showSuccess("评论发送成功")

            // 清空输入框
            view.clearCommentInput()
        } catch (e: Exception) {
            view.showError("评论发送失败: ${e.message}")
        }
    }

    override fun onDestroy() {
        // Clean up resources
    }
}
