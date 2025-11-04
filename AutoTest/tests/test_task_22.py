"""
任务22：查看每周、每月听歌时长
难度：高

人工操作步骤：
  1. 进入我的页面
  2. 点击听歌时长
  3. 查看周/月统计

验证标准：
调用task_22_check_view_listening_stats函数进行验证

参数：stat_type（'weekly'或'monthly'），默认'weekly'
"""

import sys
import os
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from verification_functions import task_22_check_view_listening_stats

def test(*args):
    print("=" * 70)
    print("任务22：查看每周、每月听歌时长")
    print("=" * 70)
    print("\n📋 人工操作步骤：")
    print("  1. 进入我的页面")
    print("  2. 点击听歌时长")
    print("  3. 查看周/月统计")
    print("\n🔍 开始验证...")

    # 根据函数签名调用验证函数
    if args:
        result = task_22_check_view_listening_stats(*args)
    else:
        result = task_22_check_view_listening_stats()

    print("\n" + "=" * 70)
    if result:
        print("✓ 测试通过 - 任务22完成")
        print("=" * 70)
        return True
    else:
        print("✗ 测试失败 - 任务22未完成")
        print("提示：请检查操作步骤是否正确执行")
        print("提示：参数：stat_type（'weekly'或'monthly'），默认'weekly'")
        print("=" * 70)
        return False

if __name__ == "__main__":
    # 从命令行获取参数
    args = sys.argv[1:] if len(sys.argv) > 1 else []
    success = test(*args)
    sys.exit(0 if success else 1)
