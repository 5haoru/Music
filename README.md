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
│   │   ├── SongDetail.kt        # 歌曲详细信息（用于搜索结果）
│   │   ├── MusicVideo.kt        # MV数据类
│   │   ├── Comment.kt           # 评论数据类
│   │   ├── Playlist.kt
│   │   ├── Artist.kt
│   │   ├── User.kt
│   │   ├── SortOrderRecord.kt   # 歌曲排序记录
│   │   └── SongDeletionRecord.kt # 歌曲删除记录
│   ├── model/                   # 记录数据结构
│   │   ├── PlayRecord.kt
│   │   ├── CommentRecord.kt
│   │   ├── ShareRecord.kt
│   │   └── ... (其他19个记录类)
│   ├── presenter/               # MVP Presenter层
│   │   ├── BasePresenter.kt
│   │   ├── MainPresenter.kt
│   │   ├── RecommendPresenter.kt           # 推荐页面Presenter
│   │   ├── DailyRecommendPresenter.kt      # 每日推荐Presenter
│   │   ├── RankListPresenter.kt            # 排行榜Presenter
│   │   ├── SearchPresenter.kt              # 搜索页面Presenter
│   │   ├── SearchResultPresenter.kt        # 搜索结果Presenter
│   │   ├── ListenRecognizePresenter.kt     # 听歌识曲Presenter
│   │   ├── ModeSelectionPresenter.kt       # 模式选择Presenter
│   │   ├── StrollPresenter.kt              # 漫游页面Presenter
│   │   ├── PlayPresenter.kt                # 播放页面Presenter
│   │   ├── CommentPresenter.kt             # 评论页面Presenter
│   │   ├── SharePresenter.kt               # 分享页面Presenter
│   │   ├── LyricPresenter.kt               # 歌词页面Presenter
│   │   ├── SubscribePresenter.kt           # 关注页面Presenter
│   │   ├── MePresenter.kt                  # 我的页面Presenter
│   │   ├── SongSortPresenter.kt            # 歌曲排序Presenter
│   │   ├── SongDelPresenter.kt             # 歌曲删除Presenter
│   │   └── CollectSongPresenter.kt         # 收藏到歌单Presenter
│   ├── view/                    # MVP View层
│   │   ├── RecommendTab.kt         # 推荐页面容器（包含子标签）
│   │   ├── DailyRecommendTab.kt    # 每日推荐子页面（已完成）
│   │   ├── RankListTab.kt          # 排行榜页面（已完成）
│   │   ├── SearchTab.kt            # 搜索页面（已完成）
│   │   ├── SearchResultTab.kt      # 搜索结果页面（已完成）
│   │   ├── ListenRecognizeTab.kt   # 听歌识曲页面（已完成）
│   │   ├── ModeSelectionTab.kt     # 模式选择页面（已完成）
│   │   ├── StrollTab.kt            # 漫游页面（已完成）
│   │   ├── PlayTab.kt              # 播放页面（已完成）
│   │   ├── CommentTab.kt           # 评论页面（已完成）
│   │   ├── ShareTab.kt             # 分享页面（已完成）
│   │   ├── LyricTab.kt             # 歌词页面（已完成）
│   │   ├── SubscribeTab.kt         # 关注页面（已完成）
│   │   ├── MeTab.kt                # 我的页面（已完成）
│   │   ├── SongSortTab.kt          # 歌曲排序页面（已完成）
│   │   ├── SongDelTab.kt           # 歌曲删除页面（已完成）
│   │   └── CollectSongTab.kt       # 收藏到歌单页面（已完成）
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
    │   ├── music_videos.json   # 10个MV数据
    │   ├── playlists.json      # 11个歌单（含我喜欢的音乐+3个特色歌单）
    │   ├─��� artists.json        # 7位艺术家
    │   ├── users.json          # 5个用户
    │   ├── comments.json       # 评论数据
    │   ├── sort_order_records.json      # 排序记录（初始为空）
    │   ├── song_deletion_records.json   # 删除记录（初始为空）
    │   └── *_records.json      # 其他记录文件（初始为空）
    └── cover/                   # 封面图片
        └── *.png               # 歌曲/歌单封面
