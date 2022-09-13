package com.example.muzplayer.common.extensions

import android.content.Context
import android.support.v4.media.MediaMetadataCompat
import com.example.muzplayer.domain.models.Song

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