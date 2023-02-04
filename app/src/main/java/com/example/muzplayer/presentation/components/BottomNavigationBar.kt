package com.example.muzplayer.presentation.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.muzplayer.common.MusicScreen

@Composable
fun BottomNavigationBar(
    allScreens: List<MusicScreen>,
    onItemSelected: (MusicScreen) -> Unit,
    currentScreen: MusicScreen
) {
    Surface(color = MaterialTheme.colorScheme.surface) {
        NavigationBar(
            modifier = Modifier.windowInsetsPadding(
                WindowInsets.safeDrawing.only(
                    WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom
                )
            ),
            containerColor = MaterialTheme.colorScheme.surface,
        ) {
            allScreens.forEach { screen ->
                val selected = currentScreen == screen
                NavigationBarItem(
                    selected = selected,
                    onClick = { onItemSelected(screen) },
                    icon = {
                        if (selected) {
                            screen.selectedIcon?.let { Icon(imageVector = it, contentDescription = screen.name) }
                        } else {
                            screen.unselectedIcon?.let { Icon(imageVector = it, contentDescription = screen.name) }
                        }
                    },
                    label = {
                        Text(
                            text = screen.name,
                            style = MaterialTheme.typography.labelMedium,
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        selectedTextColor = MaterialTheme.colorScheme.onSurface,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                    )
                )
            }
        }
    }
}