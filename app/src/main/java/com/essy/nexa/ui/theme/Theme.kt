package com.essy.nexa.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val NexaColorScheme = lightColorScheme(
    primary = NexaPrimary,
    secondary = NexaDark,
    background = NexaLight,
    surface = NexaWhite,
    onPrimary = NexaTextWhite,
    onBackground = NexaTextDark,
    onSurface = NexaTextDark,
    error = NexaAccent
)

@Composable
fun NexaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = NexaColorScheme,
        content = content
    )
}
