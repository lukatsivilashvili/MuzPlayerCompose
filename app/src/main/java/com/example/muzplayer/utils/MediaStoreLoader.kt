package com.example.muzplayer.utils

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.example.muzplayer.models.Song

class MediaStoreLoader {
    val musicItems = ArrayList<Song>()

    private var initialized = false

    fun initializeListIfNeeded(context: Context): ArrayList<Song> {
        val collection =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
            } else {
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            }
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
            val titleColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val artistColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val title = cursor.getString(titleColumn)
                val artist = cursor.getString(artistColumn)
                val albumId = cursor.getLong(albumIdColumn).toString()
                val contentUri: Uri = ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    id
                )
                val uri = Uri.parse("content://media/external/audio/albumart")
                val artUri = Uri.withAppendedPath(uri, albumId).toString()

                musicItems.add(
                    Song(
                        mediaId = id.toString(),
                        title = title,
                        subtitle = artist,
                        songUrl = contentUri.toString(),
                        imageUrl = artUri
                    )
                )
            }
        }
        initialized = true
        return musicItems
    }
}