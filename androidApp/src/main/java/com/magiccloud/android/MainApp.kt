package com.magiccloud.android

import android.app.Activity
import android.app.Application
import android.content.Context
import com.magiccloud.AndroidPlatformContext
import com.magiccloud.ble.PlatformBluetoothManager
import com.magiccloud.di.appModule
import com.magiccloud.di.viewModelModule
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.compose.KoinContext
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.scopedOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.parameter.parametersOf
import org.koin.dsl.koinApplication
import org.koin.dsl.module

class MainApp: Application() {
    override fun onCreate() {
        super.onCreate()
        // 启动Napier日志
        Napier.base(DebugAntilog())
        startKoin {
            androidLogger()
            androidContext(this@MainApp)
            modules(appModule, viewModelModule)
        }
    }
}