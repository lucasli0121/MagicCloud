package com.magiccloud.ui

import android.content.Context
import android.content.Intent
import com.magiccloud.activitis.BleSearchActivity

actual fun RadarCloudSetting(any: Any) {
    val context = any as Context
    val intent = Intent(context, BleSearchActivity::class.java)
    context.startActivity(intent)
}
