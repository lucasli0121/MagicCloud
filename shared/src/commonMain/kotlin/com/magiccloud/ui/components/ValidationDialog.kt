package com.magiccloud.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.magiccloud.ui.theme.MagicTheme
import magiccloud.shared.generated.resources.Res
import magiccloud.shared.generated.resources.ok
import org.jetbrains.compose.resources.StringResource

/**
 * 文件名：ValidationDialog
 * 描述： 显示系统提示对话框，常常用作输入框的提示
 * 作者：liguoqiang
 * 日期：2025/3/26
 */

@Composable
fun ValidationErrorDialog(
    title: String,
    errorMessage: String,
    showDialog: Boolean,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        Dialog(onDismissRequest = onDismiss) {
            Card(
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(200.dp)
                    .padding(15.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .height(45.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "提示",
                        tint = Color.Red,
                        modifier = Modifier.padding(5.dp)
                    )
                    Text16Bold(title, modifier = Modifier.padding(start = 5.dp))
                }
                Row(modifier = Modifier
                    .fillMaxWidth(1f)
                    .height(45.dp),
                    verticalAlignment = Alignment.CenterVertically) {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = Color(0xFFFFEBEE),
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(10.dp)
                    ) {
                        Text(
                            text = errorMessage,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                Row(modifier = Modifier
                    .fillMaxWidth(1f)
                    .height(45.dp),
                    verticalAlignment = Alignment.CenterVertically) {

                    MagicButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .align(Alignment.CenterVertically)
                            .padding(top = 10.dp),
                        textRes = Res.string.ok
                    )
                }
            }
        }
    }
}