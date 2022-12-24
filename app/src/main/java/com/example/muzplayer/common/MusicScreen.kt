package com.example.muzplayer.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.PlaylistPlay
import androidx.compose.material.icons.outlined.Album
import androidx.compose.material.icons.outlined.Audiotrack
import androidx.compose.material.icons.outlined.PlaylistPlay
import androidx.compose.ui.graphics.vector.ImageVector

enum class MusicScreen(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
) {
    Library(
        selectedIcon = Icons.Filled.Audiotrack,
        unselectedIcon = Icons.Outlined.Audiotrack

    ),
    Album(
        selectedIcon = Icons.Filled.Album,
        unselectedIcon = Icons.Outlined.Album
    ),
    Playlists(
        selectedIcon = Icons.Filled.PlaylistPlay,
        unselectedIcon = Icons.Outlined.PlaylistPlay
    );

    companion object {
        fun fromRoute(route: String?): MusicScreen =
            when (route?.substringBefore("/")) {
                Album.name -> Album
                Library.name -> Library
                Playlists.name -> Playlists
                null -> Library
                else -> throw IllegalArgumentException("Route $route is not recognized.")
            }
    }
}