package com.example.mymusic.presentation.playcustomize

import com.example.mymusic.presentation.playcustomize.PlayCustomizeContract
import com.example.mymusic.data.Song
import com.example.mymusic.data.repository.ArtistFollowRecordRepository
import com.example.mymusic.data.repository.DownloadRecordRepository
import com.example.mymusic.data.repository.SongRepository
import com.example.mymusic.data.model.ArtistFollowRecord
import com.example.mymusic.data.model.DownloadRecord

class PlayCustomizePresenter(
    private val view: PlayCustomizeContract.View,
    private val songRepository: SongRepository,
    private val downloadRecordRepository: DownloadRecordRepository,
    private val artistFollowRecordRepository: ArtistFollowRecordRepository
) : PlayCustomizeContract.Presenter {

    private var currentSong: Song? = null
    private var isDownloaded: Boolean = false
    private var isFollowed: Boolean = false

    override fun loadSong(songId: String) {
        view.showLoading()
        try {
            val song = songRepository.getSongById(songId)
            if (song != null) {
                currentSong = song
                view.showSong(song)
                view.updateCollectionState(false) // Simplified
                view.updateDownloadState(false) // Simplified
                view.updateFollowState(false) // Simplified
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

    override fun onDownloadClick() {
        currentSong?.let { song ->
            try {
                isDownloaded = !isDownloaded
                view.updateDownloadState(isDownloaded)
                val record = DownloadRecord(
                    downloadId = downloadRecordRepository.generateDownloadId(),
                    songId = song.songId,
                    songName = song.songName,
                    downloadTime = System.currentTimeMillis(),
                    quality = "极高",
                    isSuccess = true
                )
                downloadRecordRepository.saveDownloadRecord(record)
                val message = if (isDownloaded) "已开始下载（极高音质）" else "已取消下载"
                view.showSuccess(message)
            } catch (e: Exception) {
                view.showError("下载失败: ${e.message}")
            }
        }
    }

    override fun onFollowClick() {
        currentSong?.let { song ->
            try {
                isFollowed = !isFollowed
                view.updateFollowState(isFollowed)
                val record = ArtistFollowRecord(
                    artistId = song.artist,
                    artistName = song.artist,
                    operationType = if (isFollowed) "follow" else "unfollow",
                    operationTime = System.currentTimeMillis(),
                    isSuccess = true
                )
                artistFollowRecordRepository.saveArtistFollowRecord(record)
                val message = if (isFollowed) "已关注${song.artist}" else "已取消关注"
                view.showSuccess(message)
            } catch (e: Exception) {
                view.showError("操作失败: ${e.message}")
            }
        }
    }
    
    // Other methods remain largely the same as they are for navigation or showing unimplemented messages
    override fun onCollectionClick() { view.navigateToCollectSong() }
    override fun onShareClick() { view.close() }
    override fun onListenTogetherClick() { view.showSuccess("邀请好友一起听") }
    override fun onSongEncyclopediaClick() { view.navigateToSongProfile() }
    override fun onSimilarStrollClick() { view.showSuccess("开始相似歌曲漫游") }
    override fun onPurchaseClick() { view.showSuccess("单曲购买") }
    override fun onQualityClick() { view.showSuccess("音质设置") }
    override fun onAudioEffectClick() { view.showSuccess("音效设置") }
    override fun onPlayerStyleClick() { view.navigateToPlayerStyle() }
    override fun onCloseClick() { view.close() }
    override fun onDestroy() {}
}
