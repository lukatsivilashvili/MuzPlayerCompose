package com.example.muzplayer.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Build
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.util.Size
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.muzplayer.exoplayer.MusicServiceConnection
import com.example.muzplayer.exoplayer.isPlayEnabled
import com.example.muzplayer.exoplayer.isPlaying
import com.example.muzplayer.exoplayer.isPrepared
import com.example.muzplayer.models.Song
import com.example.muzplayer.utils.Constants.MEDIA_ROOT_ID
import com.example.muzplayer.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.FileNotFoundException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val musicServiceConnection: MusicServiceConnection,
    @SuppressLint("StaticFieldLeak") private val context: Context
) : ViewModel() {

    var mediaItems = mutableStateOf<Resource<List<Song>>>(Resource.Loading(null))

    var showPlayerFullScreen by mutableStateOf(false)

    val currentPlayingSong = musicServiceConnection.currentPlayingSong

    val playbackState = musicServiceConnection.playbackState

    init {
        mediaItems.value = (Resource.Loading(null))
        musicServiceConnection.subscribe(
            MEDIA_ROOT_ID,
            object : MediaBrowserCompat.SubscriptionCallback() {
                @RequiresApi(Build.VERSION_CODES.Q)
                override fun onChildrenLoaded(
                    parentId: String,
                    children: MutableList<MediaBrowserCompat.MediaItem>
                ) {
                    super.onChildrenLoaded(parentId, children)
                    val items = children.map {
                        Song(
                            it.mediaId!!,
                            it.description.title.toString(),
                            it.description.subtitle.toString(),
                            it.description.mediaUri.toString(),
                            it.description.iconUri.toString(),
                            hasArt = checkHasArt(it.description.mediaUri)
                        )
                    }
                    mediaItems.value = Resource.Success(items)
                }
            })
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun checkHasArt(artUri: Uri?): Boolean {
        var hasArt = true
        try {
            if (artUri != null) {
                context.contentResolver.loadThumbnail(artUri, Size(64, 64), null)
            }
        } catch (_: FileNotFoundException) {
            hasArt = false
        }
        return hasArt
    }


    fun skipToNextSong() {
        musicServiceConnection.transportController.skipToNext()
    }

    fun skipToPreviousSong() {
        musicServiceConnection.transportController.skipToPrevious()
    }

    fun seekTo(pos: Float) {
        musicServiceConnection.transportController.seekTo(pos.toLong())
    }

    fun playOrToggleSong(mediaItem: Song, toggle: Boolean = false) {
        val isPrepared = playbackState.value?.isPrepared ?: false
        if (isPrepared && mediaItem.mediaId ==
            currentPlayingSong.value?.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID)
        ) {
            playbackState.value?.let { playbackState ->
                when {
                    playbackState.isPlaying -> {
                        if (toggle) musicServiceConnection.transportController.pause()
                    }
                    playbackState.isPlayEnabled -> {
                        musicServiceConnection.transportController.play()
                    }
                    else -> Unit
                }
            }
        } else {
            musicServiceConnection.transportController.playFromMediaId(mediaItem.mediaId, null)
        }
    }

    override fun onCleared() {
        super.onCleared()
        musicServiceConnection.unsubscribe(
            MEDIA_ROOT_ID,
            object : MediaBrowserCompat.SubscriptionCallback() {})
    }
}