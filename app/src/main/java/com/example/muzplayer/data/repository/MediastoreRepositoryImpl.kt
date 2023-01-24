package com.example.muzplayer.data.repository

import android.content.Context
import com.example.muzplayer.data.MediaStoreLoader
import com.example.muzplayer.data.SimpleDataStoreCache
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

        val cachedSongsList = SimpleDataStoreCache.songsMap[MediaType.SONG]
        return if (!cachedSongsList.isNullOrEmpty()){
            cachedSongsList
        }else{
            val songsList: List<Song> = mediaStoreLoader.initializeListIfNeeded(context, MediaType.SONG)
            SimpleDataStoreCache.songsMap[MediaType.SONG] = songsList
            songsList
        }
    }

    override suspend fun getAllAlbums(): List<Album> {

        val cachedAlbumsList = SimpleDataStoreCache.albumMap[MediaType.ALBUM]
        return if (!cachedAlbumsList.isNullOrEmpty()){
            cachedAlbumsList
        }else{
            val albumsList: List<Album> = mediaStoreLoader.initializeListIfNeeded(context, MediaType.ALBUM)
            SimpleDataStoreCache.albumMap[MediaType.ALBUM] = albumsList
            albumsList
        }
    }
}