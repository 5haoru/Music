"""
任务30：搜索一首歌曲并播放MV
难度：高

人工操作步骤：
  1. 搜索歌曲
  2. 找到MV
  3. 播放

验证标准：
调用task_30_check_play_mv函数进行验证

参数：mv_id，默认'mv_001'
"""

import sys
import os
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from verification_functions import task_30_check_play_mv

def test(*args):
    print("=" * 70)
    print("任务30：搜索一首歌曲并播放MV")
    print("=" * 70)
    print("\n📋 人工操作步骤：")
    print("  1. 搜索歌曲")
    print("  2. 找到MV")
    print("  3. 播放")
    print("\n🔍 开始验证...")

    # 根据函数签名调用验证函数
    if args:
        result = task_30_check_play_mv(*args)
    else:
        result = task_30_check_play_mv()

    print("\n" + "=" * 70)
    if result:
        print("✓ 测试通过 - 任务30完成")
        print("=" * 70)
        return True
    else:
        print("✗ 测试失败 - 任务30未完成")
        print("提示：请检查操作步骤是否正确执行")
        print("提示：参数：mv_id，默认'mv_001'")
        print("=" * 70)
        return False

if __name__ == "__main__":
    # 从命令行获取参数
    args = sys.argv[1:] if len(sys.argv) > 1 else []
    success = test(*args)
    sys.exit(0 if success else 1)
