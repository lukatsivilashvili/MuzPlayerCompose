package com.example.muzplayer.models

data class Song(
    val mediaId: String = "",
    val title: String = "",
    val subtitle: String = "",
    val duration: Long = 0,
    val songUrl: String = "",
    val imageUrl: String = "",
    val hasArt: Boolean = true
)
