# 音乐App自动化测试框架

## 概述

本自动化测试框架用于验证音乐App的各项功能是否正常工作。通过adb命令访问App内部存储,读取JSON状态文件,自动验证任务完成情况。

## 工作原理

### 1. 状态记录
App在运行过程中,将各种状态信息记录到JSON文件中:
- `app_state.json` - 当前页面/导航状态
- `playback_state.json` - 播放状态(歌曲、播放/暂停、模式、音量)
- `user_favorites.json` - 用户收藏的歌曲
- `user_playlists.json` - 用户创建的歌单
- `collected_items.json` - 收藏的歌单和专辑
- `followed_artists.json` - 关注的歌手
- `search_history.json` - 搜索历史
- `comments.json` - 用户发表的评论
- `player_settings.json` - 播放器设置(样式、漫游场景)
- `listening_stats.json` - 听歌统计数据
- `mv_playback.json` - MV播放状态
- `task_logs.json` - 任务完成日志

这些文件存储在App的私有目录: `files/autotest/`

### 2. 验证流程
```
任务执行 → App更新状态JSON → Python脚本通过adb读取JSON → 验证任务是否完成
```

### 3. adb访问方式
```python
# 使用adb读取App内部文件
subprocess.run([
    'adb', 'exec-out', 'run-as', 'com.example.mymusic',
    'cat', 'files/autotest/app_state.json'
], stdout=open('temp_file.json', 'w'))
```

## 使用方法

### 前置要求
1. 安装Python 3.x
2. 安装Android Debug Bridge (adb)
3. 手机开启USB调试,并通过`adb devices`确认连接
4. 安装并运行音乐App

### 运行测试

#### 方式1: 单个任务测试
```python
from verification_functions import task_01_check_open_recommend_page

# 测试任务1: 打开app并进入推荐首页
result = task_01_check_open_recommend_page()
print("任务1:", "通过" if result else "失败")
```

#### 方式2: 批量测试
```python
from verification_functions import *

# 定义测试用例
test_cases = [
    ("任务1: 打开app并进入推荐首页", task_01_check_open_recommend_page),
    ("任务7: 将播放模式改为随机播放", task_07_check_shuffle_mode),
    ("任务8: 收藏歌曲song_001", lambda: task_08_check_favorite_song("song_001")),
]

# 执行测试
for name, func in test_cases:
    result = func()
    print(f"{name}: {'✓ 通过' if result else '✗ 失败'}")
```

## 任务验证函数列表

### 低难度任务 (1-10)
| 任务编号 | 任务描述 | 验证函数 |
|---------|---------|---------|
| 1 | 打开app并进入"推荐"首页 | `task_01_check_open_recommend_page()` |
| 2 | 从"推荐"页面进入"漫游"页面 | `task_02_check_navigate_to_stroll()` |
| 3 | 从"推荐"页面进入"我的"页面 | `task_03_check_navigate_to_profile()` |
| 4 | 播放当前暂停的歌曲 | `task_04_check_play_song()` |
| 5 | 暂停播放当前的歌曲 | `task_05_check_pause_song()` |
| 6 | 切换播放上一首歌曲 | `task_06_check_switch_previous_song(song_id)` |
| 7 | 将播放模式改为随机播放 | `task_07_check_shuffle_mode()` |
| 8 | 收藏当前歌曲 | `task_08_check_favorite_song(song_id)` |
| 9 | 调节当前音乐播放的音量 | `task_09_check_volume_adjusted(volume)` |
| 10 | 随机进入"我的"中的一个歌单 | `task_10_check_enter_playlist()` |

### 中难度任务 (11-21)
| 任务编号 | 任务描述 | 验证函数 |
|---------|---------|---------|
| 11 | 进入"每日推荐"播放第三首歌曲 | `task_11_check_daily_recommend_third_song()` |
| 12 | 创建一个新的歌单,并添加首音乐 | `task_12_check_create_playlist_and_add_song(name)` |
| 13 | 搜索"烟雨"并播放 | `task_13_check_search_and_play(query)` |
| 14 | 搜索某个歌手并播放其中的第一首歌 | `task_14_check_search_artist_and_play()` |
| 15 | 查看一首歌曲的详细信息 | `task_15_check_view_song_detail(song_id)` |
| 16 | 查看一首歌曲的歌词 | `task_16_check_view_lyrics()` |
| 17 | 漫游播放并设置播放场景为"伪感" | `task_17_check_stroll_scene_setting(scene)` |
| 18 | 打开一首歌曲的评论区并随机复制一条评论 | ❌ 不支持(剪贴板验证困难) |
| 19 | 在排行榜中随机选择打开一个榜单并播放第一首歌曲 | `task_19_check_rank_list_play()` |
| 20 | 在推荐歌单中随机选择一个歌单并收藏 | `task_20_check_collect_playlist(id)` |
| 21 | 删除歌单中的第一首歌 | `task_21_check_delete_song_from_playlist(id, count)` |

