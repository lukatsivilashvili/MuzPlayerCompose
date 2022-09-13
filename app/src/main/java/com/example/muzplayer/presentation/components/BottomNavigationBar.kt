package com.example.muzplayer.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
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
                            Icon(imageVector = screen.selectedIcon, contentDescription = screen.name)
                        } else {
                            Icon(imageVector = screen.unselectedIcon, contentDescription = screen.name)
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