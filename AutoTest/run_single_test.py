"""
运行单个或多个测试任务的工具脚本

用法：
  python run_single_test.py 1              # 运行任务1
  python run_single_test.py 1 4 7 8       # 运行任务1, 4, 7, 8
  python run_single_test.py 1-10          # 运行任务1到10
  python run_single_test.py all           # 运行所有任务
"""

import sys
import os
import importlib

def run_test(task_num):
    """运行指定编号的测试"""
    try:
        module_name = f"tests.test_task_{task_num:02d}"
        module = importlib.import_module(module_name)

        if hasattr(module, 'test'):
            result = module.test()
            return result
        else:
            print(f"✗ 任务{task_num}的测试文件中没有test()函数")
            return False
    except ModuleNotFoundError:
        print(f"✗ 找不到任务{task_num}的测试文件: test_task_{task_num:02d}.py")
        return False
    except Exception as e:
        print(f"✗ 运行任务{task_num}时出错: {e}")
        return False

def parse_task_numbers(args):
    """解析命令行参数，返回要测试的任务编号列表"""
    if not args or args[0] == 'all':
        # 运行所有任务（1-31）
        return list(range(1, 32))

    task_nums = []
    for arg in args:
        if '-' in arg:
            # 范围格式：1-10
            start, end = arg.split('-')
            task_nums.extend(range(int(start), int(end) + 1))
        else:
            # 单个编号
            task_nums.append(int(arg))

    return sorted(set(task_nums))  # 去重并排序

def main():
    if len(sys.argv) < 2:
        print("用法：")
        print("  python run_single_test.py 1              # 运行任务1")
        print("  python run_single_test.py 1 4 7 8       # 运行任务1, 4, 7, 8")
        print("  python run_single_test.py 1-10          # 运行任务1到10")
        print("  python run_single_test.py all           # 运行所有任务")
        sys.exit(1)

    task_nums = parse_task_numbers(sys.argv[1:])

    print("\n" + "=" * 70)
    print(f"准备运行 {len(task_nums)} 个测试任务")
    print("=" * 70 + "\n")

    results = {}
    for task_num in task_nums:
        print(f"\n{'='*70}")
        print(f"开始测试任务 {task_num}")
        print(f"{'='*70}\n")

        result = run_test(task_num)
        results[task_num] = result

        print()

    # 打印总结
    print("\n" + "=" * 70)
    print("测试总结")
    print("=" * 70)

    passed = sum(1 for r in results.values() if r)
    failed = len(results) - passed

    print(f"总计: {len(results)} 个任务")
    print(f"通过: {passed} ({passed/len(results)*100:.1f}%)")
    print(f"失败: {failed} ({failed/len(results)*100:.1f}%)")
    print()

    # 列出失败的任务
    if failed > 0:
        print("失败的任务:")
        for task_num, result in results.items():
            if not result:
                print(f"  ✗ 任务{task_num}")

    print("=" * 70)

    # 如果有失败的任务，退出码为1
    sys.exit(0 if failed == 0 else 1)

if __name__ == "__main__":
    main()
