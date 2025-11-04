"""
任务24：在歌单中选择一首歌曲并发表评论
难度：高

人工操作步骤：
  1. 进入歌曲
  2. 点击评论区
  3. 输入评论
  4. 发表

验证标准：
调用task_24_check_post_comment函数进行验证

参数：song_id, comment_content（可选，用于精确匹配）
"""

import sys
import os
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from verification_functions import task_24_check_post_comment

def test(*args):
    print("=" * 70)
    print("任务24：在歌单中选择一首歌曲并发表评论")
    print("=" * 70)
    print("\n📋 人工操作步骤：")
    print("  1. 进入歌曲")
    print("  2. 点击评论区")
    print("  3. 输入评论")
    print("  4. 发表")
    print("\n🔍 开始验证...")

    # 根据函数签名调用验证函数
    if args:
        result = task_24_check_post_comment(*args)
    else:
        result = task_24_check_post_comment()

    print("\n" + "=" * 70)
    if result:
        print("✓ 测试通过 - 任务24完成")
        print("=" * 70)
        return True
    else:
        print("✗ 测试失败 - 任务24未完成")
        print("提示：请检查操作步骤是否正确执行")
        print("提示：参数：song_id, comment_content（可选，用于精确匹配）")
        print("=" * 70)
        return False

if __name__ == "__main__":
    # 从命令行获取参数
    args = sys.argv[1:] if len(sys.argv) > 1 else []
    success = test(*args)
    sys.exit(0 if success else 1)
