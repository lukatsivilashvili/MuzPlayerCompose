package com.example.muzplayer.data.repository

import android.content.Context
import com.example.muzplayer.data.MediaStoreLoader
import com.example.muzplayer.domain.MediaType
import com.example.muzplayer.domain.models.Album
import com.example.muzplayer.domain.models.Song
import com.example.muzplayer.domain.repository.MediaStoreRepo
import javax.inject.Inject

class MediaStoreRepositoryImpl @Inject constructor(
    private val mediaStoreLoader: MediaStoreLoader,
    private val context: Context
) : MediaStoreRepo {
    override suspend fun getAllSongs(): List<Song> {
        return mediaStoreLoader.initializeListIfNeeded(context, MediaType.SONG)
    }

    override suspend fun getAllAlbums(): List<Album> {
        return mediaStoreLoader.initializeListIfNeeded(context, MediaType.ALBUM)
    }
}