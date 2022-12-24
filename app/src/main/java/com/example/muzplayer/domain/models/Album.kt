package com.example.muzplayer.domain.models

/**
 * Created by lukatsivilashvili on 22.12.22 4:37 PM.
 */
data class Album(
    val albumId: String = "",
    val artist: String = "",
    val title: String = "",
    val imageUrl: String = "",
    val songCount: Int = 0
)
