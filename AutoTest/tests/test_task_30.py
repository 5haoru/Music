"""
任务30：搜索一首歌曲并播放MV
难度：高

人工操作步骤：
  1. 搜索歌曲
  2. 找到MV
  3. 播放

验证标准：
调用task_30_check_play_mv函数进行验证

参数：mv_id，默认自动检测
"""

import sys
import os
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from verification_functions import task_30_check_play_mv, read_json_from_device

def test(mv_id=None):
    print("=" * 70)
    print("任务30：搜索一首歌曲并播放MV")
    print("=" * 70)
    print("\n📋 人工操作步骤：")
    print("  1. 搜索歌曲")
    print("  2. 找到MV")
    print("  3. 播放")

    # 如果没有指定mv_id，自动从mv_playback.json获取当前播放的MV
    if mv_id is None:
        print("\n🔍 自动检测当前播放的MV...")
        mv_data = read_json_from_device('autotest/mv_playback.json')

        if mv_data and 'currentMV' in mv_data and mv_data['currentMV']:
            mv_id = mv_data['currentMV'].get('mvId')
            mv_name = mv_data['currentMV'].get('mvName', 'Unknown')
            artist = mv_data['currentMV'].get('artist', 'Unknown')
            is_playing = mv_data['currentMV'].get('isPlaying', False)

            print(f"✓ 检测到当前MV: 《{mv_name}》 - {artist}")
            print(f"  MV ID: {mv_id}")
            print(f"  播放状态: {'播放中' if is_playing else '未播放'}")
        else:
            print("\n" + "=" * 70)
            print("✗ 错误：无法检测到当前播放的MV")
            print("提示：请先搜索并播放一个MV")
            print("提示：或手动指定MV ID: python test_task_30.py mv_001")
            print("=" * 70)
            return False
    else:
        print(f"\n🔍 使用指定的MV ID: {mv_id}")

    print(f"\n🔍 开始验证...")

    result = task_30_check_play_mv(mv_id)

    print("\n" + "=" * 70)
    if result:
        print(f"✓ 测试通过 - MV {mv_id} 正在播放")
        print("=" * 70)
        return True
    else:
        print(f"✗ 测试失败 - MV {mv_id} 未在播放状态")
        print("提示：请确保已点击播放MV")
        print("=" * 70)
        return False

if __name__ == "__main__":
    # 可以通过命令行参数传入mv_id
    # 用法1：python test_task_30.py          # 自动检测当前MV
    # 用法2：python test_task_30.py mv_002  # 手动指定MV ID
    mv_id = sys.argv[1] if len(sys.argv) > 1 else None
    success = test(mv_id)
    sys.exit(0 if success else 1)