```

## 数据说明

### 基础数据
- **歌曲库**: 包含15首经典华语歌曲（周杰伦、五月天、薛之谦等）
- **歌单**: 包含每日推荐、热歌榜、新歌榜等11个歌单（含"我喜欢的音乐"和3个特色动漫音乐歌单）
- **艺术家**: 7位流行歌手信息
- **用户**: 5个模拟用户账号

### 记录数据
应用会追踪以下操作并保存到JSON文件：
- 播放记录、评论记录、分享记录
- 收藏记录、导航记录、搜索记录
- 音量调节、播放模式切换、歌单管理
- 以及其他16种操作记录

### 拼音搜索功能
为适应离线环境（无法下载中文输入法语言包），本应用支持使用**拼音搜索中文歌曲**：

**使用方法**：
- 在搜索框中直接输入拼音即可搜索歌曲
- 例如：输入 `yanyuan` 可搜索到 "演员"（薛之谦）
- 例如：输入 `qingtian` 可搜索到 "晴天"（周杰伦）
- 例如：输入 `zhoujielen` 可搜索到周杰伦的所有歌曲

**技术实现**：
- Song数据类包含 `pinyin` 和 `artistPinyin` 字段
- songs.json中为每首歌曲预置了拼音数据
- 搜索逻辑同时匹配歌名、艺术家、歌名拼音、艺术家拼音
- 完全离线工作，无需任何网络连接

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
    - [x] 点击榜单卡片导航到榜单详情页（RankTab）
  - [x] **榜单详情页** (RankTab - 三级页面)
    - [x] 顶部导航栏（返回按钮、"歌单"标题、搜索、更多按钮）
    - [x] **榜单头部信息区**：
      - [x] 歌单封面（带播放次数显示，如"4000万"）
      - [x] 榜单名称（如"网易云日语榜"）
      - [x] 官方认证标识（"网易云音乐"+ 关注按钮）
      - [x] 更新时间显示（如"10月28日更新"）
    - [x] **描述区域**：榜单描述文字 + 展开/收起按钮
    - [x] **互动按钮行**：
      - [x] 分享按钮（显示分享数）
      - [x] 评论按钮（显示评论数）
      - [x] 点赞按钮（金色背景，显示点赞数"11.6万"格式）
    - [x] **播放控制栏**：
      - [x] 播放全部按钮（金色圆形图标）
      - [x] 歌曲数量显示"(100)"
      - [x] 下载图标和列表图标
    - [x] **歌曲列表**（100首）：
      - [x] 排名序号（1-100，前3名红色加粗）
      - [x] 排名变化指示器（NEW标签、▲上升、▼下降、稳定无标识）
      - [x] 收藏状态（红心图标）
      - [x] 音质标签（"超清母带"等）
      - [x] 歌曲名和歌手信息
      - [x] NEW标签（新上榜歌曲）
      - [x] 播放按钮和更多按钮
    - [x] 点击歌曲导航到播放页面
    - [x] 快速收藏功能（添加到"我喜欢的音乐"）
    - [x] MVP架构（RankContract、RankPresenter）
    - [x] 随机生成排名变化数据
    - [x] 循环使用15首歌曲填充100首列表
  - [x] **搜索页面** (SearchTab - 二级页面)
    - [x] 顶部搜索栏（返回按钮、搜索输入框、搜索图标）
    - [x] 快捷入口区（5个均匀分布：歌手、曲风、专区、识曲、听书）
    - [x] 搜索历史显示（最多显示最近3条，可清空）
    - [x] 猜你喜欢版块（推荐6首歌曲，2列网格布局）
    - [x] 双榜单设计（热搜榜和热歌榜并列显示）
    - [x] 热搜榜（1-10名歌曲，第1名带"爆"标签，第2名带火焰图标）
    - [x] 热歌榜（1-10名歌曲，带上升/下降趋势箭头）
    - [x] 实时搜索功能（输入关键词过滤歌曲）
    - [x] 搜索结果显示（歌曲列表展示）
    - [x] 搜索历史记录保存（JSON格式保存到本地）
    - [x] 拼音搜索支持（无需中文输入法，可使用拼音搜索中文歌曲）
  - [x] **听歌识曲页面** (ListenRecognizeTab - 二级页面)
    - [x] 顶部栏（返回按钮、"听歌识曲"标题、历史按钮、更多按钮）
    - [x] 功能提示条（"开启桌面悬浮窗，边刷视频边识曲" + Switch开关）
    - [x] 中心录音区域（大型红色圆形按钮，带粉色光晕效果）
    - [x] 麦克风图标（白色，居中显示）
    - [x] 录音状态显示（"录音中(Xs)" / "点击开始识曲"）
    - [x] 操作提示文字（"点击停止识曲"）
    - [x] 底部模式切换（听歌识曲 / 哼唱识曲，圆角按钮）
    - [x] 自动录音（进入页面自动开始录音）
    - [x] 录音计时器（每秒更新时长显示）
    - [x] 点击切换录音状态（开始/停止）
    - [x] 从推荐页面麦克风按钮进入
- [x] **评论页面完整实现**
  - [x] 顶部标签切换（评论/笔记，显示数量统计）
  - [x] 歌曲信息显示（封面、歌名、艺术家、评论总数）
  - [x] 排序筛选栏（推荐/最热/最新三种排序方式）
  - [x] 评论列表（用户头像、昵称、VIP标识、评论内容、时间、点赞数、回复数）
  - [x] 评论点赞功能（点击爱心图标切换点赞状态）
  - [x] 长评论折叠功能（展开/收起按钮）
  - [x] 底部话题标签显示（金曲捞、耳机常驻歌曲等）
  - [x] 评论输入框（带"随乐而起,有爱评论"提示文案和发送按钮）
  - [x] **发送评论功能**（保存到本地JSON文件）
  - [x] Toast提示功能（成功/失败提示）
  - [x] 评论发送后自动清空输入框
  - [x] 新评论立即显示在评论列表
  - [x] 评论记录保存（comment_records.json）
  - [x] 从播放页面评论按钮进入
  - [x] 10条模拟评论数据（comments.json）
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
  - [x] **快速收藏到"我喜欢的音乐"功能**（点击爱心按钮直接收藏，Toast提示成功）
  - [x] 收藏记录保存到collection_records.json
- [x] **播放页面完整实现**
  - [x] 全屏显示（无底部导航栏）
  - [x] 顶部栏（返回按钮、"播放"标题、分享按钮）
  - [x] 与漫游页面相同的UI布局
  - [x] 专辑封面、歌曲信息、进度条、播放控制
  - [x] 完整的播放功能（播放/暂停、上一曲/下一曲）
  - [x] **快速收藏到"我喜欢的音乐"功能**（点击爱心按钮直接收藏，Toast提示成功）
  - [x] 收藏记录保存到collection_records.json
  - [x] 支持从搜索结果页面指定歌曲进入播放
  - [x] **三种播放模式**：
    - [x] 顺序播放（按列表顺序播放）
    - [x] 单曲循环（重复播放当前歌曲）
    - [x] 随机播放（随机选择歌曲）
  - [x] 播放模式动态图标切换（🔁/🔂/🔀）
  - [x] 自动播放进度推进和歌曲切换
  - [x] **播放定制页面**（PlayCustomizeTab - Overlay浮层）
    - [x] 从底部功能栏"更多"按钮进入
    - [x] 半透明黑色遮罩背景（60%透明度）
    - [x] Overlay层叠架构（覆盖在播放页面上）
    - [x] **顶部歌曲信息区域**（封面缩略图、歌名、歌手、VIP标识）
    - [x] **第一层核心功能**（4个按钮横排）
      - [x] 收藏功能（爱心图标，跳转到CollectSongTab）
      - [x] 下载功能（保存到download_records.json，显示"极高VIP"标签）
      - [x] 分享功能（跳转到ShareTab）
      - [x] 一起听功能
    - [x] **第二层详细信息**
      - [x] 专辑信息（专辑名、歌手名）
      - [x] 歌手信息（带"+关注"按钮，保存到artist_follow_records.json）
    - [x] **第三层扩展功能**（3个列表项）
      - [x] 查看歌曲百科
      - [x] 开始相似歌曲漫游
      - [x] 单曲购买
    - [x] **第四层播放设置**（3个列表项）
      - [x] 音质设置（极高 VIP，红点图标）
      - [x] 音效设置
      - [x] 播放器样式设置
    - [x] Toast提示操作成功
    - [x] 点击背景关闭浮层
    - [x] MVP架构（PlayCustomizeContract、PlayCustomizePresenter）
  - [x] **收藏到歌单页面**（CollectSongTab - 全屏页面）
    - [x] 从PlayCustomizeTab的"收藏"按钮进入
    - [x] 顶部导航栏（返回按钮、"收藏到歌单"标题、常用/多选按钮）
    - [x] 新建歌单按钮（位于列表顶部，带加号图标）
    - [x] **歌单列表展示**（垂直滚动列表）
      - [x] 歌单封面缩略图（56dp，圆角）
      - [x] 歌单名称和歌曲数量（例如："153首"）
      - [x] "已添加"状态标签（歌曲已在歌单中时显示）
    - [x] 点击歌单收藏歌曲功能
    - [x] 收藏成功Toast提示（"已成功收藏到「歌单名」"）
    - [x] 收藏记录保存（collection_records.json）
    - [x] MVP架构（CollectSongContract、CollectSongPresenter）
    - [x] 浅色Material Design主题
- [x] **分享页面完整实现**（Overlay架构）
  - [x] **半透明深色蒙版背景**（60%透明度，覆盖播放界面）
  - [x] **Overlay层叠架构**（作为PlayTab的覆盖层，非独立页面）
  - [x] **Material Icons图标**（使用Material Design图标替代emoji）
  - [x] "分享至"区域（5个彩色圆形图标）
    - [x] 网易云笔记（EditNote图标，红色）
    - [x] 朋友圈（CameraAlt图标，绿色）
    - [x] 微信好友（Chat图标，绿色）
    - [x] QQ空间（Star图标，黄色）
    - [x] QQ好友（Group图标，蓝色）
  - [x] "你还可以分享"区域（3个卡片式布局）
    - [x] 邀请好友一起听（GroupAdd图标）
    - [x] 送好友会员天数（CardGiftcard图标，带"限时"红色标签）
    - [x] 微信状态（显示歌曲封面）
  - [x] 点击分享平台保存记录到share_records.json
  - [x] Toast提示分享成功
  - [x] 点击背景关闭分享页面
  - [x] 从播放页面右上角分享按钮触发
  - [x] **内部状态管理**（PlayTab内部管理showShare状态，无需MainActivity导航）
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
  - [x] SearchPresenter - 搜索页面业务逻辑（搜索功能、历史记录管理）
  - [x] StrollPresenter - 漫游页面业务逻辑（随机歌曲选择、播放状态管理）
  - [x] PlayPresenter - 播放页面业务逻辑（随机歌曲选择、播放状态管理）
  - [x] PlayCustomizePresenter - 播放定制页面业务逻辑（收藏、下载、关注等操作记录保存）
  - [x] SharePresenter - 分享页面业务逻辑（分享记录保存）
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
- [x] **歌词页面完整实现**（LyricTab - 二级页面）
  - [x] 从播放页面点击封面图片进入
  - [x] **顶部信息区域**
    - [x] 标题栏（返回按钮、VIP标识、歌曲名、刷新按钮）
    - [x] 歌手信息（歌手名、关注按钮）
    - [x] 歌曲百科入口
    - [x] 制作信息展示（作词、作曲、编曲、制作人）
  - [x] **中部歌词展示区域**
    - [x] LazyColumn滚动歌词列表
    - [x] 当前歌词高亮显示（大号字体、加粗）
    - [x] 上下文歌词渐变透明度（当前行1.0，前后行0.6，其他0.3）
    - [x] 时间戳显示（左侧）
    - [x] 播放图标（当前行右侧）
    - [x] 自动滚动到当前歌词
  - [x] **底部播放控制区域**
    - [x] 互动按钮行（更多、红心收藏、麦克风）
    - [x] 进度条（显示当前时间、音质标识、总时长）
    - [x] 播放控制栏（播放模式、上一曲、播放/暂停、下一曲、播放列表）
    - [x] 次要功能栏（评论、音效、详情、更多）
  - [x] MVP架构（LyricContract、LyricPresenter）
  - [x] 歌词解析功能（按换行符分割）
  - [x] 播放状态同步
  - [x] 收藏功能
- [x] **关注页面完整实现**（SubscribeTab - 二级页面）
  - [x] 从我的页面"关注"按钮进入
  - [x] **顶部导航栏**（三栏标签切换：推荐/关注/粉丝）
  - [x] 返回按钮和搜索按钮
  - [x] **筛选栏**（按关注时间排序、类型筛选：全部/歌手/用户）
  - [x] **聚合更新横幅**（显示多位艺人的新动态）
  - [x] **关注列表**（用户头像、昵称、VIP标识、动态信息）
  - [x] VIP徽章系统（SVIP金色、VIP蓝色、黑胶SVIP）
  - [x] 活动状态显示（发布MV、发布动态等）
  - [x] 推荐区域（底部推荐卡片）
  - [x] **取消关注功能**（UnfollowDialog）
    - [x] 点击列表项右侧"更多"按钮（三个点）弹出对话框
    - [x] 提示文本："取消关注后对方将从你的关注列表中移除"
    - [x] 取消关注按钮（PersonRemove图标 + "取消关注"文字）
    - [x] Material3 AlertDialog组件
    - [x] 取消关注后立即从列表中移除
    - [x] 内存中实时更新，保持当前筛选状态
  - [x] MVP架构（SubscribeContract、SubscribePresenter）
  - [x] 9个关注数据（follow_items.json）
  - [x] 类型筛选功能（艺术家/用户筛选）
  - [x] 时间排序功能（按关注时间降序）
- [x] **听歌时长页面完整实现**（DurationTab - 二级页面）
  - [x] 从我的页面"时长"按钮进入
  - [x] **顶部导航栏**（周/月/年三个切换标签，胶囊式设计）
  - [x] 返回按钮和"规则"按钮（带红点提示）
  - [x] **周视图**：
    - [x] 时间范围显示（带左右箭头切换）
    - [x] 红心收藏家勋章（红心形状+黑色唱片装饰）
    - [x] 勋章说明文本
    - [x] 本周总时长（超大字体："24小时29分"）
    - [x] 已听天数标签（"已听 7/7 天"）
    - [x] 7天柱状图（红色圆角柱状图，TOP标注）
    - [x] 最晚听歌时间和比上周对比
  - [x] **月视图**：
    - [x] 时间范围显示（带左右箭头切换）
    - [x] 听歌全勤奖勋章（勋章形状+✓）
    - [x] 勋章说明文本
    - [x] 九月总时长（"75小时30分"）
    - [x] 已听天数（"已听 30/30 天"）
    - [x] 日历打卡网格（7x5圆形日期，红色实心表示有听歌）
    - [x] 最晚听歌时间和比上月对比
  - [x] **年视图**：
    - [x] "年度听歌足迹"标题
    - [x] 副标题："年数据随每年听歌报告发布"
    - [x] 时间轴装饰
    - [x] 年度卡片（2024、2023）
    - [x] 听歌时长和听歌总数统计
    - [x] 年度报告卡片（带缩略图和标题）
  - [x] MVP架构（DurationContract、DurationPresenter）
  - [x] 听歌时长数据模型（WeeklyData、MonthlyData、YearlyData）
  - [x] 模拟数据（duration_data.json）
  - [x] Canvas绘制自定义图形（红心勋章、勋章徽章）
  - [x] 柱状图和日历网格组件
- [x] **创建歌单功能完整实现**（CreatePlaylistDialog - 模态弹窗）
  - [x] 从我的页面"新建歌单"按钮触发
  - [x] **底部模态弹窗设计**：
    - [x] 半透明深色背景遮罩（alpha = 0.6）
    - [x] 圆角顶部设计（topStart + topEnd = 16.dp）
    - [x] 点击背景关闭弹窗
  - [x] **顶部栏**：
    - [x] 左侧"取消"按钮
    - [x] 右侧"完成"按钮（创建歌单）
  - [x] **歌单类型选择**：
    - [x] "音乐歌单"标签（可选中）
    - [x] "视频歌单"标签（带信息图标，功能待开发提示）
  - [x] **标题输入框**：
    - [x] OutlinedTextField样式
    - [x] 占位符："输入新建歌单标题"
    - [x] 字符限制：最多20字
    - [x] 右侧显示字符计数
  - [x] **隐私设置单选**：
    - [x] RadioButton："设置为共享歌单（和好友一起管理）" + VIP红色标识
    - [x] RadioButton："设置为隐私歌单"（默认选中）
  - [x] **AI创建按钮**：
    - [x] 红色圆形按钮（CircleShape）
    - [x] 文本："AI创建歌单" + 箭头图标
    - [x] 居中底部位置
    - [x] 点击显示"AI功能待开发"提示
  - [x] **数据持久化**：
    - [x] 新歌单保存到内部存储（filesDir/data/playlists.json）
    - [x] 优先从内部存储加载歌单列表
    - [x] 首次访问时从assets复制数据
    - [x] 自动生成唯一playlistId（基于时间戳）
  - [x] **创建流程**：
    - [x] 输入验证：标题不能为空
    - [x] 创建成功后刷新歌单列表
    - [x] 显示Toast提示"创建成功"
    - [x] 自动关闭弹窗
  - [x] MVP架构（MeContract、MePresenter）
  - [x] 可复用的Dialog组件设计
- [x] **歌单详情页面完整实现**（PlaylistTab - 二级页面）
  - [x] 从我的页面点击歌单进入详情
  - [x] **顶部信息区域**：
    - [x] 半透明顶部栏（返回按钮、搜索、更多）
    - [x] 歌单封面（120dp圆角）
    - [x] 歌单标题和创建者信息
    - [x] 播放次数统计显示
  - [x] **操作按钮行**：
    - [x] 分享、评论、收藏三个圆角按钮
    - [x] 均匀分布，带图标和文字
  - [x] **播放全部区域**：
    - [x] 大型圆形橙色播放按钮（56dp）
    - [x] 歌曲数量和总时长统计
    - [x] 下载和列表快捷图标
  - [x] **继续播放横幅**（可选）：
    - [x] 半透明背景提示条
    - [x] 显示上次播放的歌曲名
    - [x] 关闭按钮
  - [x] **歌曲列表**：
    - [x] 歌曲封面缩略图（48dp圆角）
    - [x] 歌名、歌手、专辑信息
    - [x] VIP标识和音质标签（超清母带）
    - [x] 更多选项按钮
  - [x] MVP架构（PlaylistContract、PlaylistPresenter）
  - [x] 根据playlist.songIds过滤歌曲列表
  - [x] 点击歌曲导航到播放页面
  - [x] 总时长计算功能
- [x] **歌单设置页面完整实现**（PlaylistSettingTab - 三级页面）
  - [x] 从歌单详情页点击右上角三个点进入
  - [x] **顶部导航栏**：
    - [x] 返回按钮和"歌单设置"标题
    - [x] 简洁的白色背景设计
  - [x] **歌单信息头部**：
    - [x] 歌单封面（80dp圆角）
    - [x] 歌单标题和创建者信息
    - [x] 播放次数统计显示
  - [x] **功能设置列表**（9个设置项）：
    - [x] 复制DeepSeek锐评指令（带"限时"红色标签，点击复制到剪贴板）
    - [x] WiFi下自动下载歌曲（Switch开关+说明文字）
    - [x] 歌单壁纸（图标：Image）
    - [x] 添加歌曲（图标：Add）
    - [x] 歌曲红心记录（图标：Favorite）
    - [x] 更改歌曲排序（图标：Sort）
    - [x] 清空下载文件（图标：Delete）
    - [x] 添加小组件（图标：Widgets）
    - [x] 展示专辑封面（Switch开关）
  - [x] MVP架构（PlaylistSettingContract、PlaylistSettingPresenter）
  - [x] 每个设置项带Material Icons图标
  - [x] Switch组件用于开关型设置
  - [x] Toast提示操作结果
  - [x] DeepSeek锐评功能（复制预设指令到剪贴板）
- [x] **歌曲排序选择页面完整实现**（SongSortTab - 四级页面）
  - [x] 从歌单设置页点击"更改歌曲排序"进入
  - [x] **顶部导航栏**：
    - [x] 返回按钮和"歌曲排序"标题
    - [x] 简洁的白色背景设计
  - [x] **排序选项列表**（7个排序方式）：
    - [x] 手动排序（RadioButton单选）
    - [x] 按收藏时间从新到旧排序（默认选中）
    - [x] 按收藏时间从旧到新排序
    - [x] 按歌曲名排序
    - [x] 按专辑名排序
    - [x] 按歌手名排序
    - [x] 无音源歌曲置底
  - [x] MVP架构（SongSortContract、SongSortPresenter）
  - [x] 数据模型（SortOrderRecord）
  - [x] 数据持久化到 sort_order_records.json
  - [x] RadioButton单选框交互
  - [x] Toast提示选择结果并自动返回
  - [x] 按歌单ID存储不同歌单的排序偏好
- [x] **歌曲档案页面完整实现**（SongProfileTab - 三级页面）
  - [x] 从播放定制页点击"查看歌曲百科"进入
  - [x] **顶部导航栏**：返回按钮 + "歌曲百科·{歌曲名}"标题 + 更多按钮
  - [x] **个性化数据卡片**：
    - [x] 第一次听的时间和场景（"金秋的晚上 2024.10.26 20:07"）
    - [x] 累计播放次数（大号68次）
    - [x] 诗意化描述（"如同花香飘过1967条街"）
  - [x] **歌曲详情区域**：
    - [x] 基本信息：曲风（红色强调）、专辑、语种、发行时间、BPM
    - [x] 制作信息（可点击展开）
    - [x] 简介（可点击展开，显示省略号）
    - [x] 影综（显示数量"5个"，右箭头）
    - [x] 奖项（装饰奖杯emoji，显示数量"5个"，右箭头）
    - [x] 乐谱（横向滚动卡片，显示前4个乐器，数量"10个"，右箭头）
  - [x] 深色主题：背景#1A1A1A，卡片#2B2B2B，强调红色
  - [x] MVP架构：SongProfileContract、SongProfilePresenter、SongProfileTab
  - [x] 数据模型：SongProfile.kt（包含诗意描述生成逻辑）
  - [x] 从assets/songs.json加载歌曲数据
  - [x] 导航集成：PlayTab → PlayCustomizeTab → SongProfileTab
- [x] **播放器样式选择页面完整实现**（PlayerTab - 三级页面）
  - [x] 从播放定制页点击"播放器样式"进入
  - [x] **顶部导航栏**：
    - [x] 返回按钮和"播放器样式"标题
    - [x] 右侧自定义图标（相机）
    - [x] 深色背景主题（黑色）
  - [x] **横向选项卡**（ScrollableTabRow）：
    - [x] 经典、复古、创意、艺术家、联名 5个类别
    - [x] 红色指示器标识当前选中
    - [x] 点击切换不同类别
  - [x] **播放器样式卡片列表**（横向滑动）：
    - [x] 经典类别：经典黑胶、全屏封面、唱片封面
    - [x] 复古类别：复刻·千禧Pod、复刻·镭射CD（默认使用中）、复刻·琉光
    - [x] LazyRow实现横向滑动
    - [x] 每个卡片240dp宽，高380dp
    - [x] 显示播放器预览图（从assets/player加载）
    - [x] 样式名称和描述文字
    - [x] "使用中"标签（绿色，当前选中样式）
  - [x] **确认对话框**：
    - [x] 点击样式卡片弹出确认对话框
    - [x] "你确定使用该样式吗？"提示
    - [x] 取消/确定按钮
    - [x] 确定后显示"更改样式成功"Toast
  - [x] MVP架构（PlayerContract、PlayerPresenter）
  - [x] 数据模型（PlayerStyle、PlayerStyleRecord）
  - [x] 数据持久化到 playback_style_records.json
  - [x] 6张播放器样式预览图（assets/player/1-6.jpg）
  - [x] Coil图片加载库加载本地资源
  - [x] 支持按styleId记录用户选择

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

### 2025-11-03 (最新)
- ✅ **实现专辑功能（AlbumTab和AlbumListTab）**
  - 数据准备：
    - 创建Album.kt数据类（albumId, albumName, artist, artistId, coverUrl, releaseDate, description, songIds, songCount, collectCount, commentCount, shareCount）
    - 创建albums.json数据文件：为15个专辑提供完整数据（周杰伦6张、五月天3张、薛之谦2张等）
  - 修改SingerTab.kt：
    - 将Tab列表中的"等"改为"MV"
    - 根据selectedTab显示不同内容：
      * "单曲"：显示歌曲列表
      * "专辑"：显示AlbumListContent（专辑列表）
      * 其他：显示占位文本"功能开发中"
    - 添加AlbumTab条件渲染：点击专辑后全屏显示专辑详情
  - 创建专辑详情功能（AlbumTab - 三级页面）：
    - 创建AlbumContract.kt和AlbumPresenter.kt（MVP架构）
    - 实现AlbumTab.kt完整UI页面：
      * **顶部栏**：返回按钮、"专辑"标题、更多按钮
      * **专辑信息区**：封面（150dp）、专辑名、歌手信息（可点击跳转）、发行时间、描述（可展开）
      * **互动按钮行**：收藏、评论、分享（显示数量，格式化为"万"）
      * **热卖横幅**："{歌手}实体专辑热卖中 立即支持 >"（带购物车图标）
      * **播放全部区域**：金色圆形播放按钮（48dp）、歌曲数量、购物车/下载/列表图标
      * **歌曲列表**：序号、歌名、VIP/音质标签、歌手、播放/更多按钮
    - AlbumPresenter功能：
      * loadAlbumDetail()：从albums.json加载专辑信息和歌曲列表
      * onCollectClick()：显示"成功收藏《专辑名》"Toast
      * onPlayAllClick()/onSongClick()：导航到播放页面
      * onArtistClick()：导航到歌手页面
  - 创建专辑列表功能（AlbumListTab）：
    - 创建AlbumListContract.kt和AlbumListPresenter.kt（MVP架构）
    - 实现AlbumListContent组件（嵌入SingerTab使用）：
      * 专辑列表项：封面（80dp）、专辑名、歌手名、发行日期
      * 根据artistId过滤专辑列表
      * 点击专辑触发onAlbumClick回调
    - AlbumListPresenter功能：
      * loadAlbumsByArtist()：根据艺术家ID筛选专辑
      * onAlbumClick()：触发导航到专辑详情
  - 更新DataLoader.kt：
    - 添加loadAlbums()方法：加载所有专辑
    - 添加getAlbumById()方法：根据ID获取专辑
    - 添加getAlbumsByArtist()方法：根据艺术家ID获取专辑列表
    - 添加getSongsInAlbum()方法：根据专辑ID获取歌曲列表
  - 导航流程：
    - SingerTab → 点击"专辑"Tab → AlbumListContent → 点击专辑 → AlbumTab
    - AlbumTab → 点击歌手信息 → 返回SingerTab
    - AlbumTab → 点击歌曲/播放全部 → 导航到播放页面
  - 编译测试：BUILD SUCCESSFUL in 7m 26s（仅warning，无error）

### 2025-11-02
- ✅ **实现快速收藏到"我喜欢的音乐"功能**
  - 创建专用歌单my_favorites（我喜欢的音乐）到playlists.json
  - 更新DataLoader.kt：
    - 添加addSongToPlaylist()方法动态添加歌曲到歌单（带去重验证）
    - 添加isSongInPlaylist()方法检查歌曲是否已在歌单中
    - 两个方法均使用loadPlaylistsWithCache()优先从内部存储加载
  - 修改PlayPresenter.kt：
    - 重写onFavoriteClick()方法：点击爱心按钮直接添加到my_favorites歌单
    - 添加checkFavoriteStatus()检查歌曲是否已收藏
    - 已收藏显示"已在我喜欢的音乐中"，未收藏显示"成功收藏"
    - 保存收藏记录到collection_records.json（contentType: "song_to_favorites"）
    - PlayContract.View添加showSuccess(message: String)方法
    - PlayContract.Presenter添加loadSongById(songId: String)方法
  - 修改StrollPresenter.kt：
    - 实现同样的onFavoriteClick()逻辑
    - 在onNextClick()和onPreviousClick()中添加checkFavoriteStatus()调用
    - StrollContract.View添加showSuccess(message: String)方法
  - 修改PlayTab.kt：
    - 添加initialSongId参数支持播放指定歌曲
    - 实现showSuccess()方法显示Toast提示
  - 修改StrollTab.kt：
    - 实现showSuccess()方法显示Toast提示
  - Toast提示用户收藏成功或已收藏

- ✅ **实现搜索结果导航到播放页面功能**
  - 修改SearchResultContract.kt：
    - View接口添加navigateToPlay(songId: String)方法
  - 修改SearchResultPresenter.kt：
    - 实现onSongClick()方法调用view.navigateToPlay(songId)
  - 修改SearchResultTab.kt：
    - 添加onNavigateToPlay: (String) -> Unit回调参数
    - 实现navigateToPlay()回调转发
  - 修改MainActivity.kt：
    - 添加playTabSongId状态变量用于跨页面传递歌曲ID
    - SearchResultTab的onNavigateToPlay回调：设置playTabSongId、切换到播放页（currentTab = 2）、关闭搜索页
    - PlayTab传入initialSongId参数，返回时清除playTabSongId
  - 实现完整导航流程：SearchResultTab → MainActivity状态管理 → PlayTab指定歌曲播放

- ✅ **添加三个特色动漫音乐歌单**
  - 更新playlists.json添加3个新歌单：
    - playlist_009：《空之境界》精选音乐集（描述：空之境界系列精选配乐，梶浦由记经典之作）
    - playlist_010：eva系列音乐集（描述：新世纪福音战士系列经典音乐合集）
    - playlist_011：看小说推荐音乐（描述：适合阅读小说时聆听的背景音乐）
  - 每个歌单包含playlistId、playlistName、description、coverUrl、songIds数组、createTime、songCount
  - 歌单总数从7个增加到11个（含my_favorites）

- ✅ **实现推荐页面精选歌单导航功能**
  - 修改RecommendTab.kt：
    - 添加showPlaylistDetail和selectedPlaylistForDetail状态变量
    - 修改openPlaylist()方法：除"每日推荐"外的其他歌单通过PlaylistTab打开
    - 添加PlaylistTab条件渲染，支持从推荐页面导航到歌单详情
  - 实现导航流程：RecommendTab点击精选歌单 → PlaylistTab显示歌单详情
  - 支持点击《空之境界》、eva系列、看小说推荐等精选歌单打开详情页

- ✅ **实现歌单详情页收藏功能**
  - 修改PlaylistPresenter.kt：
    - 添加currentPlaylist变量保存当前歌单信息
    - 实现onCollectClick()方法显示Toast提示"成功收藏《歌单名》"
  - 点击PlaylistTab的收藏按钮显示成功提示

- ✅ **修复"我的"页面"我喜欢的音乐"重复显示问题**
  - 修改MeTab.kt的PlaylistsSection：
    - 从playlists中查找my_favorites歌单传递给FavoriteMusicItem
    - 过滤掉my_favorites，避免在普通歌单列表中重复显示
  - 修改FavoriteMusicItem：
    - 添加playlist和onPlaylistClick参数
    - 实现点击处理，支持导航到"我喜欢的音乐"歌单详情页
  - 确保"我喜欢的音乐"只显示一次，并且可以正常打开

- ✅ **实现歌手详情页面（SingerTab）**
  - 数据准备：
    - 更新artists.json添加pinyin字段（支持拼音搜索）
    - 更新Artist.kt数据类添加pinyin可选字段
  - 创建MVP架构：
    - 创建SingerContract.kt定义View和Presenter接口
    - 创建SingerPresenter.kt实现业务逻辑：
      * loadSingerData()：根据artistId加载歌手信息和歌曲列表
      * generateSongDetail()：为歌曲添加丰富展示信息（标签、热评等）
      * onFollowClick()：关注/取消关注歌手，Toast提示
      * onSongClick()：导航到播放页面
  - 创建UI页面SingerTab.kt：
    - **顶部搜索栏**：显示歌手名，返回按钮，清除按钮
    - **Tab切换栏**：综合、单曲、歌单、播客、专辑、歌手、等
    - **歌手信息区**：圆形头像、歌手名、数据统计（歌曲数·粉丝数）、乐迷团按钮
    - **AI歌单推荐区**：占位显示AI智能推荐
    - **单曲列表**：
      * 列表标题 + 播放全部按钮（金色圆形图标）
      * 每首歌曲显示：歌名+版本、权限标签（VIP/原唱）、音质标签（超清母带）
      * 歌手-专辑信息、热评/个性化文案、评论数/收藏数
      * 播放按钮和更多按钮
  - 集成导航：
    - 修改SearchResultTab.kt：
      * 添加onNavigateToSinger回调参数
      * ArtistCard添加onArtistClick参数，使其可点击
    - 修改SearchResultContract.kt：
      * View接口添加navigateToSinger(artistId)方法
      * Presenter接口添加onArtistClick(artistId)方法
    - 修改SearchResultPresenter.kt：
      * 实现onArtistClick()调用view.navigateToSinger()
    - 修改MainActivity.kt：
      * 添加showSinger和selectedArtistId状态变量
      * 添加SingerTab条件渲染（全屏显示，无底部导航栏）
      * SearchResultTab添加onNavigateToSinger回调设置状态
      * SingerTab支持导航到播放页面
  - 实现导航流程：搜索结果页点击歌手 → 歌手详情页 → 可播放歌曲
  - 支持拼音搜索歌手（如搜索"zhoujielen"找到周杰伦）
  - 编译测试成功（BUILD SUCCESSFUL in 2m 46s）

### 2025-10-30

- ✅ **修复分享功能"歌曲信息不存在"问题**
  - SharePresenter.kt：添加setSong()方法，允许直接设置currentSong
  - ShareTab.kt：在创建presenter后调用setSong(song)，传入歌曲对象
  - 修复后点击朋友圈等分享按钮正常显示"已分享到XXX"提示

- ✅ **StrollTab补全PlayTab的所有功能**
  - 添加状态变量：showComment, showShare, showLyric, showPlayCustomize, showPlayerStyle, showSongProfile, showCollectSong
  - StrollContract.View添加navigateToComment()方法
  - StrollPresenter.onCommentClick()实现，调用view.navigateToComment()
  - StrollTab添加所有功能页面的显示逻辑：
    - 评论页面（CommentTab）：点击评论按钮进入
    - 分享弹窗（ShareTab）：点击分享按钮进入
    - 歌词页面（LyricTab）：从PlayCustomizeTab进入
    - 播放定制弹窗（PlayCustomizeTab）：点击更多按钮进入
    - 播放器样式（PlayerTab）：从PlayCustomizeTab进入
    - 歌曲档案（SongProfileTab）：从PlayCustomizeTab进入
    - 收藏到歌单（CollectSongTab）：从PlayCustomizeTab进入
  - 修改顶部栏分享按钮：onShareClick = { showShare = true }
  - 漫游页面现在具有与播放页面相同的完整功能集

- ✅ **添加取消关注成功提示**
  - SubscribeContract.View添加showUnfollowSuccess(name: String)方法
  - SubscribePresenter.unfollowItem()调用view.showUnfollowSuccess()显示成功提示
  - SubscribeTab实现showUnfollowSuccess()，使用Toast显示"已成功取消关注XXX"
  - 点击取消关注后显示友好的成功提示弹窗

### 2025-10-29
- ✅ **完成歌曲档案页面**（SongProfileTab - 三级页面）
  - 创建SongProfile.kt数据模型：
    - 字段：songId, songName, artist, firstListenTime, totalPlayCount, poeticDescription, genre, album, language, releaseDate, bpm, production, introduction, filmTvList, awardsList, scoresList
    - generatePoeticDescription()：根据播放次数生成诗意描述（如"如同花香飘过1967条街"）
    - fromSong()：从Song对象创建SongProfile，填充默认数据
  - 创建SongProfileContract.kt和SongProfilePresenter.kt（MVP模式）
  - 实现SongProfileTab.kt完整UI页面（深色主题）：
    - **顶部栏**：返回按钮 + "歌曲百科·{歌曲名}"标题 + 更多按钮，深灰背景
    - **个性化数据卡片**（深色卡片，圆角12dp）：
      * 左侧：圆形头像占位符（音符emoji）
      * 中间："第一次听"标签 + 时间（如"金秋的晚上\n2024.10.26 20:07"）
      * 右侧："累计播放"次数（大号字体68次）+ 诗意描述（灰色小字）
    - **歌曲详情区域**（深色卡片，圆角12dp）：
      * 标题"歌曲详情"（18sp粗体）
      * 基本信息：曲风（红色强调"流行-华语流行"）、专辑、语种、发行时间、BPM
      * 制作信息（可点击展开）：作词/作曲/编曲，右箭头
      * 简介（可点击展开）：显示前两行+省略号，右箭头
      * 影综：预览第一项+数量（"5个"）+ 右箭头
      * 奖项：装饰奖杯emoji + 前两项预览 + 数量（"5个"）+ 右箭头
      * 乐谱：横向滚动卡片（80x80dp）显示前4个乐器（钢琴、吉他等）+ 数量（"10个"）+ 右箭头
  - SongProfilePresenter功能实现：
    - loadSongProfile()：从assets/songs.json加载歌曲数据
    - 使用SongProfile.fromSong()生成档案数据（硬编码默认值）
    - 预留点击事件：onProductionClick、onIntroductionClick、onFilmTvClick、onAwardsClick、onScoresClick
  - 修改PlayCustomizeContract.kt：
    - View接口添加navigateToSongProfile()方法
  - 修改PlayCustomizePresenter.kt：
    - onSongEncyclopediaClick()从显示Toast改为调用view.navigateToSongProfile()
  - 修改PlayCustomizeTab.kt：
    - 添加onNavigateToSongProfile回调参数
    - Presenter实现navigateToSongProfile接口方法
  - 修改PlayTab.kt：
    - 添加showSongProfile状态管理
    - PlayCustomizeTab传入导航回调，点击后关闭浮层并打开SongProfileTab
    - SongProfileTab作为全屏页面渲染（与PlayerTab同级）
  - 导航流程：PlayTab → PlayCustomizeTab（浮层）→ "查看歌曲百科" → SongProfileTab（全屏）
  - 深色主题配色：背景#1A1A1A，卡片#2B2B2B，文字白色/灰色/浅灰，强调红色
  - 构建测试通过（BUILD SUCCESSFUL in 8m 38s）
- ✅ **完成播放器样式选择页面**（PlayerTab - 三级页面）
  - 创建PlayerStyle.kt数据模型：
    - 字段：styleId, styleName, category, imageUrl, description, isInUse
    - 5个样式类别：经典、复古、创意、艺术家、联名
    - 预设6种播放器样式（经典黑胶、全屏封面、唱片封面、复刻·千禧Pod、复刻·镭射CD、复刻·琉光）
    - getAllStyles()静态方法获取所有样式
    - getStylesByCategory()按类别筛选
  - 创建PlayerStyleRecord.kt记录数据类：
    - 字段：recordId, styleId, timestamp
    - 用于保存用户选择到playback_style_records.json
  - 创建PlayerContract.kt和PlayerPresenter.kt（MVP模式）
  - 实现PlayerTab.kt完整UI页面（深色主题）：
    - **顶部栏**：返回按钮 + "播放器样式"标题 + 右侧相机图标，黑色背景
    - **横向选项卡**（ScrollableTabRow）：
      * 5个类别标签：经典、复古、创意、艺术家、联名
      * 红色指示器标识当前选中类别
      * 点击切换类别，动态加载对应样式
    - **样式卡片列表**（LazyRow横向滑动）：
      * 每个卡片240dp宽，高380dp
      * 使用Coil加载assets/player下的预览图
      * 圆角12dp设计
      * 样式名称（16sp粗体）和描述文字（13sp灰色）
      * "使用中"标签（绿色，显示在右上角）
      * 点击卡片触发确认对话框
    - **确认对话框**（AlertDialog）：
      * 标题"你确定使用该样式吗？"
      * 显示选中的样式名称
      * 取消/确定两个按钮
      * 确定后保存并显示"更改样式成功"Toast
  - PlayerPresenter功能实现：
    - loadPlayerStyles()：加载所有样式并标记当前使用的样式
    - onStyleSelected()：点击样式时显示确认对话框
    - confirmStyleChange()：保存用户选择到playback_style_records.json
    - onTabSelected()：切换类别时筛选对应样式
    - 使用Gson进行JSON序列化/反序列化
    - 文件存储在app私有目录：context.filesDir
    - 默认样式：retro_cd（复刻·镭射CD）
  - 修改PlayCustomizeContract.kt：
    - View接口添加navigateToPlayerStyle()方法
  - 修改PlayCustomizePresenter.kt：
    - onPlayerStyleClick()从显示Toast改为调用view.navigateToPlayerStyle()
  - 修改PlayCustomizeTab.kt：
    - 添加onNavigateToPlayerStyle回调参数
    - Presenter实现navigateToPlayerStyle接口方法
  - 修改PlayTab.kt：
    - 添加showPlayerStyle状态管理
    - PlayCustomizeTab传入导航回调，点击后关闭浮层并打开PlayerTab
    - PlayerTab作为全屏页面渲染（与ShareTab同级）
  - 导航流程：PlayTab → PlayCustomizeTab（浮层）→ PlayerTab（全屏）
  - 使用assets/player下的6张图片（1.jpg-6.jpg）作为样式预览
  - 构建测试通过（BUILD SUCCESSFUL in 6m 23s）

### 2025-10-28
- ✅ **完成歌曲排序选择页面**（SongSortTab - 四级页面）
  - 创建SortOrderRecord.kt数据模型：
    - 字段：recordId, playlistId, sortType, timestamp
    - 7个排序类型常量：手动排序、按收藏时间从新到旧/从旧到新、按歌曲名/专辑名/歌手名排序、无音源歌曲置底
  - 创建SongSortContract.kt和SongSortPresenter.kt（MVP模式）
  - 实现SongSortTab.kt完整UI页面：
    - **顶部栏**：返回按钮 + "歌曲排序"标题，简洁白色背景
    - **排序选项标题**："选择歌曲排序"（18sp粗体）
    - **7个排序选项列表**：
      * 每项使用RadioButton单选框 + 文本描述
      * 默认选中"按收藏时间从新到旧排序"
      * 点击任一选项立即保存并显示Toast提示
      * 自动返回到歌单设置页
  - SongSortPresenter功能实现：
    - loadCurrentSortOrder()：从文件读取当前歌单的排序设置
    - onSortOptionSelected()：保存用户选择到sort_order_records.json
    - 支持多歌单独立排序偏好（按playlistId区分）
    - 使用Gson进行JSON序列化/反序列化
    - 文件存储在app私有目录：context.filesDir
  - 修改PlaylistSettingContract.kt：
    - View接口添加navigateToSongSort(playlist)方法
  - 修改PlaylistSettingPresenter.kt：
    - onChangeSortOrderClick()从显示"功能开发中"改为调用view.navigateToSongSort()
  - 修改PlaylistSettingTab.kt：
    - 添加onNavigateToSongSort回调参数
    - Presenter实现navigateToSongSort接口方法
  - 修改MainActivity.kt：
    - 添加showSongSort和selectedSortPlaylist状态管理
    - 添加SongSortTab页面条件渲染（全屏显示，无底部导航栏）
    - SongSortTab在PlaylistSettingTab之前检查，实现四级导航
    - PlaylistSettingTab传入onNavigateToSongSort回调
  - 导航流程：MainActivity → MeTab → PlaylistTab → PlaylistSettingTab → SongSortTab
  - 数据持久化到sort_order_records.json（初始为空数组[]）
  - 构建测试通过（BUILD SUCCESSFUL in 2m 38s）

### 2025-10-29
- ✅ **完成歌曲删除操作页面**（SongDelTab - 四级页面）
  - 创建SongDeletionRecord.kt数据模型：
    - 字段：recordId, playlistId, songId, timestamp
  - 创建SongDelContract.kt和SongDelPresenter.kt（MVP模式）
  - 实现SongDelTab.kt完整UI页面（**浅色主题**）：
    - **顶部栏**：返回按钮，白色背景
    - **歌曲信息卡片**（浅灰背景#F5F5F5）：
      * 封面图片（80dp圆角12dp）
      * 歌曲名称 + VIP金色徽章
      * 歌手名称
      * 权限说明文字："下载后仅限VIP有效期内在云音乐本地播放"
    - **操作按钮区域**：
      * 评论（显示数量364197）
      * 分享
      * 单曲购买（带标签"永久拥有该歌曲"）
    - **详细信息区域**：
      * 歌手信息
      * 创作者信息（硬编码：歌手/郑伟/张宝宇/赵英俊）
      * 专辑信息
      * 更多乐谱（硬编码：吉他/钢琴/人声/小提琴/打击乐）
    - **扩展功能区域**：
      * 设置铃声或彩铃
      * 音乐礼品卡（带标签"送好友"）
      * 删除按钮（**红色高亮**）
    - **删除确认对话框**：
      * 显示歌曲名称和歌手
      * 取消/确定按钮
  - SongDelPresenter功能实现：
    - loadSongById()：从assets/songs.json读取歌曲信息
    - confirmDelete()：保存删除记录到song_deletion_records.json
    - 支持多歌单歌曲删除记录（按playlistId + songId区分）
    - 使用Gson进行JSON序列化/反序列化
    - 文件存储在app私有目录：context.filesDir
  - 修改PlaylistTab.kt：
    - 添加onNavigateToSongDel(songId, playlistId)回调参数
    - SongListItem添加onMoreClick参数
    - 连接MoreVert三个点按钮到onNavigateToSongDel回调
  - 修改MainActivity.kt：
    - 添加showSongDel、selectedDelSongId、selectedDelPlaylistId状态管理
    - 添加SongDelTab页面条件渲染（全屏显示，无底部导航栏）
    - SongDelTab在SongSortTab之后检查，实现四级导航
    - PlaylistTab传入onNavigateToSongDel回调
  - 导航流程：MainActivity → MeTab → PlaylistTab → 歌曲列表三个点菜单 → SongDelTab
  - 数据持久化到song_deletion_records.json（初始为空数组[]）
  - 构建测试通过（BUILD SUCCESSFUL in 6m 19s）

- ✅ **完成歌单设置页面**（PlaylistSettingTab - 三级页面）
  - 创建PlaylistSettingContract.kt和PlaylistSettingPresenter.kt（MVP模式）
  - 实现PlaylistSettingTab.kt完整UI页面：
    - **顶部栏**：返回按钮 + "歌单设置"标题，简洁白色背景
    - **歌单信息头部**：
      * 歌单封面（80dp圆角8dp）
      * 歌单标题和创建者信息
      * 播放次数统计
    - **功能设置列表**（9个设置项）：
      * 复制DeepSeek锐评指令：带"限时"红色标签，点击复制"分析我最近100首红心歌曲,点评我的听歌品味"到剪贴板
      * WiFi下自动下载歌曲：Switch开关 + 说明文字"下载会员歌曲将占用当月付费下载额度"
      * 歌单壁纸：点击显示"功能开发中"提示
      * 添加歌曲、歌曲红心记录、更改歌曲排序：点击显示"功能开发中"提示
      * 清空下载文件、添加小组件：点击显示"功能开发中"提示
      * 展示专辑封面：Switch开关
  - 修改PlaylistTab.kt：
    - 给顶部栏MoreVert按钮添加点击事件
    - 添加onNavigateToSetting回调参数
    - 修改PlaylistTopBar接收onMoreClick参数
  - 修改MainActivity.kt：
    - 添加showPlaylistSetting和selectedSettingPlaylist状态管理
    - 添加PlaylistSettingTab页面条件渲染（全屏显示，无底部导航栏）
    - PlaylistTab传入onNavigateToSetting回调
  - PlaylistSettingPresenter功能实现：
    - loadPlaylistSettings()：加载歌单信息
    - onCopyDeepSeekClick()：使用ClipboardManager复制预设指令到剪贴板
    - onWifiAutoDownloadChanged()：处理WiFi自动下载开关变化，显示Toast
    - onShowAlbumCoverChanged()：处理展示专辑封面开关变化，显示Toast
    - 其他设置项点击显示"功能开发中"Toast
  - UI组件实现：
    - SettingItem：普通可点击设置项（图标+标题+副标题+可选标签）
    - SettingItemWithSwitch：带开关的设置项（图标+标题+副标题+Switch）
    - 统一使用Material Icons图标（ContentCopy、CloudDownload、Image、Add、Favorite、Sort、Delete、Widgets、Album）
    - Divider分隔线（歌单信息和设置列表之间）
  - 更新README.md记录PlaylistSettingTab功能
  - 成功构建并验证功能（BUILD SUCCESSFUL in 6m 19s）

### 2025-10-27
- ✅ **完成歌单详情页面**（PlaylistTab - 二级页面）
  - 创建PlaylistContract.kt和PlaylistPresenter.kt（MVP模式）
  - 实现PlaylistTab.kt完整UI页面：
    - **顶部栏**：半透明黑色背景（alpha = 0.3）、返回按钮、搜索、更多按钮
    - **歌单信息头部**：
      * 歌单封面（120dp，圆角8dp，使用Coil加载assets图片）
      * 歌单标题（20sp粗体）
      * 创建者信息：圆形头像（24dp）+ 昵称"箱子里的稻草人"
      * 播放次数："1540次播放"（硬编码）
    - **操作按钮行**：
      * 分享、评论、收藏三个圆角按钮（均匀分布）
      * SurfaceVariant背景色，圆角20dp
      * 每个按钮带图标和文字
    - **播放全部区域**：
      * 大型圆形播放按钮（56dp，金色背景Color(0xFFFFB74D)）
      * 左侧显示"播放全部"标题和"X首歌曲 · 总时长"
      * 右侧下载和列表图标
    - **继续播放横幅**（可选显示）：
      * 半透明背景（SurfaceVariant.copy(alpha = 0.3)）
      * 显示"继续播放：歌曲名"
      * 右侧关闭按钮
    - **歌曲列表**：
      * 每行显示：封面（48dp圆角）+ 歌名/歌手/专辑 + 更多按钮
      * VIP标识和音质标签（基于歌曲ID哈希动态显示）
      * VIP标签：金色边框，"VIP"文字
      * 音质标签：金色边框，"超清母带"文字
  - 修改MePresenter.kt：
    - MeContract.View添加navigateToPlaylist(playlist: Playlist)方法
    - 实现onPlaylistClick()调用view.navigateToPlaylist()
  - 修改MeTab.kt：
    - 添加onNavigateToPlaylist: (Playlist) -> Unit回调参数
    - 实现navigateToPlaylist回调转发到onNavigateToPlaylist
  - 修改MainActivity.kt：
    - 添加showPlaylist和selectedPlaylist状态管理
    - 添加PlaylistTab条件渲染（全屏显示，无底部导航栏）
    - 实现返回和导航到播放页面功能
    - MeTab添加onNavigateToPlaylist回调
  - PlaylistPresenter功能实现：
    - loadPlaylistSongs()：根据playlist.songIds过滤歌曲列表
    - onSongClick()：导航到播放页面
    - onPlayAllClick()：播放第一首歌曲
    - onShareClick/onCommentClick/onCollectClick：显示"功能待开发"提示
  - calculateTotalDuration()工具函数：
    - 计算歌曲总时长（秒转换为小时/分钟）
    - 格式化显示："大于X小时" / "X分钟" / "少于1分钟"
  - 更新README.md记录PlaylistTab功能
  - 成功构建并验证功能（BUILD SUCCESSFUL in 6m 35s）
- ✅ **完成创建歌单功能**（CreatePlaylistDialog - 模态弹窗）
  - 创建CreatePlaylistDialog.kt可复用组件：
    - 使用Dialog + Box实现底部模态弹窗
    - 半透明深色背景遮罩（Color.Black.copy(alpha = 0.6f)）
    - 圆角顶部设计（RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)）
    - 点击背景或"取消"按钮关闭弹窗
  - **UI组件实现**：
    - 顶部栏：左侧"取消"TextButton、右侧"完成"TextButton
    - 类型选择标签："音乐歌单"/"视频歌单"（带Info图标）
    - 标题输入框：OutlinedTextField，占位符"输入新建歌单标题"
    - 字符限制：TextFieldValue监听，maxLength = 20
    - 右侧字符计数显示："20"
    - 隐私设置RadioButton组：
      * "设置为共享歌单（和好友一起管理）" + VIP红色标识
      * "设置为隐私歌单"（默认选中）
    - AI创建按钮：红色CircleShape Button，"AI创建歌单" + ArrowForward图标
  - 更新DataLoader.kt：
    - 添加savePlaylists()方法保存歌单到内部存储
    - 添加loadPlaylistsWithCache()方法优先从内部存储加载
    - 添加generatePlaylistId()生成唯一ID（基于时间戳）
    - 内部存储路径：filesDir/data/playlists.json
  - 修改MeContract接口：
    - Presenter添加createPlaylist(title, isPrivate, isMusic)方法
  - 修改MePresenter.kt：
    - 实现createPlaylist()方法：
      * 生成新Playlist对象（默认空songIds，songCount = 0）
      * 添加到歌单列表
      * 调用DataLoader.savePlaylists()持久化
      * 刷新UI：view.showPlaylists()
    - 修改loadData()使用loadPlaylistsWithCache()
  - 修改MeTab.kt：
    - 添加showCreatePlaylistDialog状态
    - 修改CreateAndImportSection点击事件显示对话框
    - 在MeTab末尾添加CreatePlaylistDialog条件渲染
    - 创建成功后显示Toast："创建成功"
  - 功能特点：
    - 输入验证：标题不能为空
    - 视频歌单和AI功能显示Toast："功能待开发"
    - 创建成功后自动刷新列表并关闭弹窗
    - 数据持久化到内部存储，支持多次创建
  - 更新README.md记录CreatePlaylistDialog功能
  - 成功构建并验证功能
- ✅ **完成听歌时长页面**（DurationTab - 二级页面）
  - 创建ListeningDuration.kt、WeeklyData、MonthlyData、YearlyData数据类
  - 创建duration_data.json模拟数据：
    - 周数据：7天听歌时长、勋章信息、TOP日期、对比数据
    - 月数据：30天打卡情况、勋章信息、对比数据
    - 年数据：多年听歌统计、年度报告信息
  - 创建DurationContract.kt和DurationPresenter.kt（MVP模式）
  - 实现DurationTab.kt完整UI页面：
    - **顶部导航栏**：返回按钮、周/月/年切换标签（胶囊式设计）、规则按钮（带红点）
    - **周视图**：
      * 时间范围显示（"10.19 - 10.25"，带左右箭头）
      * 红心收藏家勋章（Canvas绘制红心+黑色唱片）
      * 勋章说明："本周82%听歌时长来自红心歌单"
      * 本周总时长："24小时29分"（超大字体48sp）
      * 已听天数标签："已听 7/7 天"
      * 7天柱状图（红色圆角柱状图，Canvas绘制）
      * TOP标注："TOP 2025.10.23 听歌 6小时50分"
      * 底部统计：最晚听歌"02:30"、比上周"少听6分"
    - **月视图**：
      * 时间范围显示（"9.1 - 9.30"，带左右箭头）
      * 听歌全勤奖勋章（Canvas绘制勋章形状）
      * 勋章说明："本月听歌天数达30天!"
      * 九月总时长："75小时30分"
      * 已听天数："已听 30/30 天"
      * 日历打卡网格（7x5圆形日期，LazyVerticalGrid）
      * 有听歌日期显示为红色实心圆，无听歌显示为浅色圆
      * 底部统计：最晚听歌"04:55"、比上月"多听26小时12分"
    - **年视图**：
      * 标题："年度听歌足迹"
      * 副标题："年数据随每年听歌报告发布"
      * 时间轴装饰（Canvas绘制虚线）
      * 年度卡片列表（2024、2023降序排列）
      * 每个卡片显示：年份（56sp大字）、听歌时长、听歌总数
      * 2024年度报告缩略图卡片："2024年度听歌报告 - 人生是旷野，音乐也是"
  - 更新DataLoader.kt：
    - 添加loadDurationData()方法
    - 导入ListeningDuration数据类
  - 修改MeTab.kt：
    - 添加onNavigateToDuration回调参数
    - 时长文本（"1693 小时"）添加clickable修饰符
  - 修改MainActivity.kt：
    - 添加showDuration状态管理
    - 添加DurationTab页面条件渲染
    - MeTab添加onNavigateToDuration回调
  - 技术亮点：
    - Canvas自定义绘制红心勋章和勋章徽章
    - Canvas绘制柱状图和时间轴装饰
    - LazyVerticalGrid实现日历打卡网格
    - 胶囊式标签选择器设计
  - 更新README.md记录DurationTab功能实现
  - 成功构建并验证功能
- ✅ **完成取消关注功能**（UnfollowDialog）
  - 修改SubscribeContract.kt：添加unfollowItem方法到Presenter接口
  - 实现SubscribePresenter.kt的unfollowItem方法：
    - 从allFollowItems列表中移除指定关注项
    - 根据当前筛选条件更新显示列表
  - 修改SubscribeTab.kt：
    - 添加showUnfollowDialog和selectedFollowItem状态管理
    - FollowItemCard的更多按钮点击时显示取消关注对话框
    - 创建UnfollowDialog组件（Material3 AlertDialog）
    - 对话框显示提示文本："取消关注后对方将从你的关注列表中移除"
    - 确认按钮带PersonRemove图标和"取消关注"文字
    - 点击确认后调用presenter.unfollowItem()并关闭对话框
  - 添加TextAlign导入用于文本居中显示
  - 取消关注后立即从列表中移除，保持当前筛选状态
  - 更新README.md记录UnfollowDialog功能实现
  - 成功构建并验证功能

### 2025-10-26
- ✅ **完成关注页面**（SubscribeTab - 二级页面）
  - 创建FollowItem.kt数据类：
    - 支持用户和艺术家两种类型（type字段）
    - 包含VIP类型字段（svip、black_svip、vip）
    - 包含活动类型字段（mv、post）
    - 包含关注时间字段用于排序
  - 创建follow_items.json：
    - 9个关注项数据（包含艺术家和用户）
    - 示例数据：h3R3、宇多田ヒカル、燕云十六声、Taylor Swift、周杰伦、五月天等
  - 创建SubscribeContract.kt和SubscribePresenter.kt（MVP模式）
  - 实现SubscribeTab.kt完整UI页面：
    - **顶部导航栏**：三栏标签（推荐/关注/粉丝）带下划线指示器、返回按钮、搜索按钮
    - **筛选栏**："按关注时间排序"下拉菜单 + 类型筛选（全部/歌手/用户）
    - **聚合更新横幅**：显示多位艺人的新动态（如"宇多田ヒカル等3位艺人有新的动..."）
    - **关注列表项**：
      - 圆形头像（48dp）+ VIP角标（底部右侧小圆形V标识）
      - 用户名/艺术家名 + VIP徽章（不同颜色：SVIP金色、VIP蓝色、黑胶SVIP黑色）
      - 副标题显示（昵称或额外信息）
      - 活动信息显示（如"最近，发布了MV"）
      - 更多选项按钮（右侧竖三点图标）
    - **VIP徽章组件**：带星星图标和文字的彩色标签
    - **底部推荐区域**：推荐歌曲卡片（Mini播放器样式）
  - 更新DataLoader.kt：
    - 添加loadFollowItems()方法加载关注列表数据
    - 导入FollowItem数据类
  - 修改MeTab.kt：
    - 添加onNavigateToSubscribe回调参数
    - "关注"文本可点击，触发导航到SubscribeTab
  - 修改MainActivity.kt：
    - 添加showSubscribe状态管理
    - 添加SubscribeTab页面条件渲染
    - MeTab添加onNavigateToSubscribe回调参数
  - 功能实现：
    - 类型筛选功能（全部/艺术家/用户）
    - 时间排序功能（按关注时间降序）
    - 聚合更新检测（3位及以上艺术家有更新时显示横幅）
  - 成功构建并验证功能
- ✅ **完成播放定制页面**（PlayCustomizeTab - Overlay浮层）
  - 创建PlayCustomizeContract.kt和PlayCustomizePresenter.kt（MVP模式）
  - 实现PlayCustomizeTab.kt完整UI页面：
    - **顶部歌曲信息区域**：封面缩略图（48dp圆角）+ 歌曲名 + 歌手名 + VIP标识
    - **第一层核心功能**：4个圆形按钮横排（收藏/下载/分享/一起听）
    - **第二层详细信息**：专辑信息行 + 歌手信息行（带"+关注"按钮）
    - **第三层扩展功能**：查看歌曲百科、开始相似歌曲漫游、单曲购买
    - **第四层播放设置**：音质设置（极高 VIP + 红点图标）、音效、播放器样式
  - 修改PlayTab.kt：
    - 添加showPlayCustomize状态管理
    - 底部功能栏"更多"按钮点击显示PlayCustomizeTab
    - PlayCustomizeTab作为Overlay层叠加在Box中
  - 更新DataLoader.kt：
    - 添加saveCollectionRecord、loadCollectionRecords、generateCollectionId方法
    - 添加saveDownloadRecord、loadDownloadRecords、generateDownloadId方法
    - 添加saveArtistFollowRecord、loadArtistFollowRecords方法
  - 功能实现：
    - 收藏功能：保存CollectionRecord到collection_records.json，切换爱心图标状态
    - 下载功能：保存DownloadRecord到download_records.json，显示"极高VIP"标签
    - 分享功能：关闭PlayCustomizeTab，打开ShareTab
    - 关注功能：保存ArtistFollowRecord到artist_follow_records.json，切换按钮状态
  - Toast提示操作成功
  - 半透明黑色遮罩背景（60%透明度）
  - 点击背景关闭浮层
  - 使用Material Icons图标
  - 修复Kotlin编译错误：将匿名对象内的this@PlayCustomizeTab引用改为直接变量引用
  - 成功构建并验证功能（BUILD SUCCESSFUL in 28s）
- ✅ **修复歌词页面状态栏重叠问题**
  - 问题：LyricTab顶部内容与系统状态栏重叠
  - 原因：MainActivity使用了`enableEdgeToEdge()`导致内容延伸到状态栏下方
  - 解决方案：为LyricTab的主Column添加`.statusBarsPadding()`修饰符
  - 成功构建并验证修复（BUILD SUCCESSFUL in 5m 26s）
- ✅ **完成歌词页面**（LyricTab - 二级页面）
  - 创建LyricContract.kt和LyricPresenter.kt（MVP模式）
  - 实现LyricTab.kt完整UI页面：
    - **顶部信息区**：标题栏（返回、VIP标识、歌曲名、刷新）、歌手信息（歌手名+关注按钮）、歌曲百科入口、制作信息（作词/作曲/编曲/制作人，带小头像图标）
    - **中部歌词展示区**：LazyColumn滚动列表、当前歌词高亮（18sp加粗）、上下文歌词渐变透明度、时间戳显示、播放图标、自动滚动到当前歌词
    - **底部播放控制区**：互动按钮（更多/红心/麦克风）、进度条（时间/音质/总时长）、播放控制（模式/上一曲/播放暂停/下一曲/列表）、次要功能（评论/音效/详情/更多）
  - 修改PlayTab.kt：给封面图片添加clickable修饰符，点击跳转到歌词页面
  - 修改MainActivity.kt：
    - 添加showLyric状态管理
    - 添加LyricTab页面条件渲染
    - PlayTab添加onNavigateToLyric回调参数
  - 歌词解析功能：从Song.lyrics按换行符分割为行列表
  - 播放状态同步、收藏功能
  - 成功构建并验证功能（BUILD SUCCESSFUL in 6m 35s）
- ✅ **优化分享页面架构**（从独立页面改为Overlay层叠架构）
  - 修改ShareTab：接收Song对象而非songId，避免重复数据加载
  - **图标优化**：将所有emoji图标替换为Material Icons
    - 网易云笔记：Icons.Default.EditNote
    - 朋友圈：Icons.Default.CameraAlt
    - 微信好友：Icons.Default.Chat
    - QQ空间：Icons.Default.Star
    - QQ好友：Icons.Default.Group
    - 邀请好友：Icons.Default.GroupAdd
    - 送会员：Icons.Default.CardGiftcard
  - **架构重构**：从独立导航页面改为Overlay层叠架构
    - 移除MainActivity中的ShareTab导航逻辑（showShare状态、条件渲染、回调参数）
    - 移除PlayPresenter中的navigateToShare方法和onShareClick接口
    - PlayTab内部管理showShare状态，ShareTab作为Box的overlay层显示
    - 半透明深色蒙版（Color.Black.copy(alpha = 0.6f)）覆盖在PlayTab上方
    - 点击背景调用presenter.onCloseClick()关闭分享页面
  - 成功构建并验证功能（BUILD SUCCESSFUL in 5m 41s）
- ✅ 实现分享页面功能（初版）
  - 创建ShareContract和SharePresenter（MVP模式）
  - 实现ShareTab分享页面UI：
    - 半透明深色蒙版背景（60%透明度）
    - "分享至"区域：5个彩色圆形图标（网易云笔记、朋友圈、微信好友、QQ空间、QQ好友）
    - "你还可以分享"区域：3个卡片（邀请好友一起听、送好友会员天数（带限时标签）、微信状态）
    - 点击背景关闭分享页面
  - 修改PlayTab：
    - 将右上角刷新按钮改为分享按钮（Icons.Default.Share）
    - 添加onNavigateToShare回调参数
    - 修改PlayTopBar接收onShareClick参数
  - 修改PlayPresenter：
    - PlayContract.Presenter添加onShareClick()方法
    - PlayContract.View添加navigateToShare()方法
    - 实现onShareClick()触发navigateToShare()
  - 修改MainActivity：
    - 添加showShare状态管理
    - 集成ShareTab导航逻辑
    - PlayTab添加onNavigateToShare回调
  - 更新DataLoader：
    - 添加saveShareRecord方法保存分享记录
    - 添加loadShareRecords方法加载分享记录
    - 添加generateShareId方法生成唯一分享ID
    - 分享记录保存到share_records.json
  - Toast提示分享成功
  - 成功构建并验证功能
- ✅ 实现评论发送功能
  - 更新CommentContract接口：添加showSuccess和clearCommentInput方法
  - 实现CommentPresenter的onSendComment方法：
    - 验证评论内容不为空
    - 生成新的评论ID和CommentRecord ID
    - 创建Comment对象（用户名显示为"我"，头像使用avatar/1.png）
    - 调用DataLoader保存评论到内部存储（filesDir/data/comments.json）
    - 创建并保存CommentRecord记录
    - 更新本地评论列表并重新应用排序
    - 显示成功Toast提示
  - 更新CommentTab的View层：
    - 添加Toast导入
    - 实现showError和showSuccess回调显示Toast
    - 将commentText状态提升到CommentTab层
    - 实现clearCommentInput回调
    - 更新CommentBottomBar接收commentText作为参数
  - 评论发送后立即在列表中显示
  - 评论数据持久化到本地JSON文件
  - 成功构建并验证功能

### 2025-10-25
- ✅ 修复评论入口位置
  - 修改PlaySongInfoSection函数：添加onCommentClick参数
  - 更新PlaySongInfoSection调用：传入onCommentClick = { presenter.onCommentClick() }参数
  - 修改评论按钮点击事件：从底部功能栏移动到歌曲信息区域右侧的评论图标
  - 现在点击歌曲信息右侧的评论图标（显示"316"评论数的按钮）可进入评论页面
  - 成功构建并验证功能

### 2025-10-25
- ✅ 完成评论页面（CommentTab）作为二级页面
  - 创建Comment数据类（评论ID、用户信息、内容、时间戳、点赞数、回复数等）
  - 创建CommentContract和CommentPresenter（MVP模式）
  - 实现顶部标签切换栏（评论/笔记，显示数量统计）
  - 实现歌曲信息显示区域（封面、歌名-艺术家、评论总数）
  - 实现排序筛选栏（推荐/最热/最新三种排序方式）
  - 创建评论列表组件（用户头像、昵称、VIP标识、内容、时间、点赞数、回复数）
  - 实现评论点赞功能（点击爱心图标切换状态，动态更新点赞数）
  - 实现长评论折叠显示（"——展开"/"——收起"按钮）
  - 实现底部话题标签区域（话题图标+标签列表）
  - 创建评论输入框（带"随乐而起,有爱评论"提示文案和发送按钮）
  - 添加10条模拟评论数据（comments.json）
  - 实现评论格式化显示方法（点赞数、回复数、时间显示）
  - 修改PlayPresenter：实现导航到评论页面功能
  - 修改PlayTab：添加onNavigateToComment回调参数
  - 修改MainActivity：集成CommentTab页面导航
  - 更新DataLoader：添加loadComments和getCommentsBySongId方法
  - 成功构建并验证功能

### 2025-10-23
- ✅ 实现漫游模式动态切换功能
  - 修改ModeSelectionContract：添加onModeSelectedCallback回调接口
  - 修改ModeSelectionPresenter：选择模式后回传模式名称并自动返回
  - 修改StrollTab：接收currentMode参数，动态显示"私人漫游·$模式名"
  - 修改ModeSelectionTab：接收currentMode和onModeSelected回调
  - 修改MainActivity：添加selectedStrollMode状态管理，实现模式选择同步
  - 用户体验：点击漫游页标题→选择模式→自动返回并更新标题
  - 支持4种主要模式（默认、熟悉、探索、拼图）和21种场景模式
  - 成功构建并验证功能
- ✅ 修改歌手头像路径使用avatar目录图片
  - 更新artists.json：将所有7位歌手的avatarUrl改为使用avatar/1.png到avatar/7.png
  - 搜索结果页面的歌手信息卡片现在显示avatar目录下的真实头像
  - 成功构建并验证功能
- ✅ 实现拼音搜索功能（解决离线环境中文输入问题）
  - 修改Song.kt数据类：添加pinyin和artistPinyin字段
  - 更新songs.json：为所有15首歌曲添加拼音数据
  - 修改SearchPresenter.kt：增强搜索逻辑支持拼音匹配
  - 支持使用英文拼音搜索中文歌曲（例如：输入"yanyuan"可搜索"演员"）
  - 无需下载中文输入法语言包，完全离线工作
  - 搜索历史记录也支持拼音匹配
  - 成功构建并验证功能

### 2025-10-22
- ✅ 完成听歌识曲页面（ListenRecognizeTab）作为二级页面
  - 创建ListenRecognizeContract和ListenRecognizePresenter（MVP模式）
  - 实现顶部栏（返回按钮、"听歌识曲"标题、历史按钮、更多按钮）
  - 创建功能提示条（"开启桌面悬浮窗，边刷视频边识曲" + Switch开关）
  - 实现中心录音区域（大型红色圆形按钮，带粉色光晕效果）
  - 实现录音按钮（200dp红色圆形 + 300dp粉色光晕外层）
  - 添加白色麦克风图标（居中显示，80dp尺寸）
  - 实现录音状态显示（"录音中(Xs)" / "点击开始识曲" / "点击停止识曲"）
  - 实现底部模式切换（听歌识曲 / 哼唱识曲，圆角按钮切换）
  - 实现自动录音功能（进入页面自动开始录音）
  - 实现录音计时器（LaunchedEffect + 协程，每秒更新时长显示）
  - 实现点击切换录音状态（开始/停止录音功能）
  - 实现模式切换时重新开始录音
  - 修改RecommendTab：添加听歌识曲导航（从麦克风按钮进入）
  - 使用状态管理实现页面导航（showListenRecognizePage）
  - 成功构建并验证功能（仅deprecation警告，无错误）
- ✅ 完成模式选择页面（ModeSelectionTab）作为二级页面
  - 创建ModeSelectionContract和ModeSelectionPresenter（MVP模式）
  - 实现顶部栏（返回按钮、"私人漫游·默认模式"标题、设置按钮）
  - 实现深红色渐变背景（仿网易云音乐私人漫游风格）
  - 创建四大核心模式卡片（默认模式、熟悉模式、探索模式、拼图模式）
  - 模式卡片半透明设计（白色背景15%透明度，100dp高度，圆角16dp）
  - 实现场景模式标题区域
  - 创建场景模式网格（4列网格布局，emoji图标）
  - 添加21个场景模式（伤感、运动、助眠、放松、欢快、抒情、治愈、专注、浪漫情歌、R&B、下雨天、打游戏、说唱、K-Pop、宝藏原创、电音、出行、洗澡、咖啡馆、摇滚、励志）
  - 部分场景模式显示"NEW"标签（R&B、下雨天）
  - 修改StrollTab：添加点击顶部标题跳转到模式选择页面
  - 实现从漫游页面导航到模式选择的功能
  - 成功构建并验证功能
- ✅ 完成搜索结果页面（SearchResultTab）作为三级页面
  - 创建MusicVideo和SongDetail数据类（支持MV和丰富的歌曲展示信息）
  - 创建SearchResultContract和SearchResultPresenter（MVP模式）
  - 实现顶部搜索栏（显示当前搜索词、返回按钮、清除按钮）
  - 实现分类标签导航栏（综合、单曲、歌单、播客、专辑、歌手、笔记）
  - 创建歌手信息卡片（头像、V认证标识、歌手名、作品数、粉丝数、关注按钮）
  - 实现MV区域（大图封面、播放按钮覆盖层、时长、播放量）
  - 实现单曲列表（支持版本说明、音质标签、权限标签、热评、收藏信息）
  - 添加10个MV数据（music_videos.json）
  - 修改SearchTab：点击搜索结果项导航到SearchResultTab
  - 修改MainActivity和RecommendTab：添加SearchResultTab导航逻辑
  - 成功构建并验证功能
- ✅ 修复SearchTab中文输入问题
  - 添加KeyboardOptions配置（keyboardType: Text, imeAction: Search）
  - 添加KeyboardActions处理搜索动作
  - 添加LocalSoftwareKeyboardController支持键盘控制
  - 改善中文输入法兼容性
  - 成功构建并验证功能
- ✅ 优化推荐页面TopBar图标
  - 将听歌识曲按钮的星星图标改为麦克风图标
  - 更加形象地表达"听歌识曲"功能，用户体验更直观
  - 成功构建并验证功能
- ✅ 优化搜索页面关键间距
  - 移除TopAppBar的windowInsets，减少搜索栏与页面顶部的空白
  - 缩小猜你喜欢与双榜单之间的间距（从12dp缩减至4dp）
  - 整体页面更加紧凑，视觉效果更佳
  - 成功构建并验证功能
- ✅ 优化搜索页面布局间距（参考每日推荐页面）
  - 移除快捷入口顶部间距，内容区域直接开始（与每日推荐页面风格一致）
  - 统一各区域间距为12dp（快捷入口、搜索历史、猜你喜欢、双榜单）
  - 优化整体视觉平衡，保持紧凑均匀的布局风格
  - 成功构建并验证功能
- ✅ 完成搜索页面（SearchTab）作为二级页面
  - 创建SearchContract和SearchPresenter（MVP模式）
  - 实现顶部搜索栏（返回按钮、搜索输入框、搜索图标）
  - 创建快捷入口区域（歌手、曲风、专区、识曲、听书 - 5个均匀分布的图标）
  - 实现搜索历史功能（显示最近3条记录、支持点击搜索、可清空历史）
  - 创建猜你喜欢版块（6首推荐歌曲，2列网格布局）
  - 实现双榜单设计（热搜榜和热歌榜并列显示）
  - 创建RankListCard组件（榜单卡片，支持不同样式）
  - 热搜榜特效（第1名"爆"红色标签、第2名火焰emoji图标）
  - 热歌榜特效（上升/下降趋势箭头，前3名显示）
  - 实现实时搜索功能（输入关键词即时过滤歌曲）
  - 实现搜索结果显示（搜索时切换到结果列表）
  - 创建搜索历史记录保存系统（JSON格式保存到本地files目录）
  - 修改RecommendTab：添加搜索页面导航（点击搜索栏进入SearchTab）
  - 使用状态管理实现页面导航（showSearchPage）
  - 成功构建并验证功能（仅deprecation警告，无错误）

### 2025-10-21
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
