package com.magiccloud.ble

import com.juul.kable.Characteristic
import com.juul.kable.Filter
import com.juul.kable.Peripheral
import com.juul.kable.PlatformAdvertisement
import com.juul.kable.Scanner
import com.juul.kable.ServicesDiscoveredPeripheral
import com.juul.kable.WriteType
import com.juul.kable.characteristicOf
import com.juul.kable.logs.Logging
import com.juul.kable.logs.SystemLogEngine
import io.github.aakira.napier.Napier
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.first
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.coroutineContext
import kotlin.experimental.or
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
val SERVICE_UUID = kotlin.uuid.Uuid.parse("000000FF-0000-1000-8000-00805F9B34FB")
@OptIn(ExperimentalUuidApi::class)
private val serviceUuid = tagUuid("00F4")
@OptIn(ExperimentalUuidApi::class)
private val wifiConfigUuid = tagUuid("F401")
@OptIn(ExperimentalUuidApi::class)
private val cmdConfigUuid = tagUuid("F403")
@OptIn(ExperimentalUuidApi::class)
private val boardMsgUuid = tagUuid("F501")
@OptIn(ExperimentalUuidApi::class)
private val boardStateUuid = tagUuid("F301")
@OptIn(ExperimentalUuidApi::class)
private val radarDataUuid = tagUuid("F302")

private const val RETRY_ATTEMPTS = 7

@OptIn(ExperimentalUuidApi::class)
private fun tagUuid(short16BitUuid: String) : kotlin.uuid.Uuid =
    kotlin.uuid.Uuid.parse("0000${short16BitUuid.lowercase()}-0000-1000-8000-00805F9B34FB")

// from uuid to string
@OptIn(ExperimentalUuidApi::class)
private fun characteristicOf(service: Uuid, characteristic: Uuid) =
    characteristicOf(service, characteristic)

@OptIn(ExperimentalUuidApi::class)
val wifiConfigCharacteristic = characteristicOf(
    service = serviceUuid,
    characteristic = wifiConfigUuid,
)

@OptIn(ExperimentalUuidApi::class)
val cmdConfigCharacteristic = characteristicOf(
    service = serviceUuid,
    characteristic = cmdConfigUuid,
)

@OptIn(ExperimentalUuidApi::class)
val boardStateCharacteristic = characteristicOf(
    service = serviceUuid,
    characteristic = boardStateUuid,
)

@OptIn(ExperimentalUuidApi::class)
val radarDataCharacteristic = characteristicOf(
    service = serviceUuid,
    characteristic = radarDataUuid,
)

@OptIn(ExperimentalUuidApi::class)
val boardMsgCharacteristic = characteristicOf(
    service = serviceUuid,
    characteristic = boardMsgUuid,
)
expect val bluetoothScannerWithBuilder : Scanner<PlatformAdvertisement>

@OptIn(ExperimentalUuidApi::class)
val bluetoothScanner = Scanner {
    logging {
        engine = SystemLogEngine
        level = Logging.Level.Warnings
        format = Logging.Format.Multiline
    }
    filters {
        match {
            services = listOf(SERVICE_UUID)
//            name = Filter.Name.Prefix("ED719M")
            name = Filter.Name.Prefix("H03")
        }

    }
}

class BleDevice(
    private val peripheral: Peripheral
) : Peripheral by peripheral {

    val boardMsgObserve = peripheral.observe(boardMsgCharacteristic)
    val boardStateObserve = peripheral.observe(boardStateCharacteristic)
    val radarDataObserve = peripheral.observe(radarDataCharacteristic)

    /** read data from characteristic. */
    suspend fun readData(
        characteristic: Characteristic,
        attempts: Int = RETRY_ATTEMPTS,
    ): ByteArray? {

        repeat(attempts) {
            try {
                return peripheral.read(characteristic)
            } catch (e: Exception) {
                Napier.e("readFrom exception caught: $e")
                if (e is CancellationException) {
                    return null
                } else if (it != attempts - 1) {
                    Napier.w("Retrying...")
                }
            }
        }
        return null
    }

    suspend fun writeTo(
        characteristic: Characteristic,
        data: ByteArray,
        withResponse: Boolean,
    ) {
        peripheral.write(
            characteristic,
            data,
            if(withResponse) WriteType.WithResponse else WriteType.WithoutResponse
        )

    }

    suspend fun writeWifi(ssid: String, passwd: String) {
        val wifiValue = "${ssid}|${passwd}".encodeToByteArray()
        var pkNum = wifiValue.size / 18
        if (wifiValue.size % 18 > 0) {
            pkNum++
        }
        var startIndex = 0
        for ( i in 1 ..  pkNum ) {
            val len = if(i < pkNum) 20 else wifiValue.size - startIndex + 2
            var destVal = ByteArray(len).apply {
                0 to 0xa7.toByte()
            }
            val pk: Byte = ((pkNum shl 4) and 0xf0).toByte() or (i and 0x0f).toByte()
            destVal[1] = pk
            destVal = wifiValue.copyInto(destVal, 2, startIndex, startIndex + len - 2)
            startIndex += (len - 2)
            writeTo(wifiConfigCharacteristic, destVal, false)
        }
    }
}