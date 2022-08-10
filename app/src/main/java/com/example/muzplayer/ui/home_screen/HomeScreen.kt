package com.example.muzplayer.ui.home_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.muzplayer.SinglePermission

@Composable
fun HomeBody() {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary),
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Home Screen",
            modifier = Modifier
        )
        SinglePermission()
    }
}