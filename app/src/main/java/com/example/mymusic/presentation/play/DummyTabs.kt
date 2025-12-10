package com.example.mymusic.presentation.play

import androidx.compose.runtime.Composable
import com.example.mymusic.data.Song

@Composable
fun ShareTab(song: Song, onCloseClick: () -> Unit) {}

@Composable
fun PlayCustomizeTab(
    song: Song,
    onCloseClick: () -> Unit,
    onShareClick: () -> Unit,
    onNavigateToPlayerStyle: () -> Unit,
    onNavigateToSongProfile: () -> Unit,
    onNavigateToCollectSong: () -> Unit
) {}

@Composable
fun PlayerTab(onBackClick: () -> Unit) {}

@Composable
fun SongProfileTab(songId: String, onBackClick: () -> Unit) {}

@Composable
fun CollectSongTab(
    songId: String,
    onBackClick: () -> Unit,
    onNavigateToCreatePlaylist: () -> Unit
) {}
