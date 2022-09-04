package com.example.muzplayer.repository

import android.content.Context
import android.util.Log.d
import com.example.muzplayer.models.Song
import com.example.muzplayer.utils.MediaStoreLoader
import com.example.muzplayer.utils.Resource
import javax.inject.Inject

interface MediaStoreRepo {
    suspend fun getAllSongs(): Resource<List<Song>>
}

class MediaStoreRepoImpl @Inject constructor(
    private val mediaStoreLoader: MediaStoreLoader,
    private val context: Context
) : MediaStoreRepo {
    override suspend fun getAllSongs(): Resource<List<Song>> {
        return try {
            when (val result = mediaStoreLoader.initializeListIfNeeded(context)) {
                is Resource.Success -> {
                    d("myRes", result.toString())
                    Resource.Success(result.data!!)
                }
                is Resource.Error ->{
                    Resource.Error(result.message)
                }
                else -> {Resource.Loading()}
            }
        }catch (e: Exception){
            Resource.Error(e.message)
        }
    }

}