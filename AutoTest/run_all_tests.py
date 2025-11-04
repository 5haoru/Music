"""
音乐App自动化测试 - 批量测试运行器
一键运行所有测试用例并生成测试报告
"""

import sys
import json
from datetime import datetime
from verification_functions import *

# 定义所有测试用例
# 格式: (任务ID, 任务名称, 验证函数, 参数列表, 难度)
TEST_CASES = [
    # 低难度任务
    (1, "打开app并进入'推荐'首页", task_01_check_open_recommend_page, [], "低"),
    (2, "从'推荐'页面进入'漫游'页面", task_02_check_navigate_to_stroll, [], "低"),
    (3, "从'推荐'页面进入'我的'页面", task_03_check_navigate_to_profile, [], "低"),
    (4, "播放当前暂停的歌曲", task_04_check_play_song, [], "低"),
    (5, "暂停播放当前的歌曲", task_05_check_pause_song, [], "低"),
    (6, "切换播放上一首歌曲", task_06_check_switch_previous_song, [None], "低"),
    (7, "将播放模式改为随机播放", task_07_check_shuffle_mode, [], "低"),
    (8, "收藏当前歌曲", task_08_check_favorite_song, ["song_001"], "低"),
    (9, "调节当前音乐播放的音量", task_09_check_volume_adjusted, [None], "低"),
    (10, "随机进入'我的'中的一个歌单", task_10_check_enter_playlist, [], "低"),

    # 中难度任务
    (11, "进入'每日推荐'播放第三首歌曲", task_11_check_daily_recommend_third_song, [], "中"),
    (12, "创建一个新的歌单,并添加首音乐", task_12_check_create_playlist_and_add_song, ["我的最爱"], "中"),
    (13, "搜索'烟雨'并播放", task_13_check_search_and_play, ["烟雨"], "中"),
    (14, "搜索某个歌手并播放其中的第一首歌", task_14_check_search_artist_and_play, [], "中"),
    (15, "查看一首歌曲的详细信息", task_15_check_view_song_detail, ["song_001"], "中"),
    (16, "查看一首歌曲的歌词", task_16_check_view_lyrics, [], "中"),
    (17, "漫游播放并设置播放场景为'伪感'", task_17_check_stroll_scene_setting, ["伪感"], "中"),
    # 任务18: 不支持自动化验证
    (19, "在排行榜中随机选择打开一个榜单并播放第一首歌曲", task_19_check_rank_list_play, [], "中"),
    (20, "在推荐歌单中随机选择一个歌单并收藏", task_20_check_collect_playlist, ["playlist_001"], "中"),
    (21, "删除歌单中的第一首歌", task_21_check_delete_song_from_playlist, ["user_playlist_001", 1], "中"),

    # 高难度任务
    (22, "查看每周、每月听歌时长", task_22_check_view_listening_stats, ["weekly"], "高"),
    # 任务23: 不支持自动化验证
    (24, "在歌单中选择一首歌曲并发表评论", task_24_check_post_comment, ["song_001", None], "高"),
    # 任务25、26: 不支持自动化验证
    (27, "更改歌单的排序顺序", task_27_check_playlist_sort_order, ["user_playlist_001", "time_desc"], "高"),
    (28, "搜索一个歌手,在歌手主页选择一个专辑并收藏", task_28_check_collect_album, ["album_001"], "高"),
    (29, "将关注列表中的一位歌手删除", task_29_check_unfollow_artist, ["artist_002"], "高"),
    (30, "搜索一首歌曲并播放MV", task_30_check_play_mv, ["mv_001"], "高"),
    (31, "更改播放器样式", task_31_check_change_player_style, ["style_001"], "高"),
]


def run_single_test(task_id, task_name, func, args, difficulty):
    """
    运行单个测试用例
    :return: (task_id, task_name, result, error_msg, difficulty)
    """
    try:
        result = func(*args)
        return (task_id, task_name, result, None, difficulty)
    except Exception as e:
        return (task_id, task_name, False, str(e), difficulty)


def run_all_tests():
    """运行所有测试用例"""
    print("=" * 70)
    print("音乐App自动化测试 - 批量测试运行器")
    print("=" * 70)
    print(f"开始时间: {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}")
    print(f"测试用例数量: {len(TEST_CASES)}")
    print("=" * 70)
    print()

    results = []
    passed = 0
    failed = 0
    errors = 0

    # 按难度分组
    difficulty_groups = {"低": [], "中": [], "高": []}

    for task_id, task_name, func, args, difficulty in TEST_CASES:
        print(f"[任务{task_id:02d}] {task_name}...", end=" ")

        task_id_val, task_name_val, result, error_msg, diff = run_single_test(
            task_id, task_name, func, args, difficulty
        )

        results.append({
            "task_id": task_id_val,
            "task_name": task_name_val,
            "result": result,
            "error": error_msg,
            "difficulty": diff
        })

        if error_msg:
            print(f"✗ 错误")
            print(f"          错误信息: {error_msg}")
            errors += 1
            difficulty_groups[difficulty].append((task_id, task_name, "错误", error_msg))
        elif result:
            print("✓ 通过")
            passed += 1
            difficulty_groups[difficulty].append((task_id, task_name, "通过", None))
        else:
            print("✗ 失败")
            failed += 1
            difficulty_groups[difficulty].append((task_id, task_name, "失败", None))

    # 打印总结
    print()
    print("=" * 70)
    print("测试总结")
    print("=" * 70)
    total = passed + failed + errors
    print(f"总计: {total} 个测试")
    print(f"通过: {passed} ({passed/total*100:.1f}%)")
    print(f"失败: {failed} ({failed/total*100:.1f}%)")
    print(f"错误: {errors} ({errors/total*100:.1f}%)")
    print()

    # 按难度打印详细结果
    for difficulty in ["低", "中", "高"]:
        if difficulty_groups[difficulty]:
            print(f"\n【{difficulty}难度任务】")
            print("-" * 70)
            diff_passed = sum(1 for _, _, status, _ in difficulty_groups[difficulty] if status == "通过")
            diff_total = len(difficulty_groups[difficulty])
            print(f"通过率: {diff_passed}/{diff_total} ({diff_passed/diff_total*100:.1f}%)")
            print()
            for task_id, task_name, status, error in difficulty_groups[difficulty]:
                status_symbol = "✓" if status == "通过" else "✗"
                print(f"  {status_symbol} 任务{task_id:02d}: {task_name} - {status}")
                if error:
                    print(f"           错误: {error}")

    print()
    print("=" * 70)
    print(f"结束时间: {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}")
    print("=" * 70)

    # 保存测试报告
    save_test_report(results, passed, failed, errors)

    return passed, failed, errors


def save_test_report(results, passed, failed, errors):
    """保存测试报告为JSON文件"""
    report = {
        "timestamp": datetime.now().strftime('%Y-%m-%d %H:%M:%S'),
        "total": len(results),
        "passed": passed,
        "failed": failed,
        "errors": errors,
        "pass_rate": f"{passed/len(results)*100:.1f}%" if results else "0%",
        "results": results
    }

    report_file = f"test_report_{datetime.now().strftime('%Y%m%d_%H%M%S')}.json"
    with open(report_file, 'w', encoding='utf-8') as f:
        json.dump(report, f, ensure_ascii=False, indent=2)

    print(f"\n测试报告已保存到: {report_file}")


def run_tests_by_difficulty(difficulty):
    """按难度运行测试"""
    print(f"运行【{difficulty}难度】测试用例")
    print("=" * 70)

    filtered_cases = [tc for tc in TEST_CASES if tc[4] == difficulty]
    passed = 0
    failed = 0

    for task_id, task_name, func, args, diff in filtered_cases:
        print(f"[任务{task_id:02d}] {task_name}...", end=" ")
        _, _, result, error_msg, _ = run_single_test(task_id, task_name, func, args, diff)

        if error_msg:
            print(f"✗ 错误: {error_msg}")
        elif result:
            print("✓ 通过")
            passed += 1
        else:
            print("✗ 失败")
            failed += 1

    print()
    print(f"通过: {passed}/{len(filtered_cases)}")
    return passed, failed


if __name__ == "__main__":
    if len(sys.argv) > 1:
        # 支持按难度运行
        difficulty = sys.argv[1]
        if difficulty in ["低", "中", "高"]:
            run_tests_by_difficulty(difficulty)
        else:
            print(f"无效的难度参数: {difficulty}")
            print("用法: python run_all_tests.py [低|中|高]")
            sys.exit(1)
    else:
        # 运行所有测试
        passed, failed, errors = run_all_tests()

        # 根据结果设置退出码
        if errors > 0:
            sys.exit(2)  # 有错误
        elif failed > 0:
            sys.exit(1)  # 有失败
        else:
            sys.exit(0)  # 全部通过
