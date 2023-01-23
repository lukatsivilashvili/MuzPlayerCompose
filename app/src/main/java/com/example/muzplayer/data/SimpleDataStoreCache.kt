package com.example.muzplayer.data

import com.example.muzplayer.domain.MediaType
import com.example.muzplayer.domain.models.Album
import com.example.muzplayer.domain.models.Song

/**
 * Created by lukatsivilashvili on 23.01.23 4:31 PM.
 */
object SimpleDataStoreCache {
    val songsMap = mutableMapOf<MediaType, List<Song>>()

    val albumMap = mutableMapOf<MediaType, List<Album>>()
}