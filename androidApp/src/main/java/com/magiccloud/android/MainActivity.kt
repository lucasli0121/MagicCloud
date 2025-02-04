package com.magiccloud.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.magiccloud.AndroidPlatformContext
import com.magiccloud.ui.MagicApp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val platformContext : AndroidPlatformContext by lazy { AndroidPlatformContext(this) }
        setContent {
            MagicApp(platformContext)
        }
    }
}




