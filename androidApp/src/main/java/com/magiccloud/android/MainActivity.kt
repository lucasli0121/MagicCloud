package com.magiccloud.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.magiccloud.AndroidPlatformContext
import com.magiccloud.LocalPlatformContextProvider
import com.magiccloud.ui.MagicApp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.dsl.koinApplication

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val platformContext : AndroidPlatformContext by lazy { AndroidPlatformContext(this) }
        setContent {
            CompositionLocalProvider(LocalPlatformContextProvider provides platformContext) {
                MagicApp()
            }
        }
    }
}

@Preview
@Composable
fun MagicAppPreview() {
    MagicApp()
}