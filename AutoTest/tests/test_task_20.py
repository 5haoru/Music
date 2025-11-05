"""
任务20：在推荐歌单中随机选择一个歌单并收藏
难度：中

人工操作步骤：
  1. 进入推荐
  2. 找到推荐歌单
  3. 点击收藏

验证标准：
调用task_20_check_collect_playlist函数进行验证

参数：playlist_id（歌单ID），默认自动检测最新收藏
"""

import sys
import os
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from verification_functions import task_20_check_collect_playlist, read_json_from_device

def test(playlist_id=None):
    print("=" * 70)
    print("任务20：在推荐歌单中随机选择一个歌单并收藏")
    print("=" * 70)
    print("\n📋 人工操作步骤：")
    print("  1. 进入推荐")
    print("  2. 找到推荐歌单")
    print("  3. 点击收藏")

    # 如果没有指定playlist_id，自动从collected_items.json获取最新收藏的歌单
    if playlist_id is None:
        print("\n🔍 自动检测最新收藏的歌单...")
        collected_data = read_json_from_device('autotest/collected_items.json')

        if collected_data and 'collectedPlaylists' in collected_data and collected_data['collectedPlaylists']:
            # 获取最后一个（最新）收藏的歌单
            latest_playlist = collected_data['collectedPlaylists'][-1]
            playlist_id = latest_playlist.get('playlistId')
            playlist_name = latest_playlist.get('playlistName', 'Unknown')
            collected_time = latest_playlist.get('collectedTime', 'Unknown')

            print(f"✓ 检测到最新收藏的歌单: 《{playlist_name}》")
            print(f"  歌单ID: {playlist_id}")
            print(f"  收藏时间: {collected_time}")
        else:
            print("\n" + "=" * 70)
            print("✗ 错误：无法检测到已收藏的歌单")
            print("提示：请先收藏一个歌单")
            print("提示：或手动指定歌单ID: python test_task_20.py playlist_001")
            print("=" * 70)
            return False
    else:
        print(f"\n🔍 使用指定的歌单ID: {playlist_id}")

    print(f"\n🔍 开始验证...")

    result = task_20_check_collect_playlist(playlist_id)

    print("\n" + "=" * 70)
    if result:
        print(f"✓ 测试通过 - 歌单 {playlist_id} 已成功收藏")
        print("=" * 70)
        return True
    else:
        print(f"✗ 测试失败 - 歌单 {playlist_id} 未在收藏列表中")
        print("提示：请确保已点击收藏按钮")
        print("=" * 70)
        return False

if __name__ == "__main__":
    # 可以通过命令行参数传入playlist_id
    # 用法1：python test_task_20.py          # 自动检测最新收藏
    # 用法2：python test_task_20.py playlist_002  # 手动指定歌单ID
    playlist_id = sys.argv[1] if len(sys.argv) > 1 else None
    success = test(playlist_id)
    sys.exit(0 if success else 1)
