package com.magiccloud.viewmodel

import com.juul.kable.Peripheral
import com.juul.kable.PlatformAdvertisement
import com.juul.kable.peripheral
import kotlinx.coroutines.CoroutineScope

actual fun makePeripheralFromAdvertisement(scope: CoroutineScope, advertisement: PlatformAdvertisement, autoConnect: () -> Boolean): Peripheral {
    return scope.peripheral(advertisement) {
        autoConnectIf(predicate = autoConnect)
        onServicesDiscovered {
            requestMtu(128)
        }
    }
}