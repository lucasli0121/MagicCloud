package com.magiccloud.ble

import androidx.compose.runtime.Composable
import com.juul.kable.Peripheral
import com.juul.kable.PeripheralBuilder
import com.juul.kable.PlatformAdvertisement
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

actual class PlatformBluetoothManager {
    /** Returns the Bluetooth status*/
    actual val systemBluetoothStatus: Flow<BluetoothStatus>
        get() {
            TODO()
        }

    /** Indicates whether it is possible to start scanning and connection. */
    actual val isPermissionsProvided: Boolean = false
}

@Composable
actual fun providerPlatformBluetoothManager(context: Any): PlatformBluetoothManager {
    TODO("Not yet implemented")
}