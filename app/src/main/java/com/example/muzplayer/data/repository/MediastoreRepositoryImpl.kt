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
//        val cachedSongsList = SimpleDataStoreCache.songsMap[MediaType.SONG]
//        d("cached", cachedSongsList.toString())
//        return if (!cachedSongsList.isNullOrEmpty()) {
//            cachedSongsList
//        } else {
//    }
        val songsList: List<Song> = mediaStoreLoader.initializeListIfNeeded(
            context = context,
            typeOfMedia = MediaType.SONG
        )
//            SimpleDataStoreCache.songsMap[MediaType.SONG] = songsList
        return songsList
    }

    override suspend fun getAllSongsFromAlbum(albumId: Long?): List<Song> {
//        val cachedAlbumSongsList = SimpleDataStoreCache.albumSongsMap[albumId]
//        return if (!cachedAlbumSongsList.isNullOrEmpty()) {
//            cachedAlbumSongsList
//        } else {
//        }
        val albumSongsList: List<Song> = mediaStoreLoader.initializeListIfNeeded(
            context = context,
            typeOfMedia = MediaType.ALBUM_SONGS, albumId = albumId
        )
//            SimpleDataStoreCache.albumSongsMap[albumId] = albumSongsList
        return albumSongsList
    }

    override suspend fun getAllAlbums(): List<Album> {

        val cachedAlbumsList = SimpleDataStoreCache.albumMap[MediaType.ALBUM]
        return if (!cachedAlbumsList.isNullOrEmpty()) {
            cachedAlbumsList
        } else {
            val albumsList: List<Album> = mediaStoreLoader.initializeListIfNeeded(
                context = context,
                typeOfMedia = MediaType.ALBUM
            )
            SimpleDataStoreCache.albumMap[MediaType.ALBUM] = albumsList
            albumsList
        }
    }
}