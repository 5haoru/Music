"""
任务19：在排行榜中打开一个榜单并播放第一首歌曲
难度：中
"""

import logging
import sys
from .verification_functions import read_json_from_device

# 根据 playlists.json 定义所有属于“排行榜”性质的歌单名称
RANKING_PLAYLIST_NAMES = [
    "热歌榜", "新歌榜", "国风榜", "ACG榜", "日语榜", 
    "飙升榜", "硬地原创音乐榜", "潮流风向榜"
]

def check_play_from_rank_list(result=None, device_id=None, backup_dir=None):
    """
    任务19: 验证是否播放了排行榜的歌曲
    - 读取 play_records.json 获取最新的播放记录
    - 读取 playlists.json 获取所有排行榜歌单的歌曲ID集合
    - 验证最新播放的歌曲ID是否存在于排行榜歌单的歌曲ID集合中
    """
    # 1. 从设备获取最新的播放记录
    play_records = read_json_from_device("data/play_records.json", device_id, result, backup_dir)
    if not play_records or not isinstance(play_records, list) or len(play_records) == 0:
        logging.error("✗ 测试失败 - 任务19未完成：无法从设备读取播放记录(play_records.json)或记录为空")
        return False

    # 获取最后一条播放记录，即最新的一次播放
    latest_play = play_records[-1]
    played_song_id = latest_play.get("songId")
    if not played_song_id:
        logging.error(f"✗ 测试失败 - 任务19未完成：最新的播放记录中没有找到'songId': {latest_play}")
        return False
    logging.info(f"  → 最新播放的歌曲ID是: '{played_song_id}'")

    # 2. 从设备获取所有排行榜歌单中的歌曲ID
    playlists_data = read_json_from_device("data/playlists.json", device_id, result, backup_dir)
    if not playlists_data:
        logging.error("✗ 测试失败 - 任务19未完成：无法从设备读取歌单数据(playlists.json)")
        return False

    rank_list_song_ids = set()
    for playlist in playlists_data:
        if playlist.get("playlistName") in RANKING_PLAYLIST_NAMES:
            if playlist.get("songIds") and isinstance(playlist["songIds"], list):
                rank_list_song_ids.update(playlist["songIds"])

    if not rank_list_song_ids:
        logging.error("✗ 测试失败 - 任务19未完成：在设备数据中未找到任何已定义的排行榜歌单或歌单均为空")
        return False
    logging.info(f"  → 已从设备加载 {len(rank_list_song_ids)} 首排行榜歌曲ID用于验证")

    # 3. 检查播放的歌曲ID是否存在于排行榜歌曲ID集合中
    if played_song_id in rank_list_song_ids:
        logging.info(f"✓ 测试通过 - 任务19完成：播放的歌曲'{played_song_id}'确实存在于排行榜歌单中。")
        return True
    else:
        logging.error(f"✗ 测试失败 - 任务19未完成：播放的歌曲'{played_song_id}'不属于任何一个已定义的排行榜歌单。")
        return False

if __name__ == "__main__":
    logging.basicConfig(level=logging.INFO, format='%(message)s')
    print("=" * 70)
    print("任务19：在排行榜中打开一个榜单并播放第一首歌曲")
    print("=" * 70)
    print("\n📋 人工操作步骤：")
    print("  1. 打开音乐APP，进入'排行榜'页面")
    print("  2. 选择任意一个榜单")
    print("  3. 点击第一首歌曲进行播放")
    print("\n🔍 开始验证...")

    # 在独立运行时，check函数的参数将默认为None
    # 这将导致read_json_from_device因缺少必要参数而按预期失败
    success = check_play_from_rank_list()

    print(f"\n任务19验证结果: {'成功' if success else '失败'}")
    sys.exit(0 if success else 1)