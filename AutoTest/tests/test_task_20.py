"""
任务20：在推荐歌单中随机选择一个歌单并收藏
难度：中

人工操作步骤：
  1. 进入推荐
  2. 找到推荐歌单
  3. 点击收藏

验证标准：
调用task_20_check_collect_playlist函数进行验证

参数：playlist_id（歌单ID），默认'playlist_001'
"""

import sys
import os
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from verification_functions import task_20_check_collect_playlist

def test(*args):
    print("=" * 70)
    print("任务20：在推荐歌单中随机选择一个歌单并收藏")
    print("=" * 70)
    print("\n📋 人工操作步骤：")
    print("  1. 进入推荐")
    print("  2. 找到推荐歌单")
    print("  3. 点击收藏")
    print("\n🔍 开始验证...")

    # 根据函数签名调用验证函数
    if args:
        result = task_20_check_collect_playlist(*args)
    else:
        result = task_20_check_collect_playlist()

    print("\n" + "=" * 70)
    if result:
        print("✓ 测试通过 - 任务20完成")
        print("=" * 70)
        return True
    else:
        print("✗ 测试失败 - 任务20未完成")
        print("提示：请检查操作步骤是否正确执行")
        print("提示：参数：playlist_id（歌单ID），默认'playlist_001'")
        print("=" * 70)
        return False

if __name__ == "__main__":
    # 从命令行获取参数
    args = sys.argv[1:] if len(sys.argv) > 1 else []
    success = test(*args)
    sys.exit(0 if success else 1)
