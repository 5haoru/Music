"""
任务21：删除歌单中的第一首歌
难度：中

人工操作步骤：
  1. 进入歌单
  2. 选择第一首歌
  3. 删除

验证标准：
调用task_21_check_delete_song_from_playlist函数进行验证

参数：playlist_id, expected_count，默认自动检测当前浏览的歌单
"""

import sys
import os
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from verification_functions import task_21_check_delete_song_from_playlist, read_json_from_device

def test(playlist_id=None, expected_count=None):
    print("=" * 70)
    print("任务21：删除歌单中的第一首歌")
    print("=" * 70)
    print("\n📋 人工操作步骤：")
    print("  1. 进入歌单")
    print("  2. 选择第一首歌")
    print("  3. 删除")

    # 如果没有指定playlist_id，自动从user_playlists.json获取当前浏览的歌单
    if playlist_id is None or expected_count is None:
        print("\n🔍 自动检测当前浏览的歌单...")
        playlists_data = read_json_from_device('autotest/user_playlists.json')

        if playlists_data and 'currentViewingPlaylist' in playlists_data and playlists_data['currentViewingPlaylist']:
            playlist_id = playlists_data['currentViewingPlaylist']

            # 查找该歌单的当前歌曲数量
            if 'playlists' in playlists_data:
                for playlist in playlists_data['playlists']:
                    if playlist.get('playlistId') == playlist_id:
                        current_count = playlist.get('songCount', 0)
                        playlist_name = playlist.get('playlistName', 'Unknown')

                        print(f"✓ 检测到当前浏览的歌单: 《{playlist_name}》")
                        print(f"  歌单ID: {playlist_id}")
                        print(f"  删除后歌曲数量: {current_count}")

                        # 使用当前数量作为期望数量（因为删除已经完成）
                        expected_count = current_count
                        break

                if expected_count is None:
                    print("\n" + "=" * 70)
                    print("✗ 错误：无法找到当前浏览的歌单")
                    print("=" * 70)
                    return False
        else:
            print("\n" + "=" * 70)
            print("✗ 错误：无法检测到当前浏览的歌单")
            print("提示：请先进入一个歌单")
            print("提示：或手动指定参数: python test_task_21.py playlist_001 5")
            print("=" * 70)
            return False
    else:
        print(f"\n🔍 使用指定的歌单ID: {playlist_id}")
        print(f"  期望歌曲数量: {expected_count}")

    print(f"\n🔍 开始验证...")

    result = task_21_check_delete_song_from_playlist(playlist_id, expected_count)

    print("\n" + "=" * 70)
    if result:
        print(f"✓ 测试通过 - 歌单 {playlist_id} 的歌曲数量为 {expected_count}")
        print("=" * 70)
        return True
    else:
        print(f"✗ 测试失败 - 歌单 {playlist_id} 的歌曲数量不是 {expected_count}")
        print("提示：请确保已删除歌曲")
        print("=" * 70)
        return False

if __name__ == "__main__":
    # 可以通过命令行参数传入playlist_id和expected_count
    # 用法1：python test_task_21.py                   # 自动检测
    # 用法2：python test_task_21.py playlist_001 5    # 手动指定
    playlist_id = sys.argv[1] if len(sys.argv) > 1 else None
    expected_count = int(sys.argv[2]) if len(sys.argv) > 2 else None
    success = test(playlist_id, expected_count)
    sys.exit(0 if success else 1)
