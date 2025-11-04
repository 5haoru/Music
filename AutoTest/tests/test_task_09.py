"""
任务9：调节当前音乐播放的音量
难度：低

人工操作步骤：
  1. 进入播放页面
  2. 调整音量滑块或按钮

验证标准：
调用task_09_check_volume_adjusted函数进行验证

可选参数：expected_volume（期望的音量值0-100）
"""

import sys
import os
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from verification_functions import task_09_check_volume_adjusted

def test(*args):
    print("=" * 70)
    print("任务9：调节当前音乐播放的音量")
    print("=" * 70)
    print("\n📋 人工操作步骤：")
    print("  1. 进入播放页面")
    print("  2. 调整音量滑块或按钮")
    print("\n🔍 开始验证...")

    # 根据函数签名调用验证函数
    if args:
        result = task_09_check_volume_adjusted(*args)
    else:
        result = task_09_check_volume_adjusted()

    print("\n" + "=" * 70)
    if result:
        print("✓ 测试通过 - 任务9完成")
        print("=" * 70)
        return True
    else:
        print("✗ 测试失败 - 任务9未完成")
        print("提示：请检查操作步骤是否正确执行")
        print("提示：可选参数：expected_volume（期望的音量值0-100）")
        print("=" * 70)
        return False

if __name__ == "__main__":
    # 从命令行获取参数
    args = sys.argv[1:] if len(sys.argv) > 1 else []
    success = test(*args)
    sys.exit(0 if success else 1)