### 高难度任务 (22-31)
| 任务编号 | 任务描述 | 验证函数 |
|---------|---------|---------|
| 22 | 查看每周、每月听歌时长 | `task_22_check_view_listening_stats(type)` |
| 23 | 分享一首歌曲到微信朋友圈 | ❌ 不支持(外部应用交互) |
| 24 | 在歌单中选择一首歌曲并发表评论 | `task_24_check_post_comment(song_id, content)` |
| 25 | 邀请好友一起听歌 | ❌ 不支持(多用户功能) |
| 26 | 听歌识曲 | ❌ 不支持(跨应用功能) |
| 27 | 更改歌单的排序顺序 | `task_27_check_playlist_sort_order(id, order)` |
| 28 | 搜索一个歌手,在歌手主页选择一个专辑并收藏 | `task_28_check_collect_album(album_id)` |
| 29 | 将关注列表中的一位歌手删除 | `task_29_check_unfollow_artist(artist_id)` |
| 30 | 搜索一首歌曲并播放MV | `task_30_check_play_mv(mv_id)` |
| 31 | 更改播放器样式 | `task_31_check_change_player_style(style_id)` |

## App端实现要求

为了支持自动化测试,App需要在关键操作时更新对应的JSON文件。

### 示例: 更新播放状态

```kotlin
// 在播放/暂停/切歌时更新状态
fun updatePlaybackState(songId: String, isPlaying: Boolean, mode: String) {
    val stateFile = File(context.filesDir, "autotest/playback_state.json")
    val state = JSONObject().apply {
        put("currentSong", JSONObject().apply {
            put("songId", songId)
            put("songName", getSongName(songId))
            put("artist", getArtistName(songId))
        })
        put("isPlaying", isPlaying)
        put("playbackMode", mode)
        put("volume", getCurrentVolume())
        put("lastUpdated", getCurrentTimestamp())
    }
    stateFile.writeText(state.toString())
}
```

### 示例: 更新收藏状态

```kotlin
// 收藏歌曲时添加记录
fun addFavoriteSong(songId: String) {
    val favFile = File(context.filesDir, "autotest/user_favorites.json")
    val favorites = loadFavorites() // 加载现有数据
    favorites.favoriteSongs.add(mapOf(
        "songId" to songId,
        "songName" to getSongName(songId),
        "artist" to getArtistName(songId),
        "addedTime" to getCurrentTimestamp()
    ))
    favFile.writeText(Gson().toJson(favorites))
}
```

## 注意事项

1. **数据一致性**: App必须实时更新JSON文件,确保测试脚本能读取到最新状态
2. **文件权限**: JSON文件必须存储在`files/autotest/`目录下,方便adb访问
3. **格式化保存**: 建议在保存JSON时进行格式化,便于调试
4. **错误处理**: 验证函数在文件不存在或格式错误时返回False
5. **设备连接**: 确保adb能正常连接设备,可通过`adb devices`检查

## 故障排查

### 问题1: adb command not found
**解决**: 安装Android SDK Platform Tools,并将其路径添加到系统PATH

### 问题2: run-as: Package 'com.example.mymusic' is not debuggable
**解决**: 确保App是debug版本,AndroidManifest.xml中`android:debuggable="true"`

### 问题3: No such file or directory
**解决**: 确认App已创建对应的JSON文件,检查文件路径是否正确

### 问题4: JSONDecodeError
**解决**: 检查JSON文件格式是否正确,可以手动读取文件内容验证

## 扩展

可以在此基础上添加更多功能:
- 生成HTML测试报告
- 集成到CI/CD流程
- 添加性能测试指标
- 支持多设备并行测试

## 联系方式

如有问题请查看项目文档或联系开发团队。
