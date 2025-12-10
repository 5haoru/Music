package com.example.mymusic.presentation.albumlist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mymusic.data.Album
import com.example.mymusic.data.repository.AlbumRepository
import com.example.mymusic.presentation.albumlist.components.AlbumListItem

/**
 * 专辑列表内容组件（用于嵌入SingerTab）
 */
@Composable
fun AlbumListContent(
    artistId: String,
    onAlbumClick: (String) -> Unit
) {
    val context = LocalContext.current

    // 状态
    var albums by remember { mutableStateOf<List<Album>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Presenter
    val presenter = remember {
        val albumRepository = AlbumRepository(context)
        AlbumListPresenter(
            object : AlbumListContract.View {
                override fun showAlbums(albumList: List<Album>) {
                    albums = albumList
                }

                override fun navigateToAlbum(albumId: String) {
                    onAlbumClick(albumId)
                }

                override fun navigateBack() {
                    // 由外部控制
                }

                override fun showLoading() {
                    isLoading = true
                }

                override fun hideLoading() {
                    isLoading = false
                }

                override fun showError(message: String) {
                    errorMessage = message
                    isLoading = false
                }

                override fun showSuccess(message: String) {
                    android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT).show()
                }
            },
            albumRepository
        )
    }

    // 加载数据
    LaunchedEffect(artistId) {
        presenter.loadAlbumsByArtist(artistId)
    }

    // 显示内容
    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (errorMessage != null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = errorMessage ?: "")
        }
    } else if (albums.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "暂无专辑",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(albums) { album ->
                AlbumListItem(
                    album = album,
                    onClick = { presenter.onAlbumClick(album.albumId) }
                )
            }

            // 底部留白
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}
