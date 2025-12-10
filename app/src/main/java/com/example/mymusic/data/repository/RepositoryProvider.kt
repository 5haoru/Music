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
 *         RepositoryProvider.getSongRepository(),
 *         RepositoryProvider.getPlaylistRepository()
 *     )
 * }
 * ```
 */
object RepositoryProvider {

    private var _context: Context? = null

    // Lazy初始化所有Repository
    private val _songRepository: SongRepository by lazy {
        checkInitialized()
        SongRepository(_context!!)
    }

    private val _playlistRepository: PlaylistRepository by lazy {
        checkInitialized()
        PlaylistRepository(_context!!)
    }

    private val _albumRepository: AlbumRepository by lazy {
        checkInitialized()
        AlbumRepository(_context!!)
    }

    private val _artistRepository: ArtistRepository by lazy {
        checkInitialized()
        ArtistRepository(_context!!)
    }

    private val _musicVideoRepository: MusicVideoRepository by lazy {
        checkInitialized()
        MusicVideoRepository(_context!!)
    }

    private val _collectionRepository: CollectionRepository by lazy {
        checkInitialized()
        CollectionRepository(_context!!)
    }

    private val _commentRepository: CommentRepository by lazy {
        checkInitialized()
        CommentRepository(_context!!)
    }

    private val _artistFollowRecordRepository: ArtistFollowRecordRepository by lazy {
        checkInitialized()
        ArtistFollowRecordRepository(_context!!)
    }

    private val _downloadRecordRepository: DownloadRecordRepository by lazy {
        checkInitialized()
        DownloadRecordRepository(_context!!)
    }

    private val _playerStyleRepository: PlayerStyleRepository by lazy {
        checkInitialized()
        PlayerStyleRepository(_context!!)
    }

    private val _playbackStyleRecordRepository: PlaybackStyleRecordRepository by lazy {
        checkInitialized()
        PlaybackStyleRecordRepository(_context!!)
    }

    private val _fanRepository: FanRepository by lazy {
        checkInitialized()
        FanRepository(_context!!)
    }

    private val _recognitionHistoryRepository: RecognitionHistoryRepository by lazy {
        checkInitialized()
        RecognitionHistoryRepository(_context!!)
    }

    private val _durationRepository: DurationRepository by lazy {
        checkInitialized()
        DurationRepository(_context!!)
    }

    private val _lyricRepository: LyricRepository by lazy {
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
    fun getSongRepository(): SongRepository = _songRepository
    fun getPlaylistRepository(): PlaylistRepository = _playlistRepository
    fun getAlbumRepository(): AlbumRepository = _albumRepository
    fun getArtistRepository(): ArtistRepository = _artistRepository
    fun getMusicVideoRepository(): MusicVideoRepository = _musicVideoRepository
    fun getCollectionRepository(): CollectionRepository = _collectionRepository
    fun getCommentRepository(): CommentRepository = _commentRepository
    fun getArtistFollowRecordRepository(): ArtistFollowRecordRepository = _artistFollowRecordRepository
    fun getDownloadRecordRepository(): DownloadRecordRepository = _downloadRecordRepository
    fun getPlayerStyleRepository(): PlayerStyleRepository = _playerStyleRepository
    fun getPlaybackStyleRecordRepository(): PlaybackStyleRecordRepository = _playbackStyleRecordRepository
    fun getFanRepository(): FanRepository = _fanRepository
    fun getRecognitionHistoryRepository(): RecognitionHistoryRepository = _recognitionHistoryRepository
    fun getDurationRepository(): DurationRepository = _durationRepository
    fun getLyricRepository(): LyricRepository = _lyricRepository
}
