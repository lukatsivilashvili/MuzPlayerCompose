package com.example.muzplayer.data.repository

import android.content.Context
import com.example.muzplayer.domain.models.Song
import com.example.muzplayer.domain.repository.MediaStoreRepo
import com.example.muzplayer.data.MediaStoreLoader
import javax.inject.Inject

class MediaStoreRepositoryImpl @Inject constructor(
    private val mediaStoreLoader: MediaStoreLoader,
    private val context: Context
) : MediaStoreRepo {
    override suspend fun getAllSongs(): List<Song> {
        return mediaStoreLoader.initializeListIfNeeded(context)
    }
}