package com.example.mymusic.data.repository

import com.example.mymusic.data.Song
import kotlin.random.Random

class RecognitionRepository {

    /**
     * 模拟听歌识曲过程，返回识别到的歌曲
     * 实际应用中会调用AI接口进行识别
     */
    fun recognizeSong(): Song {
        // 模拟识别逻辑，这里简单返回一个预设的歌曲
        val songs = listOf(
            Song("song_001", "Imagine", "John Lennon", "Imagine", 200000L, "cover/1.png", "Imagine there's no heaven...", 1971),
            Song("song_002", "Bohemian Rhapsody", "Queen", "A Night at the Opera", 300000L, "cover/2.png", "Is this the real life? Is this just fantasy?...", 1975),
            Song("song_003", "Billie Jean", "Michael Jackson", "Thriller", 250000L, "cover/3.png", "She was more like a beauty queen from a movie scene...", 1982)
        )
        return songs[Random.nextInt(songs.size)]
    }
}
