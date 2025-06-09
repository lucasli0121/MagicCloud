package com.magiccloud.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juul.kable.ConnectionLostException
import com.juul.kable.Peripheral
import com.juul.kable.PlatformAdvertisement
import com.juul.kable.State
import com.juul.kable.peripheral
import com.magiccloud.ble.BleDevice
import com.magiccloud.ble.BluetoothStatus
import com.magiccloud.ble.PlatformBluetoothManager
import com.magiccloud.scope.peripheralScope
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

sealed class WifiState {
    object Disconnected : WifiState()
    object Connecting : WifiState()
    object Connected : WifiState()
    object ConnectFailed: WifiState()
    object AccountError : WifiState()
}
private val reconnectDelay = 1.seconds

class BleViewModel(
    private val advertisement: PlatformAdvertisement,
    private val bluetoothManager: PlatformBluetoothManager
) : ViewModel() {

    private val autoConnect = MutableStateFlow(false)
    // Intermediary scope needed until https://github.com/JuulLabs/kable/issues/577 is resolved.
    private val scope = CoroutineScope(peripheralScope.coroutineContext + Job(peripheralScope.coroutineContext.job))
    private val peripheral = makePeripheralFromAdvertisement(scope, advertisement, autoConnect::value)

    private var bleDevice = BleDevice(peripheral)
    private val _status: MutableStateFlow<BluetoothStatus> = MutableStateFlow(BluetoothStatus.Disconnected)
    private val _wifiState: MutableStateFlow<WifiState> = MutableStateFlow(WifiState.Disconnected)
    val wifiState: Flow<WifiState> = _wifiState
    /** Provides current connection status. */
    val status: Flow<BluetoothStatus> = combine(
        _status,
        bluetoothManager.systemBluetoothStatus
    ) {
      internalStatus, systemStatus ->
        if(systemStatus == BluetoothStatus.Available)  internalStatus else systemStatus
    }

    fun writeWifi(ssid: String, passwd: String) {
        viewModelScope.launch {
            bleDevice.writeWifi(ssid, passwd)
            boardStateData.collect()
        }
    }
    // 订阅主板状态通知事件
    val boardStateData = bleDevice.boardStateObserve
        .onStart {  }
        .onCompletion {  }
        .onEach { value ->
            if(value.isNotEmpty()) {
                when(value[0].toInt()) {
                    1 -> {
                        _wifiState.value = WifiState.AccountError
                    }
                    2 -> {
                        _wifiState.value = WifiState.ConnectFailed
                    }
                    3 -> {
                        _wifiState.value = WifiState.Connected
                    }
                    4 -> {
                        _wifiState.value = WifiState.Connecting
                    }
                    else -> {
                        Napier.w { "unknown board state value: ${value[0].toInt()}" }
                    }
                }
            }
        }

    init {
        viewModelScope.setupPeripheral(peripheral)
        viewModelScope.enableAutoReconnect()
    }
    private fun CoroutineScope.enableAutoReconnect() {
        status.filter { status -> status is BluetoothStatus.Disconnected }
            .onEach {
                Napier.i { "Waiting bluetooth $reconnectDelay to reconnect..." }
                delay(reconnectDelay)
                connect()
            }
    }
    private fun CoroutineScope.connect() {
        launch {
            Napier.i { "bluetooth connecting..." }
            try {
                peripheral.connect()
                autoConnect.value = true
            } catch (e: ConnectionLostException) {
                autoConnect.value = false
                Napier.e { "Connection lost: $e" }
            }
        }
    }
    private fun CoroutineScope.setupPeripheral(peripheral: Peripheral) {
        launch {
            try {
                var isCurrentlyStarted = true
                peripheral.state.collect { currentState ->
                    if (currentState !is State.Disconnected || !isCurrentlyStarted) {
                        isCurrentlyStarted = false
                        this@BleViewModel._status.value = when (currentState) {
                            is State.Disconnected,
                            State.Disconnecting,
                                -> BluetoothStatus.Disconnected
                            State.Connecting.Bluetooth,
                            State.Connecting.Services,
                            State.Connecting.Observes,
                                -> BluetoothStatus.Connecting
                            is State.Connected,
                                -> BluetoothStatus.Connected // or set it in connectToPeripheral()
                        }
                    }
                }
            } catch (t: Throwable) {
                _status.value = BluetoothStatus.Disconnected
                coroutineContext.ensureActive()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        peripheralScope.launch {
            viewModelScope.coroutineContext.job.join()
            disconnect()
            scope.cancel()
        }
    }
    suspend fun disconnect() {
        peripheral.disconnect()
    }
}

expect fun makePeripheralFromAdvertisement(scope: CoroutineScope, advertisement: PlatformAdvertisement, autoConnect: () -> Boolean): Peripheral