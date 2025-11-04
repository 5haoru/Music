"""
任务6：切换播放上一首歌曲
难度：低

人工操作步骤：
  1. 进入播放页面
  2. 点击上一首按钮（←）

验证标准：
调用task_06_check_switch_previous_song函数进行验证

可选参数：expected_song_id（期望的歌曲ID）
"""

import sys
import os
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from verification_functions import task_06_check_switch_previous_song

def test(*args):
    print("=" * 70)
    print("任务6：切换播放上一首歌曲")
    print("=" * 70)
    print("\n📋 人工操作步骤：")
    print("  1. 进入播放页面")
    print("  2. 点击上一首按钮（←）")
    print("\n🔍 开始验证...")

    # 根据函数签名调用验证函数
    if args:
        result = task_06_check_switch_previous_song(*args)
    else:
        result = task_06_check_switch_previous_song()

    print("\n" + "=" * 70)
    if result:
        print("✓ 测试通过 - 任务6完成")
        print("=" * 70)
        return True
    else:
        print("✗ 测试失败 - 任务6未完成")
        print("提示：请检查操作步骤是否正确执行")
        print("提示：可选参数：expected_song_id（期望的歌曲ID）")
        print("=" * 70)
        return False

if __name__ == "__main__":
    # 从命令行获取参数
    args = sys.argv[1:] if len(sys.argv) > 1 else []
    success = test(*args)
    sys.exit(0 if success else 1)
