"""
任务25：进入个人听歌时长页面，告诉我本周听歌时长是多少
难度：中
类型：信息检索类
"""

import logging
import sys
import re
from .verification_functions import read_json_from_device

def check_weekly_listening_duration_is_reported(result=None, device_id=None, backup_dir=None):
    """
    任务25: 验证AI是否返回了正确的本周听歌时长
    - 从AI的 final_message 中提取所有数字
    - 从 duration_data.json 读取真实的 weeklyData.totalDuration
    - 从真实数据中提取所有数字
    - 对比两组数字是否完全一致
    """
    if not result or "final_message" not in result:
        logging.error("✗ 测试失败 - 任务25未完成：AI未提供final_message")
        return False

    final_msg = result["final_message"]
    if not final_msg or not isinstance(final_msg, str):
        logging.error(f"✗ 测试失败 - 任务25未完成：final_message格式错误: {final_msg}")
        return False

    # 1. 从设备读取真实的听歌时长数据
    duration_data = read_json_from_device("data/duration_data.json", device_id, result, backup_dir)
    if not duration_data:
        logging.error("✗ 测试失败 - 任务25未完成：无法从设备读取 duration_data.json")
        return False

    # 2. 提取真实的听歌时长数值
    try:
        true_duration_str = duration_data["weeklyData"]["totalDuration"]
        # 使用正则表达式从 "24小时29分" 中提取 ['24', '29']
        true_numbers = re.findall(r'\d+', true_duration_str)
        if not true_numbers:
            logging.error(f"✗ 测试失败 - 任务25未完成：在设备数据中未能提取到时长数值: '{true_duration_str}'")
            return False
        logging.info(f"  → 设备中的真实时长是: '{true_duration_str}', 提取数值: {true_numbers}")
    except (KeyError, TypeError) as e:
        logging.error(f"✗ 测试失败 - 任务25未完成：duration_data.json 格式不正确，缺少 weeklyData.totalDuration 字段。错误: {e}")
        return False

    # 3. 从AI的回答中提取所有数字
    ai_numbers = re.findall(r'\d+', final_msg)
    logging.info(f"  → AI的回答是: '{final_msg}', 提取数值: {ai_numbers}")

    # 4. 比较两组数字是否一致 (排序后比较，忽略顺序)
    if sorted(ai_numbers) == sorted(true_numbers):
        logging.info(f"✓ 测试通过 - 任务25完成：AI返回了正确的时长数值。")
        return True
    else:
        logging.error(f"✗ 测试失败 - 任务25未完成：AI返回了错误的数值。期望: {sorted(true_numbers)}, 实际: {sorted(ai_numbers)}")
        return False

if __name__ == "__main__":
    logging.basicConfig(level=logging.INFO, format='%(message)s')
    print("=" * 70)
    print("任务25：进入个人听歌时长页面，告诉我本周听歌时长是多少")
    print("=" * 70)
    print("\n📋 人工操作步骤：")
    print("  1. 打开音乐APP，进入'我的'页面")
    print("  2. 找到并点击'听歌时长'或'统计'入口")
    print("  3. 查看本周听歌时长并告知用户")
    print("\n🔍 开始验证...")

    # 在独立运行时，check函数的参数将默认为None
    # 这将导致read_json_from_device因缺少必要参数而按预期失败
    success = check_weekly_listening_duration_is_reported()

    print(f"\n任务25验证结果: {'成功' if success else '失败'}")
    sys.exit(0 if success else 1)