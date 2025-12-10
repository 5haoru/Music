package com.example.mymusic.presentation.comment

import com.example.mymusic.data.Comment
import com.example.mymusic.data.Song
import com.example.mymusic.data.repository.CommentRepository
import com.example.mymusic.data.repository.SongRepository
import com.example.mymusic.utils.AutoTestHelper

/**
 * 评论页面Presenter
 */
class CommentPresenter(
    private val view: CommentContract.View,
    private val songRepository: SongRepository,
    private val commentRepository: CommentRepository
) : CommentContract.Presenter {

    private var allComments: List<Comment> = emptyList()
    private var displayedComments: List<Comment> = emptyList()
    private var currentSong: Song? = null
    private var currentSortMode: CommentSortMode = CommentSortMode.RECOMMENDED
    private val likedComments = mutableSetOf<String>()

    override fun loadData(songId: String) {
        view.showLoading()
        try {
            currentSong = songRepository.getSongById(songId)

            if (currentSong == null) {
                view.hideLoading()
                view.showError("未找到歌曲信息")
                return
            }

            allComments = commentRepository.getCommentsForItem(songId)

            currentSong?.let { view.showSong(it) }
            view.showCommentCount(allComments.size)
            view.showNotesCount(310) // Mock data
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
            CommentSortMode.RECOMMENDED -> allComments.sortedByDescending { it.likeCount * 0.7 + it.replyCount * 0.3 }
            CommentSortMode.HOTTEST -> allComments.sortedByDescending { it.likeCount }
            CommentSortMode.LATEST -> allComments.sortedByDescending { it.timestamp }
        }
        view.showComments(displayedComments)
    }

    override fun onCommentLike(commentId: String) {
        val isLiked = likedComments.contains(commentId)
        val comment = displayedComments.find { it.commentId == commentId } ?: return

        if (isLiked) {
            likedComments.remove(commentId)
            view.updateCommentLikeState(commentId, false, comment.likeCount)
        } else {
            likedComments.add(commentId)
            view.updateCommentLikeState(commentId, true, comment.likeCount + 1)
        }
    }

    override fun onCommentExpand(commentId: String) {}

    override fun onReplyClick(commentId: String) {}

    override fun onBackClick() {
        view.navigateBack()
    }

    override fun onSendComment(content: String) {
        if (content.isBlank()) {
            view.showError("评论内容不能为空")
            return
        }

        val song = currentSong ?: run {
            view.showError("歌曲信息不存在")
            return
        }

        try {
            val newComment = Comment(
                commentId = commentRepository.generateCommentId(),
                songId = song.songId,
                userId = "user_current",
                username = "我",
                avatarUrl = "avatar/1.png",
                content = content,
                timestamp = System.currentTimeMillis(),
                likeCount = 0,
                replyCount = 0,
                isLiked = false,
                userLevel = 1,
                isLongComment = content.length > 100,
                isCollapsed = content.length > 100
            )

            commentRepository.addComment(newComment)
            AutoTestHelper.addComment(song.songId, content)

            allComments = allComments + newComment
            applySortMode(currentSortMode)
            view.showCommentCount(allComments.size)
            view.showSuccess("评论发送成功")
            view.clearCommentInput()
        } catch (e: Exception) {
            view.showError("评论发送失败: ${e.message}")
        }
    }

    override fun onDestroy() {}
}
