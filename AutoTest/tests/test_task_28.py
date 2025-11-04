"""
任务28：搜索一个歌手,在歌手主页选择一个专辑并收藏
难度：高

人工操作步骤：
  1. 搜索歌手
  2. 进入歌手页面
  3. 选择专辑
  4. 收藏

验证标准：
调用task_28_check_collect_album函数进行验证

参数：album_id，默认'album_001'
"""

import sys
import os
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from verification_functions import task_28_check_collect_album

def test(*args):
    print("=" * 70)
    print("任务28：搜索一个歌手,在歌手主页选择一个专辑并收藏")
    print("=" * 70)
    print("\n📋 人工操作步骤：")
    print("  1. 搜索歌手")
    print("  2. 进入歌手页面")
    print("  3. 选择专辑")
    print("  4. 收藏")
    print("\n🔍 开始验证...")

    # 根据函数签名调用验证函数
    if args:
        result = task_28_check_collect_album(*args)
    else:
        result = task_28_check_collect_album()

    print("\n" + "=" * 70)
    if result:
        print("✓ 测试通过 - 任务28完成")
        print("=" * 70)
        return True
    else:
        print("✗ 测试失败 - 任务28未完成")
        print("提示：请检查操作步骤是否正确执行")
        print("提示：参数：album_id，默认'album_001'")
        print("=" * 70)
        return False

if __name__ == "__main__":
    # 从命令行获取参数
    args = sys.argv[1:] if len(sys.argv) > 1 else []
    success = test(*args)
    sys.exit(0 if success else 1)
