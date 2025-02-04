package com.magiccloud.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.juul.kable.Advertisement
import com.juul.kable.PlatformAdvertisement
import com.magiccloud.LocalPlatformContextProvider
import com.magiccloud.ble.providerPlatformBluetoothManager
import com.magiccloud.ui.theme.MagicTheme
import com.magiccloud.viewmodel.BleScanViewModel
import com.magiccloud.viewmodel.BleViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Preview
@Composable
fun BleSettingWifiScreen(
    advertisement: PlatformAdvertisement,
    onNextClicked: (Advertisement) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalPlatformContextProvider.current.getContext()
    val platformBluetoothManager = providerPlatformBluetoothManager(context)
    val viewModel: BleViewModel = koinViewModel() { parametersOf(advertisement, platformBluetoothManager) }
    MagicTheme {
        Surface(color = MagicTheme.colors.colorScheme.surface) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(

                ) {

                }
            }
        }
    }
}