"""
任务16：查看一首歌曲的歌词
难度：中

人工操作步骤：
  1. 进入播放页面
  2. 点击显示歌词按钮

验证标准：
调用task_16_check_view_lyrics函数进行验证
"""

import sys
import os
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from verification_functions import task_16_check_view_lyrics

def test(*args):
    print("=" * 70)
    print("任务16：查看一首歌曲的歌词")
    print("=" * 70)
    print("\n📋 人工操作步骤：")
    print("  1. 进入播放页面")
    print("  2. 点击显示歌词按钮")
    print("\n🔍 开始验证...")

    # 根据函数签名调用验证函数
    if args:
        result = task_16_check_view_lyrics(*args)
    else:
        result = task_16_check_view_lyrics()

    print("\n" + "=" * 70)
    if result:
        print("✓ 测试通过 - 任务16完成")
        print("=" * 70)
        return True
    else:
        print("✗ 测试失败 - 任务16未完成")
        print("提示：请检查操作步骤是否正确执行")
        print("=" * 70)
        return False

if __name__ == "__main__":
    # 从命令行获取参数
    args = sys.argv[1:] if len(sys.argv) > 1 else []
    success = test(*args)
    sys.exit(0 if success else 1)
