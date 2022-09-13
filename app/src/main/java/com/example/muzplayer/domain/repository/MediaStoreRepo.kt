package com.example.muzplayer.domain.repository

import com.example.muzplayer.domain.models.Song

interface MediaStoreRepo {
    suspend fun getAllSongs(): List<Song>
}

