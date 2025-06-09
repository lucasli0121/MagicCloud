package com.magiccloud.utils

import android.content.Context
import android.view.Gravity
import android.widget.Toast

class AndroidToastUtil(private val context: Context) : ToastUtil {
    override fun showToast(message: String, length: ToastLength) {
        val duration = when (length) {
            ToastLength.SHORT -> Toast.LENGTH_SHORT
            ToastLength.LONG -> Toast.LENGTH_LONG
        }
        val toast = Toast.makeText(context, message, duration)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }
}

actual fun getToastUtil(): ToastUtil = AndroidToastUtil(applicationContext)

private lateinit var applicationContext: Context

fun initToastUtil(context: Context) {
    applicationContext = context.applicationContext
}