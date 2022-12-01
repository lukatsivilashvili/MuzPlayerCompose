package com.example.muzplayer.common.extensions

import android.support.v4.media.MediaMetadataCompat
import com.example.muzplayer.domain.models.Song

fun MediaMetadataCompat.toSong(): Song {
    return Song(
        mediaId = description.mediaId ?: "",
        title = description.title.toString(),
        subtitle = description.subtitle.toString(),
        duration = description.description.toString().toLong(),
        songUrl = description.mediaUri.toString(),
        imageUrl = description.iconUri.toString()
    )
}