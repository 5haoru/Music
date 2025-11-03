package com.example.mymusic.presenter

import android.content.Context
import com.example.mymusic.data.Song
import com.example.mymusic.model.CollectionRecord
import com.example.mymusic.model.DownloadRecord
import com.example.mymusic.model.ArtistFollowRecord
import com.example.mymusic.utils.DataLoader

/**
 * 播放定制页面Presenter
 * 处理收藏、下载、分享、关注等操作的业务逻辑
 */
class PlayCustomizePresenter(
    private val view: PlayCustomizeContract.View,
    private val context: Context
) : PlayCustomizeContract.Presenter {

    private var currentSong: Song? = null
    private var isCollected: Boolean = false
    private var isDownloaded: Boolean = false
    private var isFollowed: Boolean = false

    override fun loadSong(songId: String) {
        view.showLoading()
        try {
            val song = DataLoader.getSongById(context, songId)
            if (song != null) {
                currentSong = song
                view.showSong(song)

                // 检查收藏状态（简化实现，实际应检查collection_records）
                isCollected = false
                view.updateCollectionState(isCollected)

                // 检查下载状态（简化实现，实际应检查download_records）
                isDownloaded = false
                view.updateDownloadState(isDownloaded)

                // 检查关注状态（简化实现，实际应检查artist_follow_records）
                isFollowed = false
                view.updateFollowState(isFollowed)

                view.hideLoading()
            } else {
                view.hideLoading()
                view.showError("未找到歌曲")
            }
        } catch (e: Exception) {
            view.hideLoading()
            view.showError("加载失败: ${e.message}")
        }
    }

    override fun onCollectionClick() {
        // 打开收藏到歌单页面，让用户选择歌单
        view.navigateToCollectSong()
    }

    override fun onDownloadClick() {
        currentSong?.let { song ->
            try {
                isDownloaded = !isDownloaded
                view.updateDownloadState(isDownloaded)

                // 保存下载记录
                val downloadId = DataLoader.generateDownloadId(context)
                val record = DownloadRecord(
                    downloadId = downloadId,
                    songId = song.songId,
                    songName = song.songName,
                    downloadTime = System.currentTimeMillis(),
                    quality = "极高",
                    isSuccess = true
                )
                DataLoader.saveDownloadRecord(context, record)

                val message = if (isDownloaded) "已开始下载（极高音质）" else "已取消下载"
                view.showSuccess(message)
            } catch (e: Exception) {
                view.showError("下载失败: ${e.message}")
            }
        }
    }

    override fun onShareClick() {
        // 关闭当前浮层，触发分享页面（由PlayTab处理）
        view.close()
    }

    override fun onListenTogetherClick() {
        view.showSuccess("邀请好友一起听")
    }

    override fun onFollowClick() {
        currentSong?.let { song ->
            try {
                isFollowed = !isFollowed
                view.updateFollowState(isFollowed)

                // 保存关注记录
                val record = ArtistFollowRecord(
                    artistId = song.artist,
                    artistName = song.artist,
                    operationType = if (isFollowed) "follow" else "unfollow",
                    operationTime = System.currentTimeMillis(),
                    isSuccess = true
                )
                DataLoader.saveArtistFollowRecord(context, record)

                val message = if (isFollowed) "已关注${song.artist}" else "已取消关注"
                view.showSuccess(message)
            } catch (e: Exception) {
                view.showError("操作失败: ${e.message}")
            }
        }
    }

    override fun onSongEncyclopediaClick() {
        view.navigateToSongProfile()
    }

    override fun onSimilarStrollClick() {
        view.showSuccess("开始相似歌曲漫游")
    }

    override fun onPurchaseClick() {
        view.showSuccess("单曲购买")
    }

    override fun onQualityClick() {
        view.showSuccess("音质设置")
    }

    override fun onAudioEffectClick() {
        view.showSuccess("音效设置")
    }

    override fun onPlayerStyleClick() {
        view.navigateToPlayerStyle()
    }

    override fun onCloseClick() {
        view.close()
    }

    override fun onDestroy() {
        // 清理资源
    }
}
