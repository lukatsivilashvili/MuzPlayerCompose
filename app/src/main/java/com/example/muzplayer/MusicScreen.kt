package com.example.muzplayer

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlaylistPlay
import androidx.compose.material.icons.outlined.Audiotrack
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PlaylistPlay
import androidx.compose.ui.graphics.vector.ImageVector

enum class MusicScreen(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
) {
    Home(
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    ),
    Library(
        selectedIcon = Icons.Filled.Audiotrack,
        unselectedIcon = Icons.Outlined.Audiotrack

    ),
    Playlists(
        selectedIcon = Icons.Filled.PlaylistPlay,
        unselectedIcon = Icons.Outlined.PlaylistPlay
    );

    companion object {
        fun fromRoute(route: String?): MusicScreen =
            when (route?.substringBefore("/")) {
                Home.name -> Home
                Library.name -> Library
                Playlists.name -> Playlists
                null -> Home
                else -> throw IllegalArgumentException("Route $route is not recognized.")
            }
    }
}