package com.magiccloud.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.magiccloud.ui.theme.MagicTheme
import magiccloud.shared.generated.resources.Res
import magiccloud.shared.generated.resources.wifi
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun Text14(res: StringResource, modifier: Modifier, textColor: Color = MagicTheme.colors.colorScheme.onSecondary ) =
    Text14(stringResource(res), modifier, textColor)

@Composable
fun Text14(text: String, modifier: Modifier, textColor: Color = MagicTheme.colors.colorScheme.onSecondary ) =
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.labelMedium.copy(
            fontWeight = FontWeight.Normal,
            letterSpacing = 0.sp,
            fontSize = 14.sp,
            color = textColor
        )
    )

@Composable
fun Text16(res: StringResource, modifier: Modifier, textColor: Color = MagicTheme.colors.colorScheme.onSecondary) =
    Text(
        modifier = modifier,
        text = stringResource(res),
        style = MaterialTheme.typography.labelMedium.copy(
            fontWeight = FontWeight.Normal,
            letterSpacing = 0.sp,
            fontSize = 16.sp,
            color = textColor
        )
    )

@Composable
fun Text16Bold(res: StringResource, modifier: Modifier, textColor: Color = MagicTheme.colors.colorScheme.onSecondary) =
    Text16Bold(stringResource(res), modifier, textColor)

@Composable
fun Text16Bold(text: String, modifier: Modifier, textColor: Color = MagicTheme.colors.colorScheme.onSecondary) =
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.labelMedium.copy(
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.sp,
            fontSize = 16.sp,
            color = textColor
        )
    )

@Composable
fun TextField14(valueText: String, modifier: Modifier, label: String = "", onValueChange: (String) -> Unit) =
    TextField(
        modifier = modifier
            .height(30.dp)
            .background(MagicTheme.colors.colorScheme.secondaryContainer, RoundedCornerShape(10.dp)),
        shape = RoundedCornerShape(10.dp),
        value = valueText,
        label = { Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Normal,
                letterSpacing = 0.sp,
                fontSize = 14.sp,
                color = MagicTheme.colors.colorScheme.onTertiary
            ))},
        onValueChange = onValueChange,
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            fontWeight = FontWeight.Normal,
            letterSpacing = 0.sp,
            fontSize = 14.sp,
            color = MagicTheme.colors.colorScheme.onSecondaryContainer
        ),
        colors = TextFieldDefaults
            .colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
            ),
    )


