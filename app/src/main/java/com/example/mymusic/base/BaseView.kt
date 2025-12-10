package com.example.mymusic.base

/**
 * 所有View层接口的基类
 */
interface BaseView {
    fun showLoading()
    fun hideLoading()
    fun showError(message: String)
    fun showSuccess(message: String)
}