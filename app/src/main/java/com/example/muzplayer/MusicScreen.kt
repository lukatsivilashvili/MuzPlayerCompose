package com.example.muzplayer

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlaylistPlay
import androidx.compose.ui.graphics.vector.ImageVector

enum class MusicScreen(
    val icon: ImageVector,
) {
    Home(
        icon = Icons.Filled.Home,
    ),
    Library(
        icon = Icons.Filled.Audiotrack,

    ),
    Playlists(
        icon = Icons.Filled.PlaylistPlay,
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