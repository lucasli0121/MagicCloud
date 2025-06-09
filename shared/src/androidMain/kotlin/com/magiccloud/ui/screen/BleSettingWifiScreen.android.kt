package com.magiccloud.ui.screen

import androidx.compose.runtime.Composable
import com.magiccloud.ui.theme.MagicTheme
import org.jetbrains.compose.ui.tooling.preview.Preview


@Preview
@Composable
fun BleSettingWifiScreenPreview() {
    MagicTheme {
        showBleSettingWifiScreen (
            onSetNetwork = fun(wifi: String, password: String) {

            },
            onCancel = fun() {

            }
        )
    }
}