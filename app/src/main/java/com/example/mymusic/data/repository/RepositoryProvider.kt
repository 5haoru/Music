package com.example.mymusic.data.repository

import android.content.Context

/**
 * Repository提供者 - 单例模式
 * 用于集中管理所有Repository实例，避免在Composable中直接创建Repository
 *
 * 使用方式：
 * ```kotlin
 * val presenter = remember {
 *     RepositoryProvider.initialize(context)
 *     XxxPresenter(
 *         view,
 *         RepositoryProvider.songRepository,
 *         RepositoryProvider.playlistRepository
 *     )
 * }
 * ```
 */
object RepositoryProvider {

    private var _context: Context? = null

    // Lazy初始化所有Repository
    private val songRepository: SongRepository by lazy {
        checkInitialized()
        SongRepository(_context!!)
    }

    private val playlistRepository: PlaylistRepository by lazy {
        checkInitialized()
        PlaylistRepository(_context!!)
    }

    private val albumRepository: AlbumRepository by lazy {
        checkInitialized()
        AlbumRepository(_context!!)
    }

    private val artistRepository: ArtistRepository by lazy {
        checkInitialized()
        ArtistRepository(_context!!)
    }

    private val musicVideoRepository: MusicVideoRepository by lazy {
        checkInitialized()
        MusicVideoRepository(_context!!)
    }

    private val collectionRepository: CollectionRepository by lazy {
        checkInitialized()
        CollectionRepository(_context!!)
    }

    private val commentRepository: CommentRepository by lazy {
        checkInitialized()
        CommentRepository(_context!!)
    }

    private val artistFollowRecordRepository: ArtistFollowRecordRepository by lazy {
        checkInitialized()
        ArtistFollowRecordRepository(_context!!)
    }

    private val downloadRecordRepository: DownloadRecordRepository by lazy {
        checkInitialized()
        DownloadRecordRepository(_context!!)
    }

    private val playerStyleRepository: PlayerStyleRepository by lazy {
        checkInitialized()
        PlayerStyleRepository(_context!!)
    }

    private val playbackStyleRecordRepository: PlaybackStyleRecordRepository by lazy {
        checkInitialized()
        PlaybackStyleRecordRepository(_context!!)
    }

    private val fanRepository: FanRepository by lazy {
        checkInitialized()
        FanRepository(_context!!)
    }

    private val recognitionHistoryRepository: RecognitionHistoryRepository by lazy {
        checkInitialized()
        RecognitionHistoryRepository(_context!!)
    }

    private val durationRepository: DurationRepository by lazy {
        checkInitialized()
        DurationRepository(_context!!)
    }

    private val lyricRepository: LyricRepository by lazy {
        checkInitialized()
        LyricRepository(_context!!)
    }

    /**
     * 初始化RepositoryProvider
     * 建议在Application.onCreate()中调用，或在首次使用前调用
     */
    fun initialize(context: Context) {
        if (_context == null) {
            _context = context.applicationContext
        }
    }

    private fun checkInitialized() {
        check(_context != null) {
            "RepositoryProvider未初始化，请先调用 RepositoryProvider.initialize(context)"
        }
    }

    // 公开访问器
    fun getSongRepository(): SongRepository = songRepository
    fun getPlaylistRepository(): PlaylistRepository = playlistRepository
    fun getAlbumRepository(): AlbumRepository = albumRepository
    fun getArtistRepository(): ArtistRepository = artistRepository
    fun getMusicVideoRepository(): MusicVideoRepository = musicVideoRepository
    fun getCollectionRepository(): CollectionRepository = collectionRepository
    fun getCommentRepository(): CommentRepository = commentRepository
    fun getArtistFollowRecordRepository(): ArtistFollowRecordRepository = artistFollowRecordRepository
    fun getDownloadRecordRepository(): DownloadRecordRepository = downloadRecordRepository
    fun getPlayerStyleRepository(): PlayerStyleRepository = playerStyleRepository
    fun getPlaybackStyleRecordRepository(): PlaybackStyleRecordRepository = playbackStyleRecordRepository
    fun getFanRepository(): FanRepository = fanRepository
    fun getRecognitionHistoryRepository(): RecognitionHistoryRepository = recognitionHistoryRepository
    fun getDurationRepository(): DurationRepository = durationRepository
    fun getLyricRepository(): LyricRepository = lyricRepository
}
