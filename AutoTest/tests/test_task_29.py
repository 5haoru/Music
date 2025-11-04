"""
任务29：将关注列表中的一位歌手删除
难度：高

人工操作步骤：
  1. 进入关注列表
  2. 找到歌手
  3. 取消关注

验证标准：
调用task_29_check_unfollow_artist函数进行验证

参数：artist_id，默认'artist_002'
"""

import sys
import os
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from verification_functions import task_29_check_unfollow_artist

def test(*args):
    print("=" * 70)
    print("任务29：将关注列表中的一位歌手删除")
    print("=" * 70)
    print("\n📋 人工操作步骤：")
    print("  1. 进入关注列表")
    print("  2. 找到歌手")
    print("  3. 取消关注")
    print("\n🔍 开始验证...")

    # 根据函数签名调用验证函数
    if args:
        result = task_29_check_unfollow_artist(*args)
    else:
        result = task_29_check_unfollow_artist()

    print("\n" + "=" * 70)
    if result:
        print("✓ 测试通过 - 任务29完成")
        print("=" * 70)
        return True
    else:
        print("✗ 测试失败 - 任务29未完成")
        print("提示：请检查操作步骤是否正确执行")
        print("提示：参数：artist_id，默认'artist_002'")
        print("=" * 70)
        return False

if __name__ == "__main__":
    # 从命令行获取参数
    args = sys.argv[1:] if len(sys.argv) > 1 else []
    success = test(*args)
    sys.exit(0 if success else 1)
