package com.example.mymusic.data.repository

import android.content.Context
import com.example.mymusic.data.Comment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets

class CommentRepository(private val context: Context) {

    private val gson = Gson()
    private var cachedComments: List<Comment>? = null
    private val COMMENTS_FILE = "comments.json"

    fun getCommentsForItem(songId: String): List<Comment> {
        return getAllComments().filter { it.songId == songId }
    }

    fun addComment(comment: Comment) {
        val currentComments = getAllComments().toMutableList()
        currentComments.add(comment)
        saveAllComments(currentComments)
    }

    fun generateCommentId(): String {
        val timestamp = System.currentTimeMillis()
        return "comment_$timestamp"
    }

    private fun getAllComments(): List<Comment> {
        if (cachedComments == null) {
            cachedComments = try {
                // First, try reading from internal storage
                val json = context.openFileInput(COMMENTS_FILE).bufferedReader().use { it.readText() }
                val type = object : TypeToken<List<Comment>>() {}.type
                gson.fromJson(json, type)
            } catch (e: Exception) {
                // If it fails, read from assets
                val json = context.assets.open("data/comments.json").bufferedReader().use { it.readText() }
                val type = object : TypeToken<List<Comment>>() {}.type
                gson.fromJson(json, type)
            }
        }
        return cachedComments ?: emptyList()
    }

    private fun saveAllComments(comments: List<Comment>) {
        val jsonString = gson.toJson(comments)
        context.openFileOutput(COMMENTS_FILE, Context.MODE_PRIVATE).use {
            OutputStreamWriter(it, StandardCharsets.UTF_8).use { writer ->
                writer.write(jsonString)
            }
        }
        cachedComments = comments
    }
}
