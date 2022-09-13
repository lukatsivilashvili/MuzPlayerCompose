package com.example.muzplayer.common.extensions

import java.util.concurrent.TimeUnit

fun Long.formatDuration(): String {
    val minutes = TimeUnit.MINUTES.convert(this, TimeUnit.MILLISECONDS)
    val seconds = TimeUnit.SECONDS.convert(
        this,
        TimeUnit.MILLISECONDS
    ) - minutes * TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES)
    return String.format("%02d:%02d", minutes, seconds)
}