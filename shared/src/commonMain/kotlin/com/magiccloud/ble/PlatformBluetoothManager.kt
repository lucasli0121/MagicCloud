package com.magiccloud.ble

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.Flow

/** Provides system specific Bluetooth connectivity features for KMP targets. */
expect class PlatformBluetoothManager {
    /** Returns the Bluetooth status*/
    val systemBluetoothStatus: Flow<BluetoothStatus>
    /** Indicates whether it is possible to start scanning and connection. */
    val isPermissionsProvided: Boolean
}

@Composable
expect fun providerPlatformBluetoothManager(context: Any): PlatformBluetoothManager
