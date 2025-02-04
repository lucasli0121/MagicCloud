package com.magiccloud.di

import android.app.Activity
import com.magiccloud.AndroidPlatformContext
import com.magiccloud.ble.PlatformBluetoothManager
import com.magiccloud.viewmodel.BleScanViewModel
import com.magiccloud.viewmodel.BleViewModel
import org.koin.core.module.dsl.scopedOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::BleScanViewModel)
    viewModelOf(::BleViewModel)
}

val appModule = module {
    scope<Activity> {
        scopedOf(::AndroidPlatformContext)
    }
    singleOf(::PlatformBluetoothManager)
}