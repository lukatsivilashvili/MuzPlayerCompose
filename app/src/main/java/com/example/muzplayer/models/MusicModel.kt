package com.example.muzplayer.models

import android.net.Uri

data class MusicModel(
    val id: String,
    val title: String,
    val album: String,
    val artist: String,
    val duration: String,
    val uri: Uri? = null,
    val artUri: String
)
