package com.example.muzplayer.domain.models

import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import androidx.core.database.getStringOrNull
import com.example.muzplayer.common.extensions.getColumnValue
import com.example.muzplayer.common.extensions.getColumnValueNullable

/**
 * Created by lukatsivilashvili on 22.12.22 4:37 PM.
 */
data class Album(
    val albumId: Long = 0,
    val artist: String = "",
    val title: String = "",
    val imageUrl: String = "",
    val songCount: Int = 0
){
    fun createAlbumArtUri(): String {
        val uri = Uri.parse("content://media/external/audio/albumart")
        return Uri.withAppendedPath(uri, albumId.toString()).toString()
    }
    companion object {
        fun fromCursor(cursor: Cursor): Album {

            return Album(
                albumId = cursor.getColumnValue(MediaStore.Audio.AlbumColumns.ALBUM_ID) {
                    cursor.getLong(it)
                },
                title = cursor.getColumnValue(MediaStore.Audio.AlbumColumns.ALBUM) {
                    cursor.getString(it)
                },
                artist = cursor.getColumnValueNullable(MediaStore.Audio.AlbumColumns.ARTIST) {
                    cursor.getStringOrNull(it)
                } ?: "",
                songCount = cursor.getColumnValue(MediaStore.Audio.AlbumColumns.NUMBER_OF_SONGS) {
                    cursor.getInt(it)
                },
                imageUrl = Uri.withAppendedPath(/* baseUri = */ Uri.parse("content://media/external/audio/albumart"),
                    cursor.getColumnValue(MediaStore.Audio.AlbumColumns.ALBUM_ID) {
                        cursor.getLong(it)
                    }.toString()
                ).toString()
            )
        }
    }
}
