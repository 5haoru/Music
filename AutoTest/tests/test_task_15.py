"""
任务15：查看一首歌曲的详细信息
难度：中

人工操作步骤：
  1. 找到一首歌曲
  2. 点击进入歌曲详情页面

验证标准：
调用task_15_check_view_song_detail函数进行验证

参数：song_id（歌曲ID），默认自动检测
"""

import sys
import os
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from verification_functions import task_15_check_view_song_detail, read_json_from_device

def test(song_id=None):
    print("=" * 70)
    print("任务15：查看一首歌曲的详细信息")
    print("=" * 70)
    print("\n📋 人工操作步骤：")
    print("  1. 找到一首歌曲")
    print("  2. 点击进入歌曲详情页面")

    # 如果没有指定song_id，自动从app_state.json获取当前查看的歌曲详情
    if song_id is None:
        print("\n🔍 自动检测当前查看的歌曲详情...")
        app_state = read_json_from_device('autotest/app_state.json')

        if app_state and app_state.get('currentPage') == 'song_detail' and app_state.get('currentSongId'):
            song_id = app_state.get('currentSongId')
            print(f"✓ 检测到正在查看歌曲详情")
            print(f"  歌曲ID: {song_id}")
        else:
            print("\n" + "=" * 70)
            print("✗ 错误：无法检测到当前查看的歌曲详情")
            print("提示：请确保已进入歌曲详情页面")
            print("提示：或手动指定歌曲ID: python test_task_15.py song_001")
            print("=" * 70)
            return False
    else:
        print(f"\n🔍 使用指定的歌曲ID: {song_id}")

    print(f"\n🔍 开始验证...")

    result = task_15_check_view_song_detail(song_id)

    print("\n" + "=" * 70)
    if result:
        print(f"✓ 测试通过 - 已成功查看歌曲 {song_id} 的详细信息")
        print("=" * 70)
        return True
    else:
        print(f"✗ 测试失败 - 未正确查看歌曲 {song_id} 的详细信息")
        print("提示：请确保已进入歌曲详情页面")
        print("=" * 70)
        return False

if __name__ == "__main__":
    # 可以通过命令行参数传入song_id
    # 用法1：python test_task_15.py          # 自动检测当前歌曲
    # 用法2：python test_task_15.py song_002  # 手动指定歌曲ID
    song_id = sys.argv[1] if len(sys.argv) > 1 else None
    success = test(song_id)
    sys.exit(0 if success else 1)
