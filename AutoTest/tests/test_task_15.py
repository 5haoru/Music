"""
任务15：查看一首歌曲的详细信息
难度：中

人工操作步骤：
  1. 找到一首歌曲
  2. 点击进入歌曲详情页面

验证标准：
调用task_15_check_view_song_detail函数进行验证

参数：song_id（歌曲ID），默认'song_001'
"""

import sys
import os
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from verification_functions import task_15_check_view_song_detail

def test(*args):
    print("=" * 70)
    print("任务15：查看一首歌曲的详细信息")
    print("=" * 70)
    print("\n📋 人工操作步骤：")
    print("  1. 找到一首歌曲")
    print("  2. 点击进入歌曲详情页面")
    print("\n🔍 开始验证...")

    # 根据函数签名调用验证函数
    if args:
        result = task_15_check_view_song_detail(*args)
    else:
        result = task_15_check_view_song_detail()

    print("\n" + "=" * 70)
    if result:
        print("✓ 测试通过 - 任务15完成")
        print("=" * 70)
        return True
    else:
        print("✗ 测试失败 - 任务15未完成")
        print("提示：请检查操作步骤是否正确执行")
        print("提示：参数：song_id（歌曲ID），默认'song_001'")
        print("=" * 70)
        return False

if __name__ == "__main__":
    # 从命令行获取参数
    args = sys.argv[1:] if len(sys.argv) > 1 else []
    success = test(*args)
    sys.exit(0 if success else 1)
