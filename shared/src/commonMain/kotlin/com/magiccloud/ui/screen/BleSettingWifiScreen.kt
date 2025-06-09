package com.magiccloud.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.QueuePlayNext
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.magiccloud.ui.theme.MagicTheme
import com.magiccloud.viewmodel.BleScanViewModel
import com.magiccloud.viewmodel.BleViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.juul.kable.Advertisement
import com.juul.kable.PlatformAdvertisement
import com.magiccloud.LocalPlatformContextProvider
import com.magiccloud.ble.BleDevice
import com.magiccloud.ble.providerPlatformBluetoothManager
import com.magiccloud.ui.components.MagicIconButton
import com.magiccloud.ui.components.Text14
import com.magiccloud.ui.components.Text16
import com.magiccloud.ui.components.TextField14
import com.magiccloud.utils.ToastUtil
import com.magiccloud.utils.getToastUtil
import com.magiccloud.viewmodel.WifiState
import magiccloud.shared.generated.resources.Res
import magiccloud.shared.generated.resources.cancel
import magiccloud.shared.generated.resources.device_set_wifi
import magiccloud.shared.generated.resources.next
import magiccloud.shared.generated.resources.wifi
import magiccloud.shared.generated.resources.wifi_account_error
import magiccloud.shared.generated.resources.wifi_connect_failed
import magiccloud.shared.generated.resources.wifi_connect_success
import magiccloud.shared.generated.resources.wifi_connecting
import magiccloud.shared.generated.resources.wifi_passwd
import org.jetbrains.compose.resources.stringResource

/**
 * 文件名：BleSettingWifiScreen.kt
 * 描述：wifi配置页面，通过蓝牙进行设备wifi用户名和密码配置
 * 作者：liguoqiang
 * 日期：2025/3/22
 */

@Composable
fun BleSettingWifiScreen(
    advertisement: PlatformAdvertisement?,
    modifier: Modifier = Modifier,
    onComplete: (Boolean) -> Unit = {},
) {
    val context = LocalPlatformContextProvider.current.getContext()
    val platformBluetoothManager = providerPlatformBluetoothManager(context)
    val viewModel: BleViewModel = koinViewModel() { parametersOf(advertisement, platformBluetoothManager) }
    val state = viewModel.wifiState.collectAsState(WifiState.Disconnected)
    when(state.value) {
        WifiState.AccountError -> {
            getToastUtil().showToast(Res.string.wifi_account_error.toString())
        }
        WifiState.ConnectFailed -> {
            getToastUtil().showToast(Res.string.wifi_connect_failed.toString())
        }
        WifiState.Connected -> {
            getToastUtil().showToast(Res.string.wifi_connect_success.toString())
            onComplete(true)
        }
        WifiState.Connecting -> {
            getToastUtil().showToast(Res.string.wifi_connecting.toString())
        }
        else -> {

        }
    }
    showBleSettingWifiScreen(
        onSetNetwork = fun(wifi: String, pass: String) {
            viewModel.writeWifi(wifi, pass)
        },
        onCancel = fun() {
            onComplete(false)
        })
}
/**
 * @function:
 * @description: 显示wifi配置界面
 *
 * @param
 * @return
 */
@Composable
fun showBleSettingWifiScreen(onSetNetwork: (wifi: String, password: String) -> Unit, onCancel: () -> Unit) {
    var wifiInputState by remember { mutableStateOf("") }
    var passwordInputState by remember { mutableStateOf("") }

    MagicTheme {
        Surface(color = MagicTheme.colors.colorScheme.surface) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(horizontal = 15.dp, vertical = 5.dp)
                    .background(color = MagicTheme.colors.colorScheme.secondary),
                contentAlignment = Alignment.Center
                ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text14(res = Res.string.wifi, modifier = Modifier.weight(1f))
                        TextField14(
                            valueText = wifiInputState,
                            modifier = Modifier
                                .weight(3f)
                                .padding(horizontal = 2.dp),
                            label = "请输入wifi"
                        ) {
                            wifiInputState = it
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text14(res = Res.string.wifi_passwd, modifier = Modifier.weight(1f))
                        TextField14(
                            valueText = passwordInputState,
                            modifier = Modifier
                                .weight(3f)
                                .padding(horizontal = 2.dp),
                            label = "请输入密码"
                        ) {
                            passwordInputState = it
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(3f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        MagicIconButton(
                            onClick = fun() {
                                if (wifiInputState.isEmpty() || passwordInputState.isEmpty()) {
                                    getToastUtil().showToast("wifi和密码不能为空")
                                    return
                                }
                                onSetNetwork(wifiInputState, passwordInputState)
                            },
                            modifier = Modifier
                                .weight(0.5f)
                                .padding(horizontal = 5.dp),
                            icon = Icons.Default.Wifi,
                            iconDescription = Res.string.device_set_wifi.toString(),
                            textRes = Res.string.device_set_wifi)
                        MagicIconButton(
                            onClick = fun() {
                                onCancel()
                            },
                            modifier = Modifier
                                .weight(0.5f)
                                .padding(horizontal = 5.dp),
                            icon = Icons.Default.Cancel,
                            iconDescription = Res.string.cancel.toString(),
                            textRes = Res.string.cancel)

                    }
                }
            }
        }
    }
}
