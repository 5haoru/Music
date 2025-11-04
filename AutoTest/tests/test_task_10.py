"""
任务10：随机进入'我的'中的一个歌单
难度：低

人工操作步骤：
  1. 进入我的页面
  2. 点击任意一个歌单

验证标准：
调用task_10_check_enter_playlist函数进行验证
"""

import sys
import os
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from verification_functions import task_10_check_enter_playlist

def test(*args):
    print("=" * 70)
    print("任务10：随机进入'我的'中的一个歌单")
    print("=" * 70)
    print("\n📋 人工操作步骤：")
    print("  1. 进入我的页面")
    print("  2. 点击任意一个歌单")
    print("\n🔍 开始验证...")

    # 根据函数签名调用验证函数
    if args:
        result = task_10_check_enter_playlist(*args)
    else:
        result = task_10_check_enter_playlist()

    print("\n" + "=" * 70)
    if result:
        print("✓ 测试通过 - 任务10完成")
        print("=" * 70)
        return True
    else:
        print("✗ 测试失败 - 任务10未完成")
        print("提示：请检查操作步骤是否正确执行")
        print("=" * 70)
        return False

if __name__ == "__main__":
    # 从命令行获取参数
    args = sys.argv[1:] if len(sys.argv) > 1 else []
    success = test(*args)
    sys.exit(0 if success else 1)
