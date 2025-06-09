package com.magiccloud.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.juul.kable.PlatformAdvertisement

@Composable
actual fun bluetoothPermissionsNeeded(context: Any): Boolean {
    return false
}

@Composable
actual fun ShowAdvertisementItem(
    advertisement: PlatformAdvertisement,
    onClick: () -> Unit
) {
}