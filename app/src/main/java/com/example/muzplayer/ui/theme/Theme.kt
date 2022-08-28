package com.example.muzplayer.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorSchemme = darkColorScheme(
    primary = TabDark,
    secondary = TabDarkSecondary,
    onPrimary = OnTabDark
)

private val LightColorScheme = lightColorScheme(
    primary = TabLight,
    secondary = TabLightSecondary,
    onPrimary = OnTabLight

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun MuzPlayerTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    isDynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val dynamicColor = isDynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val colors =  when {
        dynamicColor && isDarkTheme -> {
            dynamicDarkColorScheme(LocalContext.current)
        }
        dynamicColor && !isDarkTheme -> {
            dynamicLightColorScheme(LocalContext.current)
        }
        isDarkTheme -> DarkColorSchemme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colors,
        typography = typography,
        content = content
    )
}