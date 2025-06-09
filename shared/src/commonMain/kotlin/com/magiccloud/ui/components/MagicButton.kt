package com.magiccloud.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.magiccloud.ui.theme.MagicTheme
import org.jetbrains.compose.resources.StringResource

private val ButtonShape = RoundedCornerShape(percent = 50)

@Composable
fun MagicIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    icon: ImageVector,
    iconDescription: String,
    textRes: StringResource
) =
    IconButton(
        modifier = modifier
            .background(MagicTheme.colors.colorScheme.primaryContainer, shape = ButtonShape)
            .height(45.dp)
            .padding( horizontal = 10.dp, vertical = 5.dp),
        onClick = onClick,
        enabled = enabled,
        colors = IconButtonColors(
            contentColor = MagicTheme.colors.colorScheme.onPrimaryContainer,
            disabledContentColor = MagicTheme.colors.textHelp,
            containerColor = MagicTheme.colors.colorScheme.primaryContainer,
            disabledContainerColor = MagicTheme.colors.colorScheme.primaryContainer.copy(alpha = 0.12f)
        ),
        interactionSource = interactionSource
    ) {
        Row(modifier = Modifier.padding(horizontal = 5.dp)) {
            Icon(
                imageVector = icon,
                contentDescription = iconDescription,
                tint = MagicTheme.colors.colorScheme.onPrimaryContainer
            )
            Text16(res = textRes, modifier = Modifier.padding(start=5.dp), textColor = MagicTheme.colors.colorScheme.onPrimaryContainer)
        }

    }


@Composable
fun MagicButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    textRes: StringResource
) =
    IconButton(
        modifier = modifier
            .background(MagicTheme.colors.colorScheme.primaryContainer, shape = ButtonShape)
            .height(45.dp)
            .padding( horizontal = 10.dp, vertical = 5.dp),
        onClick = onClick,
        enabled = enabled,
        colors = IconButtonColors(
            contentColor = MagicTheme.colors.colorScheme.onPrimaryContainer,
            disabledContentColor = MagicTheme.colors.textHelp,
            containerColor = MagicTheme.colors.colorScheme.primaryContainer,
            disabledContainerColor = MagicTheme.colors.colorScheme.primaryContainer.copy(alpha = 0.12f)
        ),
        interactionSource = interactionSource
    ) {
        Row(modifier = Modifier.padding(horizontal = 5.dp)) {
            Text16(res = textRes, modifier = Modifier.padding(start=5.dp), textColor = MagicTheme.colors.colorScheme.onPrimaryContainer)
        }

    }

