package com.magiccloud.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juul.kable.PlatformAdvertisement
import com.magiccloud.ble.BluetoothStatus
import com.magiccloud.ble.PlatformBluetoothManager
import com.magiccloud.ble.bluetoothScanner
import com.magiccloud.ble.bluetoothScannerWithBuilder
import com.magiccloud.scope.cancelChildren
import com.magiccloud.scope.childScope
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Duration


private val ScanDurationMillts = Duration.parse("60s").inWholeMilliseconds

class BleScanViewModel(private val bluetoothManager: PlatformBluetoothManager) : ViewModel() {
    private val scanScope = viewModelScope.childScope()
    private var found = arrayOf<PlatformAdvertisement>()

    private val _status: MutableStateFlow<BluetoothStatus> = MutableStateFlow(BluetoothStatus.ScanStoped)

    /** Provides current connection status. */
    val status: Flow<BluetoothStatus> = _status.combine(bluetoothManager.systemBluetoothStatus) {
            internalStatus, systemStatue -> if(systemStatue == BluetoothStatus.Available) internalStatus else systemStatue
    }

    private val _advertisements = MutableStateFlow<List<PlatformAdvertisement>>(emptyList())
    val advertisements = _advertisements.asStateFlow()

    fun start() {
        if (_status.value == BluetoothStatus.Scanning) return
        _status.value = BluetoothStatus.Scanning
        found = arrayOf<PlatformAdvertisement>()
        scanScope.launch {
            withTimeoutOrNull(ScanDurationMillts) {
                val advertisement = bluetoothScanner
                    .advertisements
                    .catch { cause -> _status.value = BluetoothStatus.Failed(cause.message?:"Unknown error") }
                    .onCompletion { cause ->
                        if(cause == null || cause is CancellationException) {
                            _status.value = BluetoothStatus.ScanStoped
                        }
                    }
                    .first()
                if(advertisement.name != null) {
                    Napier.i(advertisement.name.toString())
                }
                found = found.plus(advertisement)
            }
            _advertisements.value = found.toList()
        }
    }

    fun stop() {
        _status.value = BluetoothStatus.ScanStoped
        scanScope.cancelChildren()
    }

    fun clear() {
        stop()
        _advertisements.value = emptyList()
    }
}