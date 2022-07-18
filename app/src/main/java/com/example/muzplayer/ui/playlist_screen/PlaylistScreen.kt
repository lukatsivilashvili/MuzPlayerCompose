package com.example.muzplayer.ui.playlist_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.muzplayer.MusicScreen

@Composable
fun PlaylistBody() {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(MusicScreen.Home.background),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Playlist Screen",
            modifier = Modifier
        )
    }
}