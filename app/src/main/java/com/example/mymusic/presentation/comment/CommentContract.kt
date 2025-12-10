package com.example.mymusic.presentation.comment

import com.example.mymusic.base.BasePresenter
import com.example.mymusic.base.BaseView
import com.example.mymusic.data.Comment
import com.example.mymusic.data.Song

/**
 * 评论页面契约接口
 */
interface CommentContract {
    interface View : BaseView {
        fun showSong(song: Song)
        fun showComments(comments: List<Comment>)
        fun showCommentCount(count: Int)
        fun showNotesCount(count: Int)
        fun updateSortMode(mode: CommentSortMode)
        fun updateCommentLikeState(commentId: String, isLiked: Boolean, likeCount: Int)
        fun navigateBack()
        override fun showSuccess(message: String)
        fun clearCommentInput()
    }

    interface Presenter : BasePresenter {
        fun loadData(songId: String)
        fun onSortModeChange(mode: CommentSortMode)
        fun onCommentLike(commentId: String)
        fun onCommentExpand(commentId: String)
        fun onReplyClick(commentId: String)
        fun onBackClick()
        fun onSendComment(content: String)
    }
}
