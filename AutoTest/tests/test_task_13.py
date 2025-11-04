"""
任务13：搜索'烟雨'并播放
难度：中

人工操作步骤：
  1. 点击搜索按钮
  2. 输入'烟雨'
  3. 点击搜索结果播放

验证标准：
调用task_13_check_search_and_play函数进行验证

参数：search_query（搜索关键词），默认'烟雨'
"""

import sys
import os
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from verification_functions import task_13_check_search_and_play

def test(*args):
    print("=" * 70)
    print("任务13：搜索'烟雨'并播放")
    print("=" * 70)
    print("\n📋 人工操作步骤：")
    print("  1. 点击搜索按钮")
    print("  2. 输入'烟雨'")
    print("  3. 点击搜索结果播放")
    print("\n🔍 开始验证...")

    # 根据函数签名调用验证函数
    if args:
        result = task_13_check_search_and_play(*args)
    else:
        result = task_13_check_search_and_play()

    print("\n" + "=" * 70)
    if result:
        print("✓ 测试通过 - 任务13完成")
        print("=" * 70)
        return True
    else:
        print("✗ 测试失败 - 任务13未完成")
        print("提示：请检查操作步骤是否正确执行")
        print("提示：参数：search_query（搜索关键词），默认'烟雨'")
        print("=" * 70)
        return False

if __name__ == "__main__":
    # 从命令行获取参数
    args = sys.argv[1:] if len(sys.argv) > 1 else []
    success = test(*args)
    sys.exit(0 if success else 1)
