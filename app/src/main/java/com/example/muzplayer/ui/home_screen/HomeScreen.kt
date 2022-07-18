package com.example.muzplayer.ui.home_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.muzplayer.MusicScreen
import com.example.muzplayer.SinglePermission

@Composable
fun HomeBody() {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(MusicScreen.Home.background),
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Home Screen",
            modifier = Modifier
        )
        SinglePermission()
    }
}