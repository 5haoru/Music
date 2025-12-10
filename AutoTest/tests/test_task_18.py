"""
任务18：进入排行榜的新歌榜，告诉我第一首歌叫什么
难度：中
类型：信息检索类
"""

import logging
import sys
from .verification_functions import read_json_from_device

RANK_NAME_NEW_SONGS = "新歌榜"

def check_first_song_name_is_reported(result=None, device_id=None, backup_dir=None):
    """
    任务18: 验证AI是否返回了新歌榜第一首歌的名称
    - 从AI的 final_message 中提取歌曲名称
    - 从 playlists.json 和 songs.json 读取新歌榜的真实第一首歌名
    - 对比两者是否一致
    """
    if not result or "final_message" not in result:
        logging.error("✗ 测试失败 - 任务18未完成：AI未提供final_message")
        return False

    final_msg = result["final_message"]
    if not final_msg or not isinstance(final_msg, str):
        logging.error(f"✗ 测试失败 - 任务18未完成：final_message格式错误: {final_msg}")
        return False

    # 1. 从设备读取真实的歌单和歌曲数据
    playlists_data = read_json_from_device("data/playlists.json", device_id, result, backup_dir)
    songs_data = read_json_from_device("data/songs.json", device_id, result, backup_dir)
    
    if not playlists_data:
        logging.error("✗ 测试失败 - 任务18未完成：无法从设备读取 playlists.json")
        return False
    if not songs_data:
        logging.error("✗ 测试失败 - 任务18未完成：无法从设备读取 songs.json")
        return False

    # 2. 查找新歌榜，并获取第一首歌的ID
    first_song_id = None
    for playlist in playlists_data:
        if playlist.get("playlistName") == RANK_NAME_NEW_SONGS:
            if playlist.get("songIds") and len(playlist["songIds"]) > 0:
                first_song_id = playlist["songIds"][0]
                break
    
    if not first_song_id:
        logging.error(f"✗ 测试失败 - 任务18未完成：在设备数据中未找到'{RANK_NAME_NEW_SONGS}'或该榜单为空")
        return False

    # 3. 根据歌曲ID查找歌曲名称
    first_song_name = None
    for song in songs_data:
        if song.get("songId") == first_song_id:
            first_song_name = song.get("songName")
            break

    if not first_song_name:
        logging.error(f"✗ 测试失败 - 任务18未完成：找到了 songId '{first_song_id}' 但无法在 songs.json 中匹配到歌曲")
        return False
    logging.info(f"  → 设备中'{RANK_NAME_NEW_SONGS}'的第一首歌是: '{first_song_name}'")

    # 4. 检查AI的回答
    if first_song_name in final_msg:
        logging.info(f"✓ 测试通过 - 任务18完成：AI正确返回了第一首歌的名称: '{final_msg}'")
        return True
    else:
        logging.error(f"✗ 测试失败 - 任务18未完成：AI返回错误。期望包含'{first_song_name}', 实际返回: '{final_msg}'")
        return False

if __name__ == "__main__":
    logging.basicConfig(level=logging.INFO, format='%(message)s')
    print("=" * 70)
    print("任务18：进入排行榜的新歌榜，告诉我第一首歌叫什么")
    print("=" * 70)
    print("\n📋 人工操作步骤：")
    print("  1. 打开音乐APP，进入'排行榜'页面")
    print("  2. 找到并点击'新歌榜'")
    print("  3. 查看第一首歌的名称并返回")
    print("\n🔍 开始验证...")

    # 在独立运行时，check函数的参数将默认为None
    # 这意味着它会因为缺少result而失败，这是预期的行为
    success = check_first_song_name_is_reported()

    print(f"\n任务18验证结果: {'成功' if success else '失败'}")
    sys.exit(0 if success else 1)
