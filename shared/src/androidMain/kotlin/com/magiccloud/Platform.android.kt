package com.magiccloud

import android.content.Context

class AndroidPlatform : Platform {
    override val name: String = "Android ${android.os.Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

class AndroidPlatformContext(private val context: Context) : PlatfromContext {
    override fun getContext(): Any {
        return context
    }
}