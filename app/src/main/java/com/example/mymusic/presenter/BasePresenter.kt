package com.example.mymusic.presenter

/**
 * Presenter基类接口
 */
interface BasePresenter {
    fun onDestroy()
}

/**
 * View基类接口
 */
interface BaseView {
    fun showLoading()
    fun hideLoading()
    fun showError(message: String)
}
