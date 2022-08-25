package com.example.muzplayer.extensions

import android.content.Context
import android.os.Build
import android.support.v4.media.MediaMetadataCompat
import androidx.annotation.RequiresApi
import com.example.muzplayer.models.Song

@RequiresApi(Build.VERSION_CODES.Q)
fun MediaMetadataCompat.toSong(context: Context): Song? {
    return description?.let {
        Song(
            mediaId = it.mediaId ?: "",
            title = it.title.toString(),
            subtitle = it.subtitle.toString(),
            duration = it.description.toString().toLong(),
            songUrl = it.mediaUri.toString(),
            imageUrl = it.iconUri.toString(),
            hasArt = it.mediaUri.checkHasArt(context)
        )
    }
}