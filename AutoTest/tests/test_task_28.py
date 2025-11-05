"""
任务28：搜索一个歌手,在歌手主页选择一个专辑并收藏
难度：高

人工操作步骤：
  1. 搜索歌手
  2. 进入歌手页面
  3. 选择专辑
  4. 收藏

验证标准：
调用task_28_check_collect_album函数进行验证

参数：album_id，默认自动检测最新收藏
"""

import sys
import os
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from verification_functions import task_28_check_collect_album, read_json_from_device

def test(album_id=None):
    print("=" * 70)
    print("任务28：搜索一个歌手,在歌手主页选择一个专辑并收藏")
    print("=" * 70)
    print("\n📋 人工操作步骤：")
    print("  1. 搜索歌手")
    print("  2. 进入歌手页面")
    print("  3. 选择专辑")
    print("  4. 收藏")

    # 如果没有指定album_id，自动从collected_items.json获取最新收藏的专辑
    if album_id is None:
        print("\n🔍 自动检测最新收藏的专辑...")
        collected_data = read_json_from_device('autotest/collected_items.json')

        if collected_data and 'collectedAlbums' in collected_data and collected_data['collectedAlbums']:
            # 获取最后一个（最新）收藏的专辑
            latest_album = collected_data['collectedAlbums'][-1]
            album_id = latest_album.get('albumId')
            album_name = latest_album.get('albumName', 'Unknown')
            artist = latest_album.get('artist', 'Unknown')
            collected_time = latest_album.get('collectedTime', 'Unknown')

            print(f"✓ 检测到最新收藏的专辑: 《{album_name}》 - {artist}")
            print(f"  专辑ID: {album_id}")
            print(f"  收藏时间: {collected_time}")
        else:
            print("\n" + "=" * 70)
            print("✗ 错误：无法检测到已收藏的专辑")
            print("提示：请先收藏一个专辑")
            print("提示：或手动指定专辑ID: python test_task_28.py album_001")
            print("=" * 70)
            return False
    else:
        print(f"\n🔍 使用指定的专辑ID: {album_id}")

    print(f"\n🔍 开始验证...")

    result = task_28_check_collect_album(album_id)

    print("\n" + "=" * 70)
    if result:
        print(f"✓ 测试通过 - 专辑 {album_id} 已成功收藏")
        print("=" * 70)
        return True
    else:
        print(f"✗ 测试失败 - 专辑 {album_id} 未在收藏列表中")
        print("提示：请确保已点击收藏按钮")
        print("=" * 70)
        return False

if __name__ == "__main__":
    # 可以通过命令行参数传入album_id
    # 用法1：python test_task_28.py          # 自动检测最新收藏
    # 用法2：python test_task_28.py album_002  # 手动指定专辑ID
    album_id = sys.argv[1] if len(sys.argv) > 1 else None
    success = test(album_id)
    sys.exit(0 if success else 1)
