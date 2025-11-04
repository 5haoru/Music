"""
任务27：更改歌单的排序顺序
难度：高

人工操作步骤：
  1. 进入歌单
  2. 打开设置
  3. 选择排序方式

验证标准：
调用task_27_check_playlist_sort_order函数进行验证

参数：playlist_id, expected_order（如'time_desc'）
"""

import sys
import os
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from verification_functions import task_27_check_playlist_sort_order

def test(*args):
    print("=" * 70)
    print("任务27：更改歌单的排序顺序")
    print("=" * 70)
    print("\n📋 人工操作步骤：")
    print("  1. 进入歌单")
    print("  2. 打开设置")
    print("  3. 选择排序方式")
    print("\n🔍 开始验证...")

    # 根据函数签名调用验证函数
    if args:
        result = task_27_check_playlist_sort_order(*args)
    else:
        result = task_27_check_playlist_sort_order()

    print("\n" + "=" * 70)
    if result:
        print("✓ 测试通过 - 任务27完成")
        print("=" * 70)
        return True
    else:
        print("✗ 测试失败 - 任务27未完成")
        print("提示：请检查操作步骤是否正确执行")
        print("提示：参数：playlist_id, expected_order（如'time_desc'）")
        print("=" * 70)
        return False

if __name__ == "__main__":
    # 从命令行获取参数
    args = sys.argv[1:] if len(sys.argv) > 1 else []
    success = test(*args)
    sys.exit(0 if success else 1)
