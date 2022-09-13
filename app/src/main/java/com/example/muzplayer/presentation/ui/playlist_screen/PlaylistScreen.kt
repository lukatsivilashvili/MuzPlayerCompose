package com.example.muzplayer.presentation.ui.playlist_screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.muzplayer.presentation.components.SinglePermission

@Composable
fun PlaylistBody() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
    ) {
        Log.d("currentThread", Thread.currentThread().name)
        SinglePermission()
    }
}