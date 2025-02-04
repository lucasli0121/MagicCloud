package com.magiccloud.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.juul.kable.Advertisement
import com.juul.kable.Bluetooth
import com.juul.kable.PlatformAdvertisement
import com.magiccloud.LocalPlatformContextProvider
import com.magiccloud.ble.providerPlatformBluetoothManager
import com.magiccloud.redux.Action
import com.magiccloud.ui.theme.MagicTheme
import com.magiccloud.viewmodel.BleScanViewModel
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.collect
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

/*
*
* */
@Preview
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
            Box (modifier = Modifier.fillMaxSize()) {
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