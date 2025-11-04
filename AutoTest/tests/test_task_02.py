"""
任务2：从"推荐"页面进入"漫游"页面
难度：低

人工操作步骤：
1. 确保App已启动在推荐页面
2. 点击底部导航栏的"漫游"按钮（音符图标）
3. 确认页面切换到漫游界面

验证标准：
检查app_state.json中currentPage字段是否为"stroll"
"""

import sys
import os
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from verification_functions import task_02_check_navigate_to_stroll

def test():
    print("=" * 70)
    print("任务2：从'推荐'页面进入'漫游'页面")
    print("=" * 70)
    print("\n📋 人工操作步骤：")
    print("  1. 确保App已启动在推荐页面")
    print("  2. 点击底部导航栏的'漫游'按钮（音符图标）")
    print("  3. 确认页面切换到漫游界面")
    print("\n🔍 开始验证...")

    result = task_02_check_navigate_to_stroll()

    print("\n" + "=" * 70)
    if result:
        print("✓ 测试通过 - 已成功进入漫游页面")
        print("=" * 70)
        return True
    else:
        print("✗ 测试失败 - 未检测到漫游页面状态")
        print("提示：请确保已点击底部的'漫游'按钮")
        print("=" * 70)
        return False

if __name__ == "__main__":
    success = test()
    sys.exit(0 if success else 1)
