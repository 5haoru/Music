"""
任务12：创建一个新的歌单,并添加首音乐
难度：中

人工操作步骤：
  1. 进入我的页面
  2. 点击创建歌单
  3. 输入歌单名称（可以是任意数字或名称，如"1"、"2"、"测试歌单"等）
  4. 添加歌曲

验证标准：
调用task_12_check_create_playlist_and_add_song函数进行验证

使用方式：
  方式1（推荐）：不传参数，自动检查最新创建的歌单
    python test_task_12.py

  方式2：传入歌单名称，检查指定歌单
    python test_task_12.py 1
    python test_task_12.py 2
    python test_task_12.py 测试歌单
"""

import sys
import os
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from verification_functions import task_12_check_create_playlist_and_add_song

def test(*args):
    print("=" * 70)
    print("任务12：创建一个新的歌单,并添加首音乐")
    print("=" * 70)
    print("\n📋 人工操作步骤：")
    print("  1. 进入我的页面")
    print("  2. 点击创建歌单")
    print("  3. 输入歌单名称（可以是任意数字或名称）")
    print("  4. 添加至少一首歌曲")

    if args:
        print(f"\n🔍 开始验证...（检查歌单名称: {args[0]}）")
    else:
        print("\n🔍 开始验证...（检查最新创建的歌单）")

    # 根据函数签名调用验证函数
    if args:
        result = task_12_check_create_playlist_and_add_song(*args)
    else:
        result = task_12_check_create_playlist_and_add_song()

    print("\n" + "=" * 70)
    if result:
        print("✓ 测试通过 - 任务12完成")
        print("=" * 70)
        return True
    else:
        print("✗ 测试失败 - 任务12未完成")
        print("\n提示：")
        print("  - 方式1（推荐）：不传参数，自动检查最新创建的歌单")
        print("    python test_task_12.py")
        print("  - 方式2：传入歌单名称（可以是数字如 1, 2, 3）")
        print("    python test_task_12.py 1")
        print("    python test_task_12.py 测试歌单")
        print("=" * 70)
        return False

if __name__ == "__main__":
    # 从命令行获取参数
    args = sys.argv[1:] if len(sys.argv) > 1 else []
    success = test(*args)
    sys.exit(0 if success else 1)
