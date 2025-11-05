"""
任务24：在歌单中选择一首歌曲并发表评论
难度：高

人工操作步骤：
  1. 进入歌曲
  2. 点击评论区
  3. 输入评论
  4. 发表

验证标准：
调用task_24_check_post_comment函数进行验证

参数：song_id, comment_content（可选），默认自动检测最新评论
"""

import sys
import os
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from verification_functions import task_24_check_post_comment, read_json_from_device

def test(song_id=None, comment_content=None):
    print("=" * 70)
    print("任务24：在歌单中选择一首歌曲并发表评论")
    print("=" * 70)
    print("\n📋 人工操作步骤：")
    print("  1. 进入歌曲")
    print("  2. 点击评论区")
    print("  3. 输入评论")
    print("  4. 发表")

    # 如果没有指定song_id，自动从comments.json获取最新发表的评论
    if song_id is None:
        print("\n🔍 自动检测最新发表的评论...")
        comments_data = read_json_from_device('autotest/comments.json')

        if comments_data and 'userComments' in comments_data and comments_data['userComments']:
            # 获取最后一个（最新）评论
            latest_comment = comments_data['userComments'][-1]
            song_id = latest_comment.get('songId')
            comment_content_detected = latest_comment.get('content', '')
            comment_time = latest_comment.get('commentTime', 'Unknown')

            print(f"✓ 检测到最新评论")
            print(f"  歌曲ID: {song_id}")
            print(f"  评论内容: {comment_content_detected[:50]}..." if len(comment_content_detected) > 50 else f"  评论内容: {comment_content_detected}")
            print(f"  发表时间: {comment_time}")
        else:
            print("\n" + "=" * 70)
            print("✗ 错误：无法检测到已发表的评论")
            print("提示：请先发表一条评论")
            print("提示：或手动指定歌曲ID: python test_task_24.py song_001")
            print("=" * 70)
            return False
    else:
        print(f"\n🔍 使用指定的歌曲ID: {song_id}")
        if comment_content:
            print(f"  评论内容: {comment_content}")

    print(f"\n🔍 开始验证...")

    result = task_24_check_post_comment(song_id, comment_content)

    print("\n" + "=" * 70)
    if result:
        print(f"✓ 测试通过 - 已成功为歌曲 {song_id} 发表评论")
        print("=" * 70)
        return True
    else:
        print(f"✗ 测试失败 - 歌曲 {song_id} 未找到评论")
        print("提示：请确保已发表评论")
        print("=" * 70)
        return False

if __name__ == "__main__":
    # 可以通过命令行参数传入song_id和comment_content
    # 用法1：python test_task_24.py                    # 自动检测最新评论
    # 用法2：python test_task_24.py song_002           # 手动指定歌曲ID
    # 用法3：python test_task_24.py song_002 "很棒"    # 手动指定歌曲ID和内容
    song_id = sys.argv[1] if len(sys.argv) > 1 else None
    comment_content = sys.argv[2] if len(sys.argv) > 2 else None
    success = test(song_id, comment_content)
    sys.exit(0 if success else 1)
