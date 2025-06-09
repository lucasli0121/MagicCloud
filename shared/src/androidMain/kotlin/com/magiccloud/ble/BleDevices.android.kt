package com.magiccloud.ble

import android.bluetooth.le.ScanSettings
import com.juul.kable.Filter
import com.juul.kable.ObsoleteKableApi
import com.juul.kable.PlatformAdvertisement
import com.juul.kable.Scanner
import com.juul.kable.logs.Logging
import com.juul.kable.logs.SystemLogEngine
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ObsoleteKableApi::class, ExperimentalUuidApi::class)
actual val bluetoothScannerWithBuilder: Scanner<PlatformAdvertisement>
    get() = Scanner {
        scanSettings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()
        logging {
            engine = SystemLogEngine
            level = Logging.Level.Warnings
            format = Logging.Format.Multiline
        }
        filters {
            Filter.Name.Prefix("H03_")
            Filter.Service(SERVICE_UUID)
        }
    }