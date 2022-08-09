package com.example.muzplayer

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlaylistPlay
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.muzplayer.ui.theme.TabLight

enum class MusicScreen(
    val icon: ImageVector,
    val background: androidx.compose.ui.graphics.Color
) {
    Home(
        icon = Icons.Filled.Home,
        background = TabLight
    ),
    Library(
        icon = Icons.Filled.Audiotrack,
        background = TabLight

    ),
    Playlists(
        icon = Icons.Filled.PlaylistPlay,
        background = TabLight
    );

    companion object {
        fun fromRoute(route: String?): MusicScreen =
            when (route?.substringBefore("/")) {
                Home.name -> Home
                Library.name -> Library
                Playlists.name -> Playlists
                null -> Library
                else -> throw IllegalArgumentException("Route $route is not recognized.")
            }
    }
}