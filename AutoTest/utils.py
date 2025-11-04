"""
自动化测试工具函数
提供设备检查、文件读取等实用功能
"""

import subprocess
import json
import os

APP_PACKAGE = "com.example.mymusic"


def check_adb_installed():
    """检查adb是否已安装"""
    try:
        result = subprocess.run(['adb', 'version'],
                              capture_output=True,
                              text=True,
                              timeout=5)
        if result.returncode == 0:
            print("✓ adb已安装")
            print(f"  版本: {result.stdout.split()[4]}")
            return True
        else:
            print("✗ adb未正确安装")
            return False
    except FileNotFoundError:
        print("✗ 找不到adb命令")
        print("  请安装Android SDK Platform Tools并添加到PATH")
        return False
    except Exception as e:
        print(f"✗ 检查adb时出错: {e}")
        return False


def check_device_connected():
    """检查设备是否连接"""
    try:
        result = subprocess.run(['adb', 'devices'],
                              capture_output=True,
                              text=True,
                              timeout=5)
        lines = result.stdout.strip().split('\n')[1:]  # 跳过第一行标题
        devices = [line for line in lines if line.strip() and 'device' in line]

        if devices:
            print(f"✓ 发现 {len(devices)} 个设备:")
            for device in devices:
                print(f"  - {device}")
            return True
        else:
            print("✗ 没有检测到设备")
            print("  请确保:")
            print("  1. 手机已通过USB连接到电脑")
            print("  2. 手机已开启USB调试")
            print("  3. 已授权电脑进行USB调试")
            return False
    except Exception as e:
        print(f"✗ 检查设备时出错: {e}")
        return False


def check_app_installed():
    """检查App是否已安装"""
    try:
        result = subprocess.run(['adb', 'shell', 'pm', 'list', 'packages', APP_PACKAGE],
                              capture_output=True,
                              text=True,
                              timeout=10)
        if APP_PACKAGE in result.stdout:
            print(f"✓ App已安装: {APP_PACKAGE}")
            return True
        else:
            print(f"✗ App未安装: {APP_PACKAGE}")
            print("  请先安装App到设备上")
            return False
    except Exception as e:
        print(f"✗ 检查App时出错: {e}")
        return False


def check_app_debuggable():
    """检查App是否可调试"""
    try:
        result = subprocess.run(['adb', 'shell', 'run-as', APP_PACKAGE, 'echo', 'test'],
                              capture_output=True,
                              text=True,
                              timeout=10)
        if result.returncode == 0 and 'test' in result.stdout:
            print(f"✓ App可调试")
            return True
        else:
            print(f"✗ App不可调试")
            print("  请确保使用debug版本的App")
            print("  AndroidManifest.xml中应有: android:debuggable=\"true\"")
            return False
    except Exception as e:
        print(f"✗ 检查App调试状态时出错: {e}")
        return False


def list_autotest_files():
    """列出所有自动化测试相关的JSON文件"""
    try:
        result = subprocess.run(['adb', 'shell', 'run-as', APP_PACKAGE, 'ls', 'files/autotest/'],
                              capture_output=True,
                              text=True,
                              timeout=10)
        if result.returncode == 0:
            files = result.stdout.strip().split('\n')
            print("✓ 自动化测试文件列表:")
            for file in files:
                if file.strip():
                    print(f"  - {file}")
            return files
        else:
            print("✗ 无法列出自动化测试文件")
            print("  请确保App已创建autotest目录")
            return []
    except Exception as e:
        print(f"✗ 列出文件时出错: {e}")
        return []


def view_json_file(filename):
    """查看指定JSON文件的内容"""
    try:
        local_file = f"temp_{filename}"
        result = subprocess.run(['adb', 'exec-out', 'run-as', APP_PACKAGE,
                               'cat', f'files/autotest/{filename}'],
                              stdout=open(local_file, 'w'),
                              stderr=subprocess.PIPE,
                              timeout=10)

        if os.path.exists(local_file) and os.path.getsize(local_file) > 0:
            with open(local_file, 'r', encoding='utf-8') as f:
                data = json.load(f)

            print(f"✓ 文件内容: {filename}")
            print(json.dumps(data, ensure_ascii=False, indent=2))

            os.remove(local_file)
            return data
        else:
            print(f"✗ 无法读取文件: {filename}")
            if os.path.exists(local_file):
                os.remove(local_file)
            return None
    except json.JSONDecodeError:
        print(f"✗ JSON格式错误: {filename}")
        if os.path.exists(local_file):
            with open(local_file, 'r', encoding='utf-8') as f:
                print(f"文件内容: {f.read()}")
            os.remove(local_file)
        return None
    except Exception as e:
        print(f"✗ 查看文件时出错: {e}")
        if os.path.exists(local_file):
            os.remove(local_file)
        return None


def run_system_check():
    """运行完整的系统检查"""
    print("=" * 70)
    print("自动化测试环境检查")
    print("=" * 70)
    print()

    checks = [
        ("adb安装", check_adb_installed),
        ("设备连接", check_device_connected),
        ("App安装", check_app_installed),
        ("App可调试", check_app_debuggable),
    ]

    results = []
    for name, check_func in checks:
        print(f"[检查] {name}")
        result = check_func()
        results.append((name, result))
        print()

    # 如果前面的检查都通过,列出测试文件
    if all(r[1] for r in results):
        print("[检查] 自动化测试文件")
        list_autotest_files()
        print()

    # 总结
    print("=" * 70)
    passed = sum(1 for _, result in results if result)
    print(f"检查结果: {passed}/{len(results)} 项通过")

    if passed == len(results):
        print("✓ 环境配置正确,可以开始测试")
        return True
    else:
        print("✗ 环境配置有问题,请解决上述问题后重试")
        return False


def clear_autotest_files():
    """清空所有自动化测试文件"""
    try:
        print("警告: 此操作将删除所有自动化测试JSON文件")
        confirm = input("是否继续? (y/N): ")

        if confirm.lower() != 'y':
            print("操作已取消")
            return False

        files = [
            'app_state.json',
            'playback_state.json',
            'user_favorites.json',
            'user_playlists.json',
            'collected_items.json',
            'followed_artists.json',
            'search_history.json',
            'comments.json',
            'player_settings.json',
            'listening_stats.json',
            'mv_playback.json',
            'task_logs.json'
        ]

        for file in files:
            subprocess.run(['adb', 'shell', 'run-as', APP_PACKAGE,
                          'rm', f'files/autotest/{file}'],
                         capture_output=True,
                         timeout=5)

        print("✓ 已清空所有自动化测试文件")
        return True
    except Exception as e:
        print(f"✗ 清空文件时出错: {e}")
        return False


if __name__ == "__main__":
    import sys

    if len(sys.argv) > 1:
        command = sys.argv[1]

        if command == "check":
            run_system_check()
        elif command == "list":
            list_autotest_files()
        elif command == "view":
            if len(sys.argv) > 2:
                view_json_file(sys.argv[2])
            else:
                print("用法: python utils.py view <filename>")
        elif command == "clear":
            clear_autotest_files()
        else:
            print("未知命令")
            print("用法:")
            print("  python utils.py check       - 运行环境检查")
            print("  python utils.py list        - 列出所有测试文件")
            print("  python utils.py view <file> - 查看指定文件内容")
            print("  python utils.py clear       - 清空所有测试文件")
    else:
        # 默认运行系统检查
        run_system_check()
