package com.example.mymusic.presentation.me.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mymusic.data.Playlist

/**
 * 歌单列表区域
 */
@Composable
fun PlaylistsSection(
    playlists: List<Playlist>,
    onPlaylistClick: (Playlist) -> Unit,
    onDeletePlaylist: (Playlist) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        // 我喜欢的音乐（特殊样式）
        val myFavoritesPlaylist = playlists.find { it.playlistId == "my_favorites" }
        if (myFavoritesPlaylist != null) {
            FavoriteMusicItem(
                playlist = myFavoritesPlaylist,
                onPlaylistClick = onPlaylistClick
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 其他歌单（过滤掉"我喜欢的音乐"）
        playlists.filter { it.playlistId != "my_favorites" }.forEach { playlist ->
            PlaylistItem(
                playlist = playlist,
                onClick = { onPlaylistClick(playlist) },
                onDeleteClick = { onDeletePlaylist(playlist) }
            )
        }
    }
}
