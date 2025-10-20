# MyMusic - 网易云音乐克隆项目

这是一个仿网易云音乐的Android应用项目，用于GUI Agent任务完成情况检查研究。

## 项目概述

本项目是一个简化版的音乐播放应用，主要用于测试和验证AI Agent在移动应用中的操作能力。应用采用MVP架构模式，使用Jetpack Compose构建UI。

### 主要特点

- ✅ **无需网络**: 所有数据从本地assets目录加载
- ✅ **无需登录**: 直接使用，无需用户认证
- ✅ **固定数据**: 歌曲、歌单等数据预先定义，不会动态变化
- ✅ **任务追踪**: 记录用户操作（播放、收藏、评论等）用于AI任务验证

## 技术栈

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVP (Model-View-Presenter)
- **Data Format**: JSON
- **Image Loading**: Coil
- **JSON Parsing**: Gson

## 项目结构

```
app/src/main/
├── java/com/example/mymusic/
│   ├── MainActivity.kt          # 主Activity，包含底部导航
│   ├── data/                    # 数据类
│   │   ├── Song.kt
│   │   ├── Playlist.kt
│   │   ├── Artist.kt
│   │   └── User.kt
│   ├── model/                   # 记录数据结构
│   │   ├── PlayRecord.kt
│   │   ├── CommentRecord.kt
│   │   ├── ShareRecord.kt
│   │   └── ... (其他19个记录类)
│   ├── presenter/               # MVP Presenter层
│   │   ├── BasePresenter.kt
│   │   ├── MainPresenter.kt
│   │   └── RecommendPresenter.kt  # 推荐页面Presenter
│   ├── view/                    # MVP View层
│   │   ├── RecommendTab.kt     # 推荐页面
│   │   ├── StrollTab.kt        # 漫游页面（空页面）
│   │   ├── PlayTab.kt          # 播放页面（空页面）
│   │   └── MeTab.kt            # 我的页面（空页面）
│   ├── ui/components/           # UI组件
│   │   ├── TopBar.kt           # 顶部搜索栏
│   │   ├── SongItem.kt         # 歌曲列表项
│   │   └── PlaylistCard.kt     # 歌单卡片（含大尺寸卡片）
│   ├── utils/                   # 工具类
│   │   └── DataLoader.kt       # 数据加载工具
│   └── ui/theme/                # UI主题
└── assets/
    ├── data/                    # JSON数据文件
    │   ├── songs.json          # 15首歌曲
    │   ├── playlists.json      # 7个歌单
    │   ├── artists.json        # 7位艺术家
    │   ├── users.json          # 5个用户
    │   └── *_records.json      # 19个记录文件（初始为空）
    └── cover/                   # 封面图片
        └── *.png               # 歌曲/歌单封面
```

## 数据说明

### 基础数据
- **歌曲库**: 包含15首经典华语歌曲（周杰伦、五月天、薛之谦等）
- **歌单**: 包含每日推荐、热歌榜、新歌榜等7个歌单
- **艺术家**: 7位流行歌手信息
- **用户**: 5个模拟用户账号

### 记录数据
应用会追踪以下操作并保存到JSON文件：
- 播放记录、评论记录、分享记录
- 收藏记录、导航记录、搜索记录
- 音量调节、播放模式切换、歌单管理
- 以及其他16种操作记录

## 当前开发状态

### ✅ 已完成
- [x] 项目基础架构（MVP模式）
- [x] 底部导航栏（4个Tab）
- [x] 数据加载工具
- [x] JSON数据定义
- [x] 19个数据记录模型
- [x] 4个页面框架
- [x] **推荐页面完整实现**
  - [x] 顶部搜索栏（含搜索框、菜单、设置按钮）
  - [x] 每日推荐模块（水平滚动大卡片）
  - [x] 歌曲推荐列表（带封面、标题、艺术家、播放按钮）
  - [x] 精选歌单区（水平滚动卡片）
  - [x] 排行榜区（水平滚动卡片）
- [x] **UI组件库**
  - [x] RecommendTopBar - 推荐页顶部栏
  - [x] SongItem - 歌曲列表项组件
  - [x] PlaylistCard - 歌单卡片组件
  - [x] LargePlaylistCard - 大尺寸歌单卡片
- [x] **RecommendPresenter** - 推荐页面业务逻辑

### 🚧 待开发
- [ ] 底部播放控制栏（全局组件）
- [ ] 漫游页面
- [ ] 播放页面
- [ ] 我的页面

## 构建项目

```bash
# 清理构建
./gradlew clean

# 构建项目
./gradlew build

# 安装到设备
./gradlew installDebug
```

## 依赖库

```kotlin
// Jetpack Compose
implementation("androidx.compose.material3:material3")
implementation("androidx.activity:activity-compose")

// JSON解析
implementation("com.google.code.gson:gson:2.10.1")

// 图片加载
implementation("io.coil-kt:coil-compose:2.5.0")
```

## 开发规范

1. **遵循MVP架构**: View负责UI展示，Presenter处理业务逻辑
2. **避免长文件**: 单个文件不超过500行
3. **使用Compose**: 所有UI使用Jetpack Compose构建
4. **数据驱动**: 所有数据从assets/data目录加载

## 许可证

本项目仅用于研究和学习目的。

## 更新日志

### 2025-10-18
- ✅ 完成推荐页面详细开发
  - 实现RecommendPresenter及契约接口
  - 创建TopBar、SongItem、PlaylistCard三个可复用组件
  - 完成RecommendTab所有UI模块（每日推荐、歌曲列表、歌单推荐、排行榜）
  - 实现加载状态和错误处理
  - 成功构建并验证功能

### 2025-10-17
- 初始化项目结构
- 实现MVP架构基础
- 创建4个Tab页面框架
- 添加数据加载工具
- 定义JSON数据结构
- 完成首次成功构建
