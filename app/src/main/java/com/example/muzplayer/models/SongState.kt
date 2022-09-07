package com.example.muzplayer.models

data class SongState(
    val isLoading: Boolean = false,
    val songs: List<Song> = emptyList(),
    val error: String = ""
)
