package com.magiccloud.ble

import android.Manifest
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.BLUETOOTH_CONNECT
import android.Manifest.permission.BLUETOOTH_SCAN
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.P
import android.os.Build.VERSION_CODES.R
import androidx.compose.runtime.Composable
import com.juul.kable.Bluetooth
import com.juul.kable.Reason
import kotlinx.coroutines.flow.map
import org.koin.compose.koinInject
import org.koin.core.context.GlobalContext.get
import org.koin.core.parameter.parametersOf
import javax.inject.Inject

val Bluetooth.permissionsNeeded: List<String> by lazy {
    when {
        // If your app targets Android 9 (API level 28) or lower, you can declare the ACCESS_COARSE_LOCATION permission
        // instead of the ACCESS_FINE_LOCATION permission.
        // https://developer.android.com/guide/topics/connectivity/bluetooth/permissions#declare-android11-or-lower
        SDK_INT <= P -> listOf(ACCESS_COARSE_LOCATION)

        // ACCESS_FINE_LOCATION is necessary because, on Android 11 (API level 30) and lower, a Bluetooth scan could
        // potentially be used to gather information about the location of the user.
        // https://developer.android.com/guide/topics/connectivity/bluetooth/permissions#declare-android11-or-lower
        SDK_INT <= R -> listOf(ACCESS_FINE_LOCATION)

        // If your app targets Android 12 (API level 31) or higher, declare the following permissions in your app's
        // manifest file:
        //
        // 1. If your app looks for Bluetooth devices, such as BLE peripherals, declare the `BLUETOOTH_SCAN` permission.
        // 2. If your app makes the current device discoverable to other Bluetooth devices, declare the
        //    `BLUETOOTH_ADVERTISE` permission.
        // 3. If your app communicates with already-paired Bluetooth devices, declare the BLUETOOTH_CONNECT permission.
        // https://developer.android.com/guide/topics/connectivity/bluetooth/permissions#declare-android12-or-higher
        else /* SDK_INT >= S */ -> listOf(BLUETOOTH_SCAN, BLUETOOTH_CONNECT)
    }
}

actual class PlatformBluetoothManager (private val context: Context) {

    /** Returns the Bluetooth status of the system: `Unusable` subtypes or null if it's `Usable`. */
    actual val systemBluetoothStatus = Bluetooth.availability.map {
        when (it) {
            Bluetooth.Availability.Available -> BluetoothStatus.Available
            is Bluetooth.Availability.Unavailable -> when (it.reason) {
                Reason.Off -> BluetoothStatus.Disabled
                Reason.LocationServicesDisabled -> BluetoothStatus.LocationShouldBeEnable
                else -> BluetoothStatus.UnAvailable(it.reason.toString())
            }
        }
    }

    /** Indicates whether it is possible to start scanning and connection. */
    actual val isPermissionsProvided
        get() = if (SDK_INT < Build.VERSION_CODES.S) {
            context.isPermissionProvided(ACCESS_FINE_LOCATION)
        } else {
            context.isPermissionProvided(BLUETOOTH_SCAN) &&
                    context.isPermissionProvided(BLUETOOTH_CONNECT)
        }
}

private fun Context.isPermissionProvided(permission: String): Boolean =
    ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED


@Composable
actual fun providerPlatformBluetoothManager(context: Any): PlatformBluetoothManager {
    val platform : PlatformBluetoothManager = koinInject() {parametersOf(context as Context)}
    return platform
}