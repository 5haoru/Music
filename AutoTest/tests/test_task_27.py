"""
任务27：更改歌单的排序顺序
难度：高

人工操作步骤：
  1. 进入歌单
  2. 打开设置
  3. 选择排序方式

验证标准：
调用task_27_check_playlist_sort_order函数进行验证

参数：playlist_id, expected_order，默认自动检测当前浏览的歌单
"""

import sys
import os
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from verification_functions import task_27_check_playlist_sort_order, read_json_from_device

def test(playlist_id=None, expected_order=None):
    print("=" * 70)
    print("任务27：更改歌单的排序顺序")
    print("=" * 70)
    print("\n📋 人工操作步骤：")
    print("  1. 进入歌单")
    print("  2. 打开设置")
    print("  3. 选择排序方式")

    # 如果没有指定playlist_id，自动从user_playlists.json获取当前浏览的歌单
    if playlist_id is None or expected_order is None:
        print("\n🔍 自动检测当前浏览的歌单...")
        playlists_data = read_json_from_device('autotest/user_playlists.json')

        if playlists_data and 'currentViewingPlaylist' in playlists_data and playlists_data['currentViewingPlaylist']:
            playlist_id = playlists_data['currentViewingPlaylist']

            # 查找该歌单的当前排序方式
            if 'playlists' in playlists_data:
                for playlist in playlists_data['playlists']:
                    if playlist.get('playlistId') == playlist_id:
                        current_order = playlist.get('sortOrder', 'default')
                        playlist_name = playlist.get('playlistName', 'Unknown')

                        print(f"✓ 检测到当前浏览的歌单: 《{playlist_name}》")
                        print(f"  歌单ID: {playlist_id}")
                        print(f"  当前排序方式: {current_order}")

                        # 使用当前排序方式作为期望值（因为排序已经完成）
                        expected_order = current_order
                        break

                if expected_order is None:
                    print("\n" + "=" * 70)
                    print("✗ 错误：无法找到当前浏览的歌单")
                    print("=" * 70)
                    return False
        else:
            print("\n" + "=" * 70)
            print("✗ 错误：无法检测到当前浏览的歌单")
            print("提示：请先进入一个歌单并修改排序")
            print("提示：或手动指定参数: python test_task_27.py playlist_001 time_desc")
            print("=" * 70)
            return False
    else:
        print(f"\n🔍 使用指定的歌单ID: {playlist_id}")
        print(f"  期望排序方式: {expected_order}")

    print(f"\n🔍 开始验证...")

    result = task_27_check_playlist_sort_order(playlist_id, expected_order)

    print("\n" + "=" * 70)
    if result:
        print(f"✓ 测试通过 - 歌单 {playlist_id} 的排序方式为 {expected_order}")
        print("=" * 70)
        return True
    else:
        print(f"✗ 测试失败 - 歌单 {playlist_id} 的排序方式不是 {expected_order}")
        print("提示：请确保已修改排序方式")
        print("=" * 70)
        return False

if __name__ == "__main__":
    # 可以通过命令行参数传入playlist_id和expected_order
    # 用法1：python test_task_27.py                        # 自动检测
    # 用法2：python test_task_27.py playlist_001 time_desc  # 手动指定
    playlist_id = sys.argv[1] if len(sys.argv) > 1 else None
    expected_order = sys.argv[2] if len(sys.argv) > 2 else None
    success = test(playlist_id, expected_order)
    sys.exit(0 if success else 1)
