package com.example.muzplayer.ui.home_screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.muzplayer.components.HomeBottomBar
import com.example.muzplayer.components.SinglePermission

@Composable
fun HomeBody() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary),
    ) {
        HomeBottomBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
        )
        Log.d("currentThread", Thread.currentThread().name)
        SinglePermission()
    }
}