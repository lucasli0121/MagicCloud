package com.magiccloud.utils

/**
 * 文件名：ToastUtil
 * 描述：定义toast 提示
 * 作者：liguoqiang
 * 日期：2025/3/27
 */

interface ToastUtil {
    fun showToast(message: String, length: ToastLength = ToastLength.SHORT)
}

enum class ToastLength {
    SHORT,
    LONG
}

expect fun getToastUtil(): ToastUtil