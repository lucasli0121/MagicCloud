package com.magiccloud

import androidx.compose.runtime.compositionLocalOf

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

interface PlatfromContext {
    fun getContext(): Any
}

val LocalPlatformContextProvider = compositionLocalOf<PlatfromContext> {
    error("No PlatfromContext provided")
}