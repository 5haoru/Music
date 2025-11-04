"""
自动生成剩余测试文件的脚本
"""

import os

# 测试任务定义
# 格式: (任务编号, 任务描述, 难度, 操作步骤, 验证函数名, 参数说明)
TASKS = [
    (5, "暂停播放当前的歌曲", "低",
     ["进入播放页面", "点击暂停按钮"],
     "task_05_check_pause_song", ""),

    (6, "切换播放上一首歌曲", "低",
     ["进入播放页面", "点击上一首按钮（←）"],
     "task_06_check_switch_previous_song",
     "可选参数：expected_song_id（期望的歌曲ID）"),

    (9, "调节当前音乐播放的音量", "低",
     ["进入播放页面", "调整音量滑块或按钮"],
     "task_09_check_volume_adjusted",
     "可选参数：expected_volume（期望的音量值0-100）"),

    (10, "随机进入'我的'中的一个歌单", "低",
     ["进入我的页面", "点击任意一个歌单"],
     "task_10_check_enter_playlist", ""),

    (11, "进入'每日推荐'播放第三首歌曲", "中",
     ["进入推荐页面", "点击'每日推荐'", "点击第3首歌曲播放"],
     "task_11_check_daily_recommend_third_song", ""),

    (12, "创建一个新的歌单,并添加首音乐", "中",
     ["进入我的页面", "点击创建歌单", "输入歌单名称", "添加歌曲"],
     "task_12_check_create_playlist_and_add_song",
     "参数：playlist_name（歌单名称），默认'我的最爱'"),

    (13, "搜索'烟雨'并播放", "中",
     ["点击搜索按钮", "输入'烟雨'", "点击搜索结果播放"],
     "task_13_check_search_and_play",
     "参数：search_query（搜索关键词），默认'烟雨'"),

    (14, "搜索某个歌手并播放其中的第一首歌", "中",
     ["点击搜索", "搜索歌手名", "进入歌手页面", "播放第一首歌"],
     "task_14_check_search_artist_and_play", ""),

    (15, "查看一首歌曲的详细信息", "中",
     ["找到一首歌曲", "点击进入歌曲详情页面"],
     "task_15_check_view_song_detail",
     "参数：song_id（歌曲ID），默认'song_001'"),

    (16, "查看一首歌曲的歌词", "中",
     ["进入播放页面", "点击显示歌词按钮"],
     "task_16_check_view_lyrics", ""),

    (19, "在排行榜中随机选择打开一个榜单并播放第一首歌曲", "中",
     ["进入排行榜", "选择一个榜单", "播放第一首歌"],
     "task_19_check_rank_list_play", ""),

    (20, "在推荐歌单中随机选择一个歌单并收藏", "中",
     ["进入推荐", "找到推荐歌单", "点击收藏"],
     "task_20_check_collect_playlist",
     "参数：playlist_id（歌单ID），默认'playlist_001'"),

    (21, "删除歌单中的第一首歌", "中",
     ["进入歌单", "选择第一首歌", "删除"],
     "task_21_check_delete_song_from_playlist",
     "参数：playlist_id, expected_count（删除后的歌曲数量）"),

    (22, "查看每周、每月听歌时长", "高",
     ["进入我的页面", "点击听歌时长", "查看周/月统计"],
     "task_22_check_view_listening_stats",
     "参数：stat_type（'weekly'或'monthly'），默认'weekly'"),

    (24, "在歌单中选择一首歌曲并发表评论", "高",
     ["进入歌曲", "点击评论区", "输入评论", "发表"],
     "task_24_check_post_comment",
     "参数：song_id, comment_content（可选，用于精确匹配）"),

    (27, "更改歌单的排序顺序", "高",
     ["进入歌单", "打开设置", "选择排序方式"],
     "task_27_check_playlist_sort_order",
     "参数：playlist_id, expected_order（如'time_desc'）"),

    (28, "搜索一个歌手,在歌手主页选择一个专辑并收藏", "高",
     ["搜索歌手", "进入歌手页面", "选择专辑", "收藏"],
     "task_28_check_collect_album",
     "参数：album_id，默认'album_001'"),

    (29, "将关注列表中的一位歌手删除", "高",
     ["进入关注列表", "找到歌手", "取消关注"],
     "task_29_check_unfollow_artist",
     "参数：artist_id，默认'artist_002'"),

    (30, "搜索一首歌曲并播放MV", "高",
     ["搜索歌曲", "找到MV", "播放"],
     "task_30_check_play_mv",
     "参数：mv_id，默认'mv_001'"),

    (31, "更改播放器样式", "高",
     ["进入播放器设置", "选择样式"],
     "task_31_check_change_player_style",
     "参数：style_id，默认'style_001'"),
]

def generate_test_file(task_num, desc, difficulty, steps, func_name, param_info):
    """生成单个测试文件"""

    # 格式化操作步骤
    steps_text = "\n".join([f"  {i+1}. {step}" for i, step in enumerate(steps)])

    # 构建文件内容
    content = f'''"""
任务{task_num}：{desc}
难度：{difficulty}

人工操作步骤：
{steps_text}

验证标准：
调用{func_name}函数进行验证
'''

    if param_info:
        content += f"\n{param_info}\n"

    content += '''"""

import sys
import os
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from verification_functions import ''' + func_name + '''

def test(*args):
    print("=" * 70)
    print("任务''' + str(task_num) + '''：''' + desc + '''")
    print("=" * 70)
    print("\\n📋 人工操作步骤：")
'''

    for i, step in enumerate(steps, 1):
        content += f'    print("  {i}. {step}")\n'

    content += '''    print("\\n🔍 开始验证...")

    # 根据函数签名调用验证函数
    if args:
        result = ''' + func_name + '''(*args)
    else:
        result = ''' + func_name + '''()

    print("\\n" + "=" * 70)
    if result:
        print("✓ 测试通过 - 任务''' + str(task_num) + '''完成")
        print("=" * 70)
        return True
    else:
        print("✗ 测试失败 - 任务''' + str(task_num) + '''未完成")
        print("提示：请检查操作步骤是否正确执行")
'''

    if param_info:
        content += f'        print("提示：{param_info}")\n'

    content += '''        print("=" * 70)
        return False

if __name__ == "__main__":
    # 从命令行获取参数
    args = sys.argv[1:] if len(sys.argv) > 1 else []
    success = test(*args)
    sys.exit(0 if success else 1)
'''

    return content

def main():
    # 确保tests目录存在
    tests_dir = "tests"
    if not os.path.exists(tests_dir):
        os.makedirs(tests_dir)
        print(f"创建目录: {tests_dir}")

    # 生成测试文件
    generated = 0
    skipped = 0

    for task_num, desc, difficulty, steps, func_name, param_info in TASKS:
        filename = f"test_task_{task_num:02d}.py"
        filepath = os.path.join(tests_dir, filename)

        if os.path.exists(filepath):
            print(f"跳过 {filename} (已存在)")
            skipped += 1
            continue

        content = generate_test_file(task_num, desc, difficulty, steps, func_name, param_info)

        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(content)

        print(f"[OK] Generated {filename}")
        generated += 1

    print(f"\nDone! Generated {generated} test files, skipped {skipped} existing files")

if __name__ == "__main__":
    main()
