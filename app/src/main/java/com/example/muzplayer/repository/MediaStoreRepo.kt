package com.example.muzplayer.repository

import android.content.Context
import android.util.Log.d
import com.example.muzplayer.models.Song
import com.example.muzplayer.utils.MediaStoreLoader
import com.example.muzplayer.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface MediaStoreRepo {
    suspend fun getAllSongs(): Resource<List<Song>>
}

class MediaStoreRepoImpl @Inject constructor(
    private val mediaStoreLoader: MediaStoreLoader,
    private val context: Context
) : MediaStoreRepo {
    override suspend fun getAllSongs(): Resource<List<Song>> =
        withContext(Dispatchers.IO) {
            d("currentThread", Thread.currentThread().name)
            try {
                val result = mediaStoreLoader.initializeListIfNeeded(context)
                if (result.isNotEmpty()) {
                    Resource.Success(result)
                } else {
                    Resource.Error("Music couldn't be found")
                }
            } catch (e: Exception) {
                Resource.Error("Error")
            }
        }
}