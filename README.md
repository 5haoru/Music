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
│   │   ├── RecommendPresenter.kt      # 推荐页面Presenter
│   │   ├── DailyRecommendPresenter.kt # 每日推荐Presenter
│   │   ├── RankListPresenter.kt       # 排行榜Presenter
│   │   ├── StrollPresenter.kt         # 漫游页面Presenter
│   │   ├── PlayPresenter.kt           # 播放页面Presenter
│   │   └── MePresenter.kt             # 我的页面Presenter
│   ├── view/                    # MVP View层
│   │   ├── RecommendTab.kt         # 推荐页面容器（包含子标签）
│   │   ├── DailyRecommendTab.kt    # 每日推荐子页面（已完成）
│   │   ├── RankListTab.kt          # 排行榜页面（已完成）
│   │   ├── StrollTab.kt            # 漫游页面（已完成）
│   │   ├── PlayTab.kt              # 播放页面（已完成）
│   │   └── MeTab.kt                # 我的页面（已完成）
│   ├── ui/components/           # UI组件
│   │   ├── TopBar.kt           # 顶部搜索栏
│   │   ├── SongItem.kt         # 歌曲列表项
│   │   ├── DailySongItem.kt    # 每日推荐歌曲项（带标签）
│   │   ├── PlaylistCard.kt     # 歌单卡片（含大尺寸卡片）
│   │   ├── FeaturedRankCard.kt # 特色榜单卡片（渐变色）
│   │   └── OfficialRankItem.kt # 官方榜单列表项
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
- [x] 底部导航栏（4个Tab，漫游图标使用音符）
- [x] 数据加载工具
- [x] JSON数据定义
- [x] 19个数据记录模型
- [x] 4个页面框架
- [x] **推荐页面完整实现** (RecommendTab)
  - [x] 顶部搜索栏（含搜索框、菜单、设置按钮）
  - [x] 每日推荐模块（水平滚动大卡片，点击进入详情页）
  - [x] 歌曲推荐列表（带封面、标题、艺术家、播放按钮）
  - [x] 精选歌单区（水平滚动卡片）
  - [x] 排行榜区（水平滚动卡片，自定义榜单名：ACG榜、日语榜、国风榜）
  - [x] 排行榜标题右侧">"图标入口（点击进入排行榜详情页）
  - [x] **每日推荐详情页** (DailyRecommendTab - 二级页面)
    - [x] 顶部模式切换（默认推荐/风格推荐）
    - [x] 返回按钮（返回推荐页）
    - [x] 日期显示（大字号21/10格式）
    - [x] 查看今日运势入口
    - [x] 历史日推入口（带VIP标识）
    - [x] 播放全部按钮（红色圆形播放图标）
    - [x] 歌曲列表（序号、封面、歌名/歌手、标签、播放按钮）
    - [x] 动态标签系统（VIP、超清母带、播放热度、小众佳作等）
  - [x] **排行榜详情页** (RankListTab - 二级页面)
    - [x] 顶部标题栏（返回按钮、"排行榜"标题、更多按钮）
    - [x] Tab切换栏（排行榜/歌单/原创）
    - [x] 榜单推荐区（水平滚动特色榜单卡片）
    - [x] 特色榜单卡片（渐变色背景：硬地原创音乐榜、潮流风向榜、国风榜）
    - [x] 官方榜区域标题（红色圆点图标 + "官方榜"）
    - [x] 官方榜列表项（飙升榜、新歌榜、热歌榜）
    - [x] 榜单项左侧封面（100dp，带播放按钮覆盖层）
    - [x] 榜单项右侧信息（榜单名、更新频率、TOP3歌曲）
    - [x] TOP3歌曲显示（排名、歌名-歌手、状态标识）
    - [x] 歌曲状态标识（新歌"新"绿色标签、上升红色△、下降绿色▽）
- [x] **漫游页面完整实现**
  - [x] 顶部栏（返回按钮、"私人漫游·伤感"标题、分享按钮）
  - [x] 专辑封面展示（使用assets/cover图片，圆角卡片）
  - [x] 歌曲信息区（歌曲名、艺术家、关注按钮）
  - [x] 互动统计（点赞数999+、评论数399，点赞状态切换）
  - [x] 播放进度条（带当前时间/总时长/极高音质标识）
  - [x] 播放控制栏（无限循环、上一曲、播放/暂停、下一曲、播放列表）
  - [x] 次要功能栏（音效、评论、详情、更多）
  - [x] 随机歌曲切换功能（漫游模式）
  - [x] 自动播放进度推进（每秒更新）
- [x] **播放页面完整实现**
  - [x] 全屏显示（无底部导航栏）
  - [x] 顶部栏（返回按钮、"播放"标题、刷新按钮）
  - [x] 与漫游页面相同的UI布局
  - [x] 专辑封面、歌曲信息、进度条、播放控制
  - [x] 完整的播放功能（播放/暂停、上一曲/下一曲）
  - [x] **三种播放模式**：
    - [x] 顺序播放（按列表顺序播放）
    - [x] 单曲循环（重复播放当前歌曲）
    - [x] 随机播放（随机选择歌曲）
  - [x] 播放模式动态图标切换（🔁/🔂/🔀）
  - [x] 自动播放进度推进和歌曲切换
- [x] **UI组件库**
  - [x] RecommendTopBar - 推荐页顶部栏
  - [x] SongItem - 歌曲列表项组件
  - [x] DailySongItem - 每日推荐歌曲项（带丰富标签）
  - [x] PlaylistCard - 歌单卡片组件
  - [x] LargePlaylistCard - 大尺寸歌单卡片
  - [x] FeaturedRankCard - 特色榜单卡片（渐变色背景）
  - [x] OfficialRankItem - 官方榜单列表项（封面+TOP3歌曲）
- [x] **Presenter层**
  - [x] RecommendPresenter - 推荐页面业务逻辑
  - [x] DailyRecommendPresenter - 每日推荐业务逻辑（歌曲列表加载）
  - [x] RankListPresenter - 排行榜业务逻辑（榜单数据加载、状态模拟）
  - [x] StrollPresenter - 漫游页面业务逻辑（随机歌曲选择、播放状态管理）
  - [x] PlayPresenter - 播放页面业务逻辑（随机歌曲选择、播放状态管理）
  - [x] MePresenter - 我的页面业务逻辑（用户数据、歌单管理）
- [x] **我的页面完整实现**（简化版，参考网易云音乐设计）
  - [x] 顶部栏（汉堡菜单、小头像+用户名、搜索、更多按钮）
  - [x] 用户信息区域（大头像、用户名、VIP徽章、等级徽章）
  - [x] 统计信息（关注/粉丝/等级/听歌时长）- 可点击进入关注列表
  - [x] 功能按钮组（最近、本地、网盘、装扮、更多）
  - [x] 歌单管理栏（近期🔒、创建²、收藏³、播放全部、更多）
  - [x] 我喜欢的音乐特殊卡片（蓝色渐变+心形图标、播放次数、心动模式按钮）
  - [x] 我的歌单列表（带封面、歌曲数量、更多选项）
  - [x] 新建歌单选项（+ 图标）
  - [x] 导入外部歌单选项
  - [x] 底部提示文字

### 🚧 待开发
- [ ] 底部播放控制栏（全局组件）

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
implementation("androidx.compose.material:material-icons-extended:1.7.6")

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

### 2025-10-21 (最新)
- ✅ 完成排行榜详情页面（RankListTab）作为二级页面
  - 创建RankListContract和RankListPresenter（MVP模式）
  - 实现顶部标题栏（返回按钮、"排行榜"标题、更多按钮）
  - 实现Tab切换栏（排行榜/歌单/原创）
  - 创建特色榜单推荐区（水平滚动）
  - 实现FeaturedRankCard组件（160dp x 180dp，渐变色背景）
  - 创建三个特色榜单：硬地原创音乐榜、潮流风向榜、国风榜
  - 实现官方榜区域（红色圆点图标 + "官方榜"标题）
  - 创建OfficialRankItem组件（左侧封面+右侧TOP3歌曲）
  - 实现三个官方榜单：飙升榜、新歌榜、热歌榜
  - 实现TOP3歌曲显示（排名、歌名-歌手、状态标识）
  - 实现歌曲状态标识系统（新歌"新"绿色标签、上升红色△、下降绿色▽）
  - 修改RecommendTab：排行榜区域自定义名称（ACG榜、日语榜、国风榜）
  - 添加SectionTitleWithAction组件（标题 + ">" 图标）
  - 实现从推荐页进入排行榜详情的导航（点击">"图标）
  - 使用状态管理实现页面导航（showRankListDetail）
  - 成功构建并验证功能（仅deprecation警告，无错误）
- ✅ 完成每日推荐详情页面（DailyRecommendTab）作为二级页面
  - 创建DailyRecommendContract和DailyRecommendPresenter（MVP模式）
  - 实现顶部模式切换栏（默认推荐/风格推荐）
  - 添加返回按钮（返回推荐页主页）
  - 实现日期显示区域（21/10格式大字号）
  - 添加查看今日运势入口
  - 添加历史日推入口（带VIP标识）
  - 创建播放全部功能区（红色圆形播放按钮+歌曲数量）
  - 实现DailySongItem组件（带序号、封面、歌曲信息、动态标签）
  - 实现动态标签系统（VIP、超清母带、Hi-Res、播放热度、小众佳作、新歌等）
  - 修复页面层级：RecommendTab保持原有内容，点击"每日推荐"卡片进入DailyRecommendTab
  - 使用状态管理实现页面导航（无需复杂路由）
  - 成功构建并验证功能（仅deprecation警告，无错误）

### 2025-10-20
- ✅ 简化我的页面（MeTab）- 移除冗余组件
  - 移除底部Mini播放器
  - 移除用户简介（info）
  - 移除Tab切换（音乐/播客/笔记）
  - 优化顶部空隙（使用LazyColumn统一布局）
  - 简化顶部栏为普通Row布局（移除TopAppBar）
  - 成功构建并验证功能
- ✅ 优化我的页面（MeTab）UI设计 - 参考UI Reference截图
  - 重构用户信息区域（大头像、VIP徽章、等级徽章）
  - 修改"关注"为数字显示格式（"6 关注"），作为进入关注列表的入口
  - 修改听歌数量为时长显示（"1693 小时"）
  - 添加完整统计信息行（关注·粉丝·等级·时长）
  - 添加顶部栏（汉堡菜单、小头像+用户名、搜索、更多）
  - 添加功能按钮组（最近、本地、网盘、装扮、更多 - 5个圆角按钮）
  - 添加Tab切换（音乐、播客、笔记）
  - 添加歌单管理栏（近期🔒、创建²、收藏³、播放全部、更多）
  - 优化"我喜欢的音乐"卡片（蓝色渐变背景、心形图标、播放次数、心动模式按钮）
  - 优化歌单列表项显示（歌单·X首·创建者）
  - 添加"新建歌单"和"导入外部歌单"选项
  - 添加底部提示文字
  - 成功构建并验证功能
- ✅ 完成我的页面（MeTab）完整开发（初版）
  - 实现MePresenter及契约接口
  - 创建用户信息区域（圆形头像、用户名、等级徽章）
  - 显示累计听歌数量和关注按钮
  - 实现"我喜欢的音乐"特殊卡片（红心图标）
  - 创建我的歌单列表（带封面、歌曲数量、跳转箭头）
  - 添加"新建歌单"功能按钮
  - 实现底部Mini播放器组件（当前播放歌曲信息、封面、播放控制）
  - Mini播放器点击跳转到播放页面功能
  - 从assets/avatar加载用户头像
  - 成功构建并验证功能
- ✅ 完成播放页面三种播放模式功能
  - 实现PlayMode枚举（顺序播放、单曲循环、随机播放）
  - 播放模式循环切换逻辑
  - 根据播放模式动态切歌（顺序/循环/随机）
  - 播放模式图标动态显示（Repeat/RepeatOne/Shuffle）
  - 播放页面全屏显示（隐藏底部导航栏）
  - 顶部标题改为"播放"
  - 成功构建并验证功能
- ✅ 完成播放页面完整开发
  - 实现PlayPresenter及契约接口
  - 复用漫游页面UI结构
  - 完整的播放功能（播放/暂停、上一曲/下一曲、自动进度）
  - 成功构建并验证功能
- ✅ 完成漫游页面完整开发
  - 实现StrollPresenter及契约接口
  - 创建完整的音乐播放器UI界面
  - 实现随机歌曲选择功能（漫游模式核心）
  - 添加播放控制（播放/暂停、上一曲/下一曲）
  - 实现收藏状态切换和进度条交互
  - 使用Coil加载assets中的专辑封面
  - 使用Material Icons Extended中的音符图标
  - 自动播放进度推进功能
  - 成功构建并验证功能

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
