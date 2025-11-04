"""
任务1：打开app并进入"推荐"首页
难度：低

人工操作步骤：
1. 在模拟器中启动App
2. 确认进入推荐首页（底部导航栏默认选中"推荐"）

验证标准：
检查app_state.json中currentPage字段是否为"recommend"
"""

import sys
import os
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from verification_functions import task_01_check_open_recommend_page

def test():
    print("=" * 70)
    print("任务1：打开app并进入'推荐'首页")
    print("=" * 70)
    print("\n📋 人工操作步骤：")
    print("  1. 在模拟器中启动App")
    print("  2. 确认进入推荐首页（底部导航栏默认选中'推荐'）")
    print("\n🔍 开始验证...")

    result = task_01_check_open_recommend_page()

    print("\n" + "=" * 70)
    if result:
        print("✓ 测试通过 - 已成功进入推荐首页")
        print("=" * 70)
        return True
    else:
        print("✗ 测试失败 - 未检测到推荐首页状态")
        print("提示：请确保App已启动并显示推荐页面")
        print("=" * 70)
        return False

if __name__ == "__main__":
    success = test()
    sys.exit(0 if success else 1)
