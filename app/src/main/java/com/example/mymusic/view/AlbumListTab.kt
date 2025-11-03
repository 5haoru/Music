package com.example.mymusic.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mymusic.data.Album
import com.example.mymusic.presenter.AlbumListContract
import com.example.mymusic.presenter.AlbumListPresenter

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
            },
            context
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

/**
 * 专辑列表项
 */
@Composable
private fun AlbumListItem(
    album: Album,
    onClick: () -> Unit
) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 专辑封面
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data("file:///android_asset/${album.coverUrl}")
                .crossfade(true)
                .build(),
            contentDescription = "专辑封面",
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        // 专辑信息
        Column(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        ) {
            // 专辑名
            Text(
                text = album.albumName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            // 歌手名
            Text(
                text = album.artist,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            // 发行日期
            Text(
                text = album.releaseDate,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
