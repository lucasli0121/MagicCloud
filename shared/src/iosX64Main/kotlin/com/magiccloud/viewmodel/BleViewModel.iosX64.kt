package com.magiccloud.viewmodel

import com.juul.kable.Peripheral
import com.juul.kable.PlatformAdvertisement
import kotlinx.coroutines.CoroutineScope

actual fun makePeripheralFromAdvertisement(
    scope: CoroutineScope,
    advertisement: PlatformAdvertisement,
    autoConnect: () -> Boolean
): Peripheral {
    TODO("Not yet implemented")
}