"""
任务22：查看每周、每月听歌时长
难度：高

人工操作步骤：
  1. 进入我的页面
  2. 点击听歌时长
  3. 查看周/月统计

验证标准：
调用task_22_check_view_listening_stats函数进行验证

参数：stat_type（'weekly'或'monthly'），默认自动检测
"""

import sys
import os
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from verification_functions import task_22_check_view_listening_stats, read_json_from_device

def test(stat_type=None):
    print("=" * 70)
    print("任务22：查看每周、每月听歌时长")
    print("=" * 70)
    print("\n📋 人工操作步骤：")
    print("  1. 进入我的页面")
    print("  2. 点击听歌时长")
    print("  3. 查看周/月统计")

    # 如果没有指定stat_type，自动从listening_stats.json检测查看过的统计类型
    if stat_type is None:
        print("\n🔍 自动检测查看过的统计类型...")
        stats_data = read_json_from_device('autotest/listening_stats.json')

        if stats_data and 'viewedStats' in stats_data:
            viewed_stats = stats_data['viewedStats']
            weekly_viewed = viewed_stats.get('weekly', False)
            monthly_viewed = viewed_stats.get('monthly', False)

            # 优先检测最近查看的（假设两者都为True时，选择monthly）
            if monthly_viewed:
                stat_type = 'monthly'
                print(f"✓ 检测到查看了月度统计")
            elif weekly_viewed:
                stat_type = 'weekly'
                print(f"✓ 检测到查看了周统计")
            else:
                print("\n" + "=" * 70)
                print("✗ 错误：未检测到查看过的统计")
                print("提示：请先查看周统计或月度统计")
                print("提示：或手动指定类型: python test_task_22.py weekly")
                print("=" * 70)
                return False
        else:
            print("\n" + "=" * 70)
            print("✗ 错误：无法读取统计数据")
            print("提示：或手动指定类型: python test_task_22.py weekly")
            print("=" * 70)
            return False
    else:
        print(f"\n🔍 使用指定的统计类型: {stat_type}")

    print(f"\n🔍 开始验证...")

    result = task_22_check_view_listening_stats(stat_type)

    print("\n" + "=" * 70)
    if result:
        stat_name = "月度统计" if stat_type == "monthly" else "周统计"
        print(f"✓ 测试通过 - 已查看{stat_name}")
        print("=" * 70)
        return True
    else:
        stat_name = "月度统计" if stat_type == "monthly" else "周统计"
        print(f"✗ 测试失败 - 未查看{stat_name}")
        print("提示：请确保已查看听歌时长统计")
        print("=" * 70)
        return False

if __name__ == "__main__":
    # 可以通过命令行参数传入stat_type
    # 用法1：python test_task_22.py          # 自动检测
    # 用法2：python test_task_22.py weekly   # 手动指定周统计
    # 用法3：python test_task_22.py monthly  # 手动指定月度统计
    stat_type = sys.argv[1] if len(sys.argv) > 1 else None
    success = test(stat_type)
    sys.exit(0 if success else 1)
