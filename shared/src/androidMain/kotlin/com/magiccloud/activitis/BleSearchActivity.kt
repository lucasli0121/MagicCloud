package com.magiccloud.activitis

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.magiccloud.AndroidPlatformContext
import com.magiccloud.LocalPlatformContextProvider
import com.magiccloud.ui.MagicApp
import com.magiccloud.ui.theme.MagicTheme

class BleSearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val platformContext : AndroidPlatformContext by lazy { AndroidPlatformContext(this) }
        setContent {
            CompositionLocalProvider(LocalPlatformContextProvider provides platformContext) {
                showContent()
            }
        }
    }
    @Composable
    private fun showContent() {
        MagicTheme {
            Column(
                modifier = Modifier.fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background)
            ) {
            }
        }
    }
}