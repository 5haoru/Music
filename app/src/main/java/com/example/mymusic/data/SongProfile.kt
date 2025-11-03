package com.example.mymusic.data

/**
 * 歌曲档案数据类
 * 用于歌曲百科页面显示完整的歌曲信息
 */
data class SongProfile(
    val songId: String,
    val songName: String,
    val artist: String,

    // 用户听歌数据
    val firstListenTime: String?,  // 第一次听的时间（如"金秋的晚上\n2024.10.26 20:07"）
    val totalPlayCount: Int,       // 累计播放次数
    val poeticDescription: String, // 诗意描述（如"如同花香飘过1967条街"）

    // 歌曲详情
    val genre: String,             // 曲风（如"流行-华语流行"）
    val album: String,             // 专辑
    val language: String,          // 语种
    val releaseDate: String,       // 发行日期（如"2015-06-05(录音室版)"）
    val bpm: Int,                  // 每分钟节拍数

    // 扩展信息
    val production: String,        // 制作信息（作词/作曲/编曲）
    val introduction: String,      // 简介（完整内容）
    val filmTvList: List<String>,  // 影综列表
    val awardsList: List<String>,  // 奖项列表
    val scoresList: List<String>   // 乐谱列表（如"钢琴"、"吉他"）
) {
    companion object {
        /**
         * 根据播放次数生成诗意描述
         */
        fun generatePoeticDescription(playCount: Int): String {
            return when {
                playCount < 10 -> "如同微风拂过${playCount * 50}片花瓣"
                playCount < 50 -> "如同细雨洒落${playCount * 30}个窗台"
                playCount < 100 -> "如同花香飘过${playCount * 20}条街"
                playCount < 500 -> "如同星光照亮${playCount * 10}个夜晚"
                else -> "如同音符跳动${playCount * 5}次心跳"
            }
        }

        /**
         * 从Song创建SongProfile（使用默认数据）
         */
        fun fromSong(
            song: Song,
            playCount: Int = 68,
            firstListenDate: String? = "金秋的晚上\n2024.10.26 20:07"
        ): SongProfile {
            return SongProfile(
                songId = song.songId,
                songName = song.songName,
                artist = song.artist,
                firstListenTime = firstListenDate,
                totalPlayCount = playCount,
                poeticDescription = generatePoeticDescription(playCount),
                genre = "流行-华语流行",
                album = song.album,
                language = "国语",
                releaseDate = "${song.releaseYear}-06-05(录音室版)",
                bpm = 91,
                production = "作词 ${song.artist} / 作曲 ${song.artist} / 编曲 ${song.artist}",
                introduction = "《${song.songName}》由${song.artist}包揽词曲创作，并首度升级制作人，2015年${song.artist}再...",
                filmTvList = listOf(
                    "演唱会《许嵩...唱会》插曲",
                    "电影《青春派》主题曲",
                    "电视剧《爱情公寓》插曲",
                    "综艺《中国好声音》翻唱曲目",
                    "音乐节《草莓音乐节》表演曲目"
                ),
                awardsList = listOf(
                    "第1届唱工委音乐奖 年度歌曲",
                    "QQ音乐年度盛典 十大金曲",
                    "酷狗音乐盛典 年度最佳单曲",
                    "网易云音乐盛典 年度热歌",
                    "华语音乐传媒大奖 年度歌曲"
                ),
                scoresList = listOf("钢琴", "吉他", "吉他", "吉他", "尤克里里", "架子鼓", "贝斯", "小提琴", "口琴", "古筝")
            )
        }
    }
}
