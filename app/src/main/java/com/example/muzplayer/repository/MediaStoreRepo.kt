package com.example.muzplayer.repository

import android.content.Context
import com.example.muzplayer.models.Song
import com.example.muzplayer.utils.MediaStoreLoader
import javax.inject.Inject

interface MediaStoreRepo {
    suspend fun getAllSongs(): List<Song>
}

class MediaStoreRepoImpl @Inject constructor(
    private val mediaStoreLoader: MediaStoreLoader,
    private val context: Context
) : MediaStoreRepo {
    override suspend fun getAllSongs(): List<Song> {
        return mediaStoreLoader.initializeListIfNeeded(context)
    }

}