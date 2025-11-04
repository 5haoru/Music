"""
任务19：在排行榜中随机选择打开一个榜单并播放第一首歌曲
难度：中

人工操作步骤：
  1. 进入排行榜
  2. 选择一个榜单
  3. 播放第一首歌

验证标准：
调用task_19_check_rank_list_play函数进行验证
"""

import sys
import os
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from verification_functions import task_19_check_rank_list_play

def test(*args):
    print("=" * 70)
    print("任务19：在排行榜中随机选择打开一个榜单并播放第一首歌曲")
    print("=" * 70)
    print("\n📋 人工操作步骤：")
    print("  1. 进入排行榜")
    print("  2. 选择一个榜单")
    print("  3. 播放第一首歌")
    print("\n🔍 开始验证...")

    # 根据函数签名调用验证函数
    if args:
        result = task_19_check_rank_list_play(*args)
    else:
        result = task_19_check_rank_list_play()

    print("\n" + "=" * 70)
    if result:
        print("✓ 测试通过 - 任务19完成")
        print("=" * 70)
        return True
    else:
        print("✗ 测试失败 - 任务19未完成")
        print("提示：请检查操作步骤是否正确执行")
        print("=" * 70)
        return False

if __name__ == "__main__":
    # 从命令行获取参数
    args = sys.argv[1:] if len(sys.argv) > 1 else []
    success = test(*args)
    sys.exit(0 if success else 1)
