package com.magiccloud.utils

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.magiccloud.ui.components.Text14
import com.magiccloud.ui.theme.MagicTheme
import kotlinx.coroutines.delay

/**
 * 文件名：ComposeToast
 * 描述： 实现一个compose KMP下的toast
 * 作者：liguoqiang
 * 日期：2025/3/27
 */
@Composable
fun ComposeToast(
    message: String,
    duration: Long = 3000,
    show: Boolean,
    onDismiss: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(if (visible) 1f else 0f)

    LaunchedEffect(show) {
        if (show) {
            visible = true
            delay(duration)
            visible = false
            delay(300) // Animation duration
            onDismiss()
        }
    }

    if (show) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .padding(bottom = 50.dp)
                    .alpha(alpha)
                    .background(
                        color = MagicTheme.colors.colorScheme.secondaryContainer,
                        shape = RoundedCornerShape(15.dp)
                    )
                    .padding(horizontal = 15.dp, vertical = 10.dp)
                    .wrapContentSize()
            ) {
                Text14(message, Modifier.padding(5.dp), MagicTheme.colors.colorScheme.onSecondaryContainer)
            }
        }
    }
}