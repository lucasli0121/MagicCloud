package com.magiccloud.ui.screen

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.juul.kable.Bluetooth
import com.magiccloud.ble.permissionsNeeded
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.juul.kable.Advertisement
import com.juul.kable.PlatformAdvertisement
import com.magiccloud.LocalPlatformContextProvider
import com.magiccloud.activitis.openAppDetails
import com.magiccloud.ui.theme.MagicTheme
import magiccloud.shared.generated.resources.Res

@OptIn(ExperimentalPermissionsApi::class)
@Composable
actual fun bluetoothPermissionsNeeded(context: Any): Boolean {
    val ctx = context as Activity
    val permissionsState = rememberMultiplePermissionsState(Bluetooth.permissionsNeeded)
    var didAskPermission by remember { mutableStateOf(false) }
    if (!didAskPermission) {
        didAskPermission = true
        SideEffect {
            permissionsState.launchMultiplePermissionRequest()
        }
    }
    if(!permissionsState.allPermissionsGranted) {
        if(permissionsState.shouldShowRationale) {
            BluetoothPermissionsNotGranted(permissionsState)
        } else {
            BluetoothPermissionsNotAvailable(ctx::openAppDetails)
        }
    } else {
        return true
    }
    return false
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun BluetoothPermissionsNotGranted(permissions: MultiplePermissionsState) {
    ActionRequired(
        icon = Icons.Filled.LocationOn,
        contentDescription = "Bluetooth permissions required",
        description = "Bluetooth permissions are required for scanning. Please grant the permission.",
        buttonText = "Continue",
        onClick = permissions::launchMultiplePermissionRequest,
    )
}

@Composable
private fun BluetoothPermissionsNotAvailable(openSettingsAction: () -> Unit) {
    ActionRequired(
        icon = Icons.Filled.Warning,
        contentDescription = "Bluetooth permissions required",
        description = "Bluetooth permission denied. Please, grant access on the Settings screen.",
        buttonText = "Open Settings",
        onClick = openSettingsAction,
    )
}

@Composable
private fun ActionRequired(
    icon: ImageVector,
    contentDescription: String?,
    description: String,
    buttonText: String,
    onClick: () -> Unit,
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Center,
    ) {
        Icon(
            modifier = Modifier.size(150.dp),
            tint = contentColorFor(backgroundColor = MaterialTheme.colorScheme.background),
            imageVector = icon,
            contentDescription = contentDescription,
        )
        Spacer(Modifier.size(8.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .align(CenterHorizontally),
            textAlign = TextAlign.Center,
            text = description,
        )
        Spacer(Modifier.size(15.dp))
        Button(onClick) {
            Text(buttonText)
        }
    }
}

@Composable
actual fun ShowAdvertisementItem(
    advertisement: PlatformAdvertisement,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height = 60.dp)
            .padding(horizontal = 10.dp)
            .clip(
                RoundedCornerShape(
                    10.dp,
                    10.dp,
                    10.dp,
                    10.dp
                )
            )
            .background(color = MagicTheme.colors.colorScheme.secondary)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    fontSize = 18.sp,
                    text = advertisement.name ?: "Unknown",
                    color = MagicTheme.colors.colorScheme.onSecondaryContainer
                )
                Text(
                    fontSize = 14.sp,
                    text = advertisement.address,
                    color = MagicTheme.colors.colorScheme.onTertiary)
            }

            Text(
                modifier = Modifier.align(CenterVertically),
                fontSize = 14.sp,
                text = "${advertisement.rssi} dBm",
                color = MagicTheme.colors.colorScheme.onTertiary
            )
        }
    }
}