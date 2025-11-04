"""
任务31：更改播放器样式
难度：高

人工操作步骤：
  1. 进入播放器设置
  2. 选择样式

验证标准：
调用task_31_check_change_player_style函数进行验证

参数：style_id，默认'style_001'
"""

import sys
import os
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from verification_functions import task_31_check_change_player_style

def test(*args):
    print("=" * 70)
    print("任务31：更改播放器样式")
    print("=" * 70)
    print("\n📋 人工操作步骤：")
    print("  1. 进入播放器设置")
    print("  2. 选择样式")
    print("\n🔍 开始验证...")

    # 根据函数签名调用验证函数
    if args:
        result = task_31_check_change_player_style(*args)
    else:
        result = task_31_check_change_player_style()

    print("\n" + "=" * 70)
    if result:
        print("✓ 测试通过 - 任务31完成")
        print("=" * 70)
        return True
    else:
        print("✗ 测试失败 - 任务31未完成")
        print("提示：请检查操作步骤是否正确执行")
        print("提示：参数：style_id，默认'style_001'")
        print("=" * 70)
        return False

if __name__ == "__main__":
    # 从命令行获取参数
    args = sys.argv[1:] if len(sys.argv) > 1 else []
    success = test(*args)
    sys.exit(0 if success else 1)
