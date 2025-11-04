package com.example.mymusic

import android.app.Application
import android.util.Log
import com.example.mymusic.utils.AutoTestHelper

/**
 * App的Application类
 * 用于全局初始化
 */
class MyMusicApplication : Application() {

    companion object {
        private const val TAG = "MyMusicApplication"
    }

    override fun onCreate() {
        super.onCreate()

        Log.d(TAG, "应用启动,开始初始化")

        // 初始化自动化测试辅助工具
        try {
            AutoTestHelper.init(this)
            Log.d(TAG, "AutoTestHelper初始化成功")
        } catch (e: Exception) {
            Log.e(TAG, "AutoTestHelper初始化失败", e)
        }
    }
}
