package com.example.muzplayer.data

import android.content.Context
import android.provider.MediaStore
import android.util.Log.d
import com.example.muzplayer.domain.MediaType
import com.example.muzplayer.domain.models.Album
import com.example.muzplayer.domain.models.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object MediaStoreLoader {
    private val musicItems = mutableListOf<Song>()
    private var albumItems = listOf<Album>()
    private val albumSet = mutableSetOf<Album>()

    private var initialized = false

    suspend fun <T> initializeListIfNeeded(
        context: Context,
        typeOfMedia: MediaType,
        albumId: Long? = null
    ): List<T> {
        return when (typeOfMedia) {
            MediaType.SONG -> initializeSongList(context) as List<T>
            MediaType.ALBUM -> initializeAlbumList(context) as List<T>
            MediaType.ALBUM_SONGS -> initializeSongListWithAlbumId(context, albumId) as List<T>
        }
    }

    private suspend fun initializeSongList(context: Context): List<Song> {
        musicItems.clear()
        withContext(Dispatchers.IO) {
            val collection = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
            val selection = MediaStore.Audio.Media.IS_MUSIC + " !=0"
            val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"
            val projection = arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ALBUM_ID
            )
            val query = context.contentResolver.query(
                collection,
                projection,
                selection,
                null,
                sortOrder,
                null
            )
            query?.use { cursor ->
                while (cursor.moveToNext()) {
                    musicItems.add(
                        Song.fromCursor(cursor)
                    )
                }
            }
            initialized = true
        }
        return musicItems
    }

    private suspend fun initializeSongListWithAlbumId(
        context: Context,
        albumId: Long?
    ): List<Song> {
        musicItems.clear()
        withContext(Dispatchers.IO) {
            val collection = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
            val selection = MediaStore.Audio.Media.IS_MUSIC + " !=0"
            val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"
            val projection = arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ALBUM_ID
            )
            val query = context.contentResolver.query(
                collection,
                projection,
                selection,
                null,
                sortOrder,
                null
            )
            query?.use { cursor ->
                val albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
                while (cursor.moveToNext()) {
                    val currAlbumId = cursor.getLong(albumIdColumn)
                    if (currAlbumId == albumId){
                        musicItems.add(
                            Song.fromCursor(cursor = cursor)
                        )
                    }
                }
            }
            initialized = true
        }
        return musicItems
    }

    private suspend fun initializeAlbumList(context: Context): List<Album> {
        musicItems.clear()
        withContext(Dispatchers.IO) {
            val collection = MediaStore.Audio.Albums.getContentUri(MediaStore.VOLUME_EXTERNAL)
            val sortOrder = "${MediaStore.Audio.Albums.ALBUM} ASC"
            val projection = arrayOf(
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Albums.ALBUM,
                MediaStore.Audio.Albums.ARTIST,
                MediaStore.Audio.Albums.NUMBER_OF_SONGS
            )
            val query = context.contentResolver.query(
                /* uri = */ collection,
                /* projection = */ projection,
                /* selection = */ null,
                /* selectionArgs = */ null,
                /* sortOrder = */ sortOrder,
                /* cancellationSignal = */ null
            )
            query?.use { cursor ->
                while (cursor.moveToNext()) {
                    albumSet.add(
                        Album.fromCursor(cursor = cursor)
                    )
                }
            }
            albumItems = albumSet.toList()
            initialized = true
        }
        return albumItems
    }
}