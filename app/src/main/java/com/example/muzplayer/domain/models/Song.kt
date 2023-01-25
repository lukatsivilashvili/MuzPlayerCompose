package com.example.muzplayer.domain.models

import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull
import com.example.muzplayer.common.extensions.getColumnValue
import com.example.muzplayer.common.extensions.getColumnValueNullable

data class Song(
    val mediaId: String = "",
    val title: String = "",
    val artist: String = "",
    val duration: Long = 0,
    val songUrl: String = "",
    val imageUrl: String = "",
    val hasArt: Boolean = true,
    val albumId: Long = 0
) {
    companion object {
        fun fromCursor(cursor: Cursor): Song {

            return Song(
                albumId = cursor.getColumnValue(MediaStore.Audio.Media.ALBUM_ID) {
                    cursor.getLong(it)
                },
                title = cursor.getColumnValue(MediaStore.Audio.Media.TITLE) {
                    cursor.getString(it)
                },
                artist = cursor.getColumnValueNullable(MediaStore.Audio.Media.ARTIST) {
                    cursor.getStringOrNull(it)
                } ?: "",
                duration = cursor.getColumnValueNullable(MediaStore.Audio.Media.DURATION) {
                    cursor.getLongOrNull(it)
                } ?: 0,
                songUrl = ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    cursor.getColumnValue(MediaStore.Audio.Media._ID) {
                        cursor.getLong(it)
                    }).toString(),
                mediaId = cursor.getColumnValue(MediaStore.Audio.Media._ID) {
                    cursor.getLong(it)
                }.toString(),
                imageUrl = Uri.withAppendedPath(/* baseUri = */ Uri.parse("content://media/external/audio/albumart"),
                    cursor.getColumnValue(MediaStore.Audio.Media.ALBUM_ID) {
                        cursor.getLong(it)
                    }.toString()
                ).toString()
            )
        }
    }
}
