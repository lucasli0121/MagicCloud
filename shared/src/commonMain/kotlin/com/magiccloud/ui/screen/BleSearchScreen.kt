package com.magiccloud.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.juul.kable.PlatformAdvertisement
import com.magiccloud.LocalPlatformContextProvider
import com.magiccloud.ble.BluetoothStatus
import com.magiccloud.ble.providerPlatformBluetoothManager
import com.magiccloud.ui.theme.MagicTheme
import com.magiccloud.viewmodel.BleScanViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

/**
 * 文件名：BleSearchScreen.kt
 * 描述：扫描BLE设备
 * 作者：Li guo qiang
 * 日期：2025/3/30
 */

@Composable
fun BleSearchScreen(
    onNextClicked: (PlatformAdvertisement) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalPlatformContextProvider.current.getContext()
    val platformBluetoothManager = providerPlatformBluetoothManager(context)
    val viewModel: BleScanViewModel = koinViewModel() { parametersOf(platformBluetoothManager) }
    val grantedOk = bluetoothPermissionsNeeded(context)

    MagicTheme {
        Surface(color = MagicTheme.colors.colorScheme.surface) {
            Box(modifier = Modifier.fillMaxSize()) {
                if (grantedOk) {
                    val advertisementState = viewModel.advertisements.collectAsState()
                    if (advertisementState.value.isNotEmpty()) {
                        viewModel.stop()
                        showAdvertisementItems(advertisementState.value, onNextClicked)
                    }
                    LaunchedEffect(Unit) {
                        viewModel.clear()
                        viewModel.start()
                    }
                    val status = viewModel.status.collectAsState(initial = BluetoothStatus.ScanStoped)
                    when(status.value) {
                        BluetoothStatus.Disabled -> {

                        }
                        is BluetoothStatus.Failed -> {

                        }
                        BluetoothStatus.NoPermissions -> {

                        }
                        is BluetoothStatus.UnAvailable -> {

                        }
                        else {

                        }
                    }
                }
            }
        }
    }
}

@Composable
fun showAdvertisementItems(advertisements: List<PlatformAdvertisement>, onClick: (PlatformAdvertisement) -> Unit) {
    val listState = rememberLazyListState()
    if (advertisements.isNotEmpty()) {
        LaunchedEffect(advertisements.last()) {
            listState.animateScrollToItem(advertisements.lastIndex, scrollOffset = 2)
        }
    }
    LazyColumn (
        modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            state = listState
    ){
        item { Spacer(Modifier.size(20.dp)) }
        items(advertisements.size ) { index ->
            val advertisement = advertisements[index]
            ShowAdvertisementItem(advertisement) {
                onClick(advertisement)
            }
        }
//        item {
//            Box(Modifier.height(70.dp))
//        }
    }
}


@Composable
expect fun ShowAdvertisementItem(advertisement: PlatformAdvertisement, onClick: () -> Unit)

@Composable
expect fun bluetoothPermissionsNeeded(context: Any): Boolean