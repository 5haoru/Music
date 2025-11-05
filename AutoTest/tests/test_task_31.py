"""
任务31：更改播放器样式
难度：高

人工操作步骤：
  1. 进入播放器设置
  2. 选择样式

验证标准：
调用task_31_check_change_player_style函数进行验证

参数：style_id，默认自动检测
"""

import sys
import os
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from verification_functions import task_31_check_change_player_style, read_json_from_device

def test(style_id=None):
    print("=" * 70)
    print("任务31：更改播放器样式")
    print("=" * 70)
    print("\n📋 人工操作步骤：")
    print("  1. 进入播放器设置")
    print("  2. 选择样式")

    # 如果没有指定style_id，自动从player_settings.json获取当前播放器样式
    if style_id is None:
        print("\n🔍 自动检测当前播放器样式...")
        settings_data = read_json_from_device('autotest/player_settings.json')

        if settings_data and 'playerStyle' in settings_data and settings_data['playerStyle']:
            style_id = settings_data['playerStyle'].get('styleId')
            style_name = settings_data['playerStyle'].get('styleName', 'Unknown')

            print(f"✓ 检测到当前播放器样式: {style_name}")
            print(f"  样式ID: {style_id}")
        else:
            print("\n" + "=" * 70)
            print("✗ 错误：无法检测到当前播放器样式")
            print("提示：请先进入播放器设置并选择一个样式")
            print("提示：或手动指定样式ID: python test_task_31.py style_001")
            print("=" * 70)
            return False
    else:
        print(f"\n🔍 使用指定的样式ID: {style_id}")

    print(f"\n🔍 开始验证...")

    result = task_31_check_change_player_style(style_id)

    print("\n" + "=" * 70)
    if result:
        print(f"✓ 测试通过 - 播放器样式已设置为 {style_id}")
        print("=" * 70)
        return True
    else:
        print(f"✗ 测试失败 - 播放器样式未设置为 {style_id}")
        print("提示：请确保已在播放器设置中选择了样式")
        print("=" * 70)
        return False

if __name__ == "__main__":
    # 可以通过命令行参数传入style_id
    # 用法1：python test_task_31.py          # 自动检测当前样式
    # 用法2：python test_task_31.py style_002  # 手动指定样式ID
    style_id = sys.argv[1] if len(sys.argv) > 1 else None
    success = test(style_id)
    sys.exit(0 if success else 1)
