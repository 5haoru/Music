"""
任务21：删除歌单中的第一首歌
难度：中

人工操作步骤：
  1. 进入歌单
  2. 选择第一首歌
  3. 删除

验证标准：
调用task_21_check_delete_song_from_playlist函数进行验证

参数：playlist_id, expected_count（删除后的歌曲数量）
"""

import sys
import os
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from verification_functions import task_21_check_delete_song_from_playlist

def test(*args):
    print("=" * 70)
    print("任务21：删除歌单中的第一首歌")
    print("=" * 70)
    print("\n📋 人工操作步骤：")
    print("  1. 进入歌单")
    print("  2. 选择第一首歌")
    print("  3. 删除")
    print("\n🔍 开始验证...")

    # 根据函数签名调用验证函数
    if args:
        result = task_21_check_delete_song_from_playlist(*args)
    else:
        result = task_21_check_delete_song_from_playlist()

    print("\n" + "=" * 70)
    if result:
        print("✓ 测试通过 - 任务21完成")
        print("=" * 70)
        return True
    else:
        print("✗ 测试失败 - 任务21未完成")
        print("提示：请检查操作步骤是否正确执行")
        print("提示：参数：playlist_id, expected_count（删除后的歌曲数量）")
        print("=" * 70)
        return False

if __name__ == "__main__":
    # 从命令行获取参数
    args = sys.argv[1:] if len(sys.argv) > 1 else []
    success = test(*args)
    sys.exit(0 if success else 1)
