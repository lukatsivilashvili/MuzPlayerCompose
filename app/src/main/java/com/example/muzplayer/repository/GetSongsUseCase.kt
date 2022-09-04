package com.example.muzplayer.repository

import com.example.muzplayer.models.Song
import com.example.muzplayer.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetSongsUseCase @Inject constructor(private val repository: MediaStoreRepo) {
    operator fun invoke(): Flow<Resource<List<Song>>> = flow {
        try {
            emit(Resource.Loading())
            val songs = repository.getAllSongs()
            emit(Resource.Success(songs))
        } catch(e: Exception){
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
}