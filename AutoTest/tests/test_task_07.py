"""
任务7：将播放模式改为随机播放
难度：低

人工操作步骤：
1. 进入播放页面
2. 找到播放模式切换按钮（通常在播放控制区域）
3. 多次点击直到切换到随机播放模式（显示随机图标）

验证标准：
检查playback_state.json中playbackMode字段是否为"shuffle"
"""

import sys
import os
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from verification_functions import task_07_check_shuffle_mode

def test():
    print("=" * 70)
    print("任务7：将播放模式改为随机播放")
    print("=" * 70)
    print("\n📋 人工操作步骤：")
    print("  1. 进入播放页面")
    print("  2. 找到播放模式切换按钮（通常在播放控制区域）")
    print("  3. 多次点击直到切换到随机播放模式（显示随机图标）")
    print("\n🔍 开始验证...")

    result = task_07_check_shuffle_mode()

    print("\n" + "=" * 70)
    if result:
        print("✓ 测试通过 - 已切换到随机播放模式")
        print("=" * 70)
        return True
    else:
        print("✗ 测试失败 - 未检测到随机播放模式")
        print("提示：请确保已切换到随机播放（shuffle）模式")
        print("=" * 70)
        return False

if __name__ == "__main__":
    success = test()
    sys.exit(0 if success else 1)
