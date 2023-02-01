package com.example.muzplayer.presentation.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
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
fun ProvideDimens(
    dimensions: Dimensions,
    content: @Composable () -> Unit
) {
    val dimensionSet = remember { dimensions }
    CompositionLocalProvider(LocalAppDimens provides dimensionSet, content = content)
}

private val LocalAppDimens = staticCompositionLocalOf {
    smallDimensions
}

@Composable
fun MuzPlayerTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    isDynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colors = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && isDynamicColor && isDarkTheme -> {
            dynamicDarkColorScheme(LocalContext.current)
        }

        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && isDynamicColor && !isDarkTheme -> {
            dynamicLightColorScheme(LocalContext.current)
        }

        isDarkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val configuration = LocalConfiguration.current
    val dimensions = if (configuration.screenWidthDp <= 360) smallDimensions else sw360Dimensions


    ProvideDimens(dimensions = dimensions) {
        MaterialTheme(
            colorScheme = colors,
            typography = typography,
            content = content,
        )

    }
}

object AppTheme {
    val dimens: Dimensions
        @Composable
        get() = LocalAppDimens.current
}
