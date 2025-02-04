package com.magiccloud.ble

/* 通过密封接口定义不同的状态*/
sealed interface BluetoothStatus {
    /**
     * Only on Android and iOS. It is implied that you will use this in the UI layer:
     *
     * ```
     * val correctConnectionStatus =
     *   if (btPermissions.allPermissionsGranted) state.connectionStatus else BluetoothConnectionStatus.NoPermissions
     * ```
     */
    data object NoPermissions : BluetoothStatus
    data class Failed(val message: CharSequence): BluetoothStatus
    /** Bluetooth not available. */
    data class UnAvailable(val message: CharSequence): BluetoothStatus
    object Available: BluetoothStatus
    /**
     * Only on Android 11 and below (on older systems, Bluetooth will not work if GPS is turned off).
     *
     * To enable, enable it via statusbar, use the Google Services API or go to location settings:
     *
     * ```
     * fun Context.goToLocationSettings() = startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
     * ```
     */
    object LocationShouldBeEnable: BluetoothStatus
    /**
     * To enable (on Android), use this:
     *
     * ```
     * fun Context.isPermissionProvided(permission: String): Boolean =
     *   ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
     *
     * @SuppressLint("MissingPermission")
     * fun Context.enableBluetoothDialog() {
     *   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
     *     !isPermissionProvided(Manifest.permission.BLUETOOTH_CONNECT)
     *   ) {
     *     return
     *   }
     *
     *   startActivity(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
     * }
     * ```
     */
    object Disabled: BluetoothStatus

    object Enabled : BluetoothStatus
        // Bluetooth scanning stopped
    object ScanStoped: BluetoothStatus
    object Scanning: BluetoothStatus
    object Disconnected: BluetoothStatus
    object Connecting: BluetoothStatus
    object Connected: BluetoothStatus
}