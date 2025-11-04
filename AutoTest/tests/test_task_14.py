"""
任务14：搜索某个歌手并播放其中的第一首歌
难度：中

人工操作步骤：
  1. 点击搜索
  2. 搜索歌手名
  3. 进入歌手页面
  4. 播放第一首歌

验证标准：
调用task_14_check_search_artist_and_play函数进行验证
"""

import sys
import os
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from verification_functions import task_14_check_search_artist_and_play

def test(*args):
    print("=" * 70)
    print("任务14：搜索某个歌手并播放其中的第一首歌")
    print("=" * 70)
    print("\n📋 人工操作步骤：")
    print("  1. 点击搜索")
    print("  2. 搜索歌手名")
    print("  3. 进入歌手页面")
    print("  4. 播放第一首歌")
    print("\n🔍 开始验证...")

    # 根据函数签名调用验证函数
    if args:
        result = task_14_check_search_artist_and_play(*args)
    else:
        result = task_14_check_search_artist_and_play()

    print("\n" + "=" * 70)
    if result:
        print("✓ 测试通过 - 任务14完成")
        print("=" * 70)
        return True
    else:
        print("✗ 测试失败 - 任务14未完成")
        print("提示：请检查操作步骤是否正确执行")
        print("=" * 70)
        return False

if __name__ == "__main__":
    # 从命令行获取参数
    args = sys.argv[1:] if len(sys.argv) > 1 else []
    success = test(*args)
    sys.exit(0 if success else 1)
