"""
任务8：收藏当前歌曲
难度：低

人工操作步骤：
1. 进入播放页面，确保有歌曲正在播放或暂停
2. 点击收藏按钮（通常是爱心图标）
3. 看到"成功收藏"提示

验证标准：
检查user_favorites.json中favoriteSongs数组是否包含当前歌曲

参数：
- song_id: 需要传入您收藏的歌曲ID，默认为"song_001"
"""

import sys
import os
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from verification_functions import task_08_check_favorite_song, read_json_from_device

def test(song_id=None):
    print("=" * 70)
    print("任务8：收藏当前歌曲")
    print("=" * 70)
    print("\n📋 人工操作步骤：")
    print("  1. 进入播放页面，确保有歌曲正在播放或暂停")
    print("  2. 点击收藏按钮（通常是爱心图标）")
    print("  3. 看到'成功收藏'提示")

    # 如果没有指定song_id，自动从playback_state.json获取当前播放的歌曲
    if song_id is None:
        print("\n🔍 自动检测当前播放的歌曲...")
        playback_data = read_json_from_device('autotest/playback_state.json')

        if playback_data and 'currentSong' in playback_data and playback_data['currentSong']:
            song_id = playback_data['currentSong'].get('songId')
            song_name = playback_data['currentSong'].get('songName', 'Unknown')
            artist = playback_data['currentSong'].get('artist', 'Unknown')
            print(f"✓ 检测到当前歌曲: 《{song_name}》 - {artist}")
            print(f"  歌曲ID: {song_id}")
        else:
            print("\n" + "=" * 70)
            print("✗ 错误：无法检测到当前播放的歌曲")
            print("提示：请先进入播放页面并确保有歌曲在播放")
            print("提示：或手动指定歌曲ID: python test_task_08.py song_001")
            print("=" * 70)
            return False
    else:
        print(f"\n🔍 使用指定的歌曲ID: {song_id}")

    print(f"\n🔍 开始验证收藏状态...")

    result = task_08_check_favorite_song(song_id)

    print("\n" + "=" * 70)
    if result:
        print(f"✓ 测试通过 - 歌曲 {song_id} 已成功收藏")
        print("=" * 70)
        return True
    else:
        print(f"✗ 测试失败 - 歌曲 {song_id} 未在收藏列表中")
        print("提示：请确保已点击收藏按钮并看到成功提示")
        print("提示：可能需要等待几秒让App保存数据")
        print("=" * 70)
        return False

if __name__ == "__main__":
    # 可以通过命令行参数传入song_id
    # 用法1：python test_task_08.py          # 自动检测当前歌曲
    # 用法2：python test_task_08.py song_002  # 手动指定歌曲ID
    song_id = sys.argv[1] if len(sys.argv) > 1 else None
    success = test(song_id)
    sys.exit(0 if success else 1)
