package com.example.muzplayer.domain.exoplayer

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.MediaMetadataCompat.Builder
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_ARTIST
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_DISPLAY_DESCRIPTION
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_DURATION
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_MEDIA_ID
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_MEDIA_URI
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_TITLE
import android.util.Log.d
import com.example.muzplayer.data.repository.MediaStoreRepositoryImpl
import com.example.muzplayer.domain.MediaType
import com.example.muzplayer.domain.exoplayer.State.STATE_CREATED
import com.example.muzplayer.domain.exoplayer.State.STATE_ERROR
import com.example.muzplayer.domain.exoplayer.State.STATE_INITIALIZED
import com.example.muzplayer.domain.exoplayer.State.STATE_INITIALIZING
import com.example.muzplayer.domain.models.Album
import com.example.muzplayer.domain.models.Song
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MusicSource @Inject constructor(
    private val repository: MediaStoreRepositoryImpl
) {

    var songs = emptyList<MediaMetadataCompat>()

    suspend fun fetchMediaData(mediaType: MediaType = MediaType.SONG, albumId: Long? = null) =
        withContext(Dispatchers.Main) {
            state = STATE_INITIALIZING
            when (mediaType) {
                MediaType.SONG -> {
                    val allSongs = repository.getAllSongs()
                    songs = allSongs.map { song ->
                        Builder()
                            .putString(METADATA_KEY_TITLE, song.title)
                            .putString(METADATA_KEY_DISPLAY_TITLE, song.title)
                            .putString(METADATA_KEY_ARTIST, song.artist)
                            .putString(METADATA_KEY_DISPLAY_SUBTITLE, song.artist)
                            .putString(METADATA_KEY_MEDIA_ID, song.mediaId)
                            .putString(METADATA_KEY_MEDIA_URI, song.songUrl)
                            .putLong(METADATA_KEY_DURATION, song.duration)
                            .putString(
                                METADATA_KEY_DISPLAY_ICON_URI,
                                if (song.hasArt) song.imageUrl else null
                            )
                            .putString(
                                METADATA_KEY_ALBUM_ART_URI,
                                if (song.hasArt) song.imageUrl else null
                            )
                            .putString(METADATA_KEY_DISPLAY_DESCRIPTION, song.duration.toString())
                            .build()
                    }
                }

                MediaType.ALBUM_SONGS -> {
                    repository.getAllSongsFromAlbum(albumId = albumId)
                }

                MediaType.ALBUM -> TODO()
            }
            state = STATE_INITIALIZED
        }

    suspend fun fetchSongData(): List<Song> {
        state = STATE_INITIALIZING
        val allSongs = repository.getAllSongs()
        state = STATE_INITIALIZED
        return allSongs
    }

    suspend fun fetchAlbumData(): List<Album> {
        state = STATE_INITIALIZING
        val allAlbums = repository.getAllAlbums()
        state = STATE_INITIALIZED
        return allAlbums
    }

    suspend fun fetchSongsFromAlbum(albumId: Long): List<Song> {
        state = STATE_INITIALIZING
        val albumSongs = repository.getAllSongsFromAlbum(albumId = albumId)
        state = STATE_INITIALIZED
        return albumSongs
    }

    fun asMediaSource(dataSourceFactory: DefaultDataSource.Factory): ConcatenatingMediaSource {
        val concatenatingMediaSource = ConcatenatingMediaSource()
        songs.forEach { song ->
            d("myRes", song.description.toString())
            val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(song.getString(METADATA_KEY_MEDIA_URI)))
            concatenatingMediaSource.addMediaSource(mediaSource)
        }
        return concatenatingMediaSource
    }

    fun asMediaItems() = songs.map { song ->
        val desc = MediaDescriptionCompat.Builder()
            .setMediaUri(song.description.mediaUri)
            .setTitle(song.description.title)
            .setSubtitle(song.description.subtitle)
            .setMediaId(song.description.mediaId)
            .setIconUri(song.description.iconUri)
            .setDescription(song.getLong(METADATA_KEY_DURATION).toString())
            .build()
        MediaBrowserCompat.MediaItem(desc, FLAG_PLAYABLE)
    }.toMutableList()

    private val onReadyListeners = mutableListOf<(Boolean) -> Unit>()

    private var state: State = STATE_CREATED
        set(value) {
            if (value == STATE_INITIALIZED || value == STATE_ERROR) {
                synchronized(onReadyListeners) {
                    field = value
                    onReadyListeners.forEach { listener ->
                        listener(state == STATE_INITIALIZED)
                    }
                }
            }
        }

    fun whenReady(action: (Boolean) -> Unit): Boolean {
        return if (state == STATE_CREATED || state == STATE_INITIALIZING) {
            onReadyListeners += action
            false
        } else {
            action(state == STATE_INITIALIZED)
            true
        }
    }
}

enum class State {
    STATE_CREATED,
    STATE_INITIALIZING,
    STATE_INITIALIZED,
    STATE_ERROR
}