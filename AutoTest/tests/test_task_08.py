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

from verification_functions import task_08_check_favorite_song

def test(song_id="song_001"):
    print("=" * 70)
    print("任务8：收藏当前歌曲")
    print("=" * 70)
    print("\n📋 人工操作步骤：")
    print("  1. 进入播放页面，确保有歌曲正在播放或暂停")
    print("  2. 点击收藏按钮（通常是爱心图标）")
    print("  3. 看到'成功收藏'提示")
    print(f"\n🔍 开始验证... (检查歌曲ID: {song_id})")

    result = task_08_check_favorite_song(song_id)

    print("\n" + "=" * 70)
    if result:
        print(f"✓ 测试通过 - 歌曲 {song_id} 已成功收藏")
        print("=" * 70)
        return True
    else:
        print(f"✗ 测试失败 - 歌曲 {song_id} 未在收藏列表中")
        print("提示：请确保已点击收藏按钮并看到成功提示")
        print("提示：如果收藏的不是song_001，请传入正确的song_id")
        print("=" * 70)
        return False

if __name__ == "__main__":
    # 可以通过命令行参数传入song_id
    # 用法：python test_task_08.py song_002
    song_id = sys.argv[1] if len(sys.argv) > 1 else "song_001"
    success = test(song_id)
    sys.exit(0 if success else 1)
