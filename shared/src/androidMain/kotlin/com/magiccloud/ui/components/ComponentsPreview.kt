package com.magiccloud.ui.components

import android.content.res.Resources
import androidx.compose.runtime.Composable
import com.magiccloud.R
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * 文件名：ComponentsPreview
 * 描述：
 * 作者：liguoqiang
 * 日期：2025/3/26
 */

@Preview
@Composable
fun ShowValidationDialog() {
    ValidationErrorDialog(
        title = "提示信息",
        errorMessage = "This is an error message",
        showDialog = true,
        onDismiss = {}
    )
}