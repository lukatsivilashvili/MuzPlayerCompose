package com.example.muzplayer.presentation.ui.bottom_bar

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat.SHUFFLE_MODE_ALL
import android.support.v4.media.session.PlaybackStateCompat.SHUFFLE_MODE_NONE
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.muzplayer.common.Constants.MEDIA_ROOT_ID
import com.example.muzplayer.common.extensions.isPlayEnabled
import com.example.muzplayer.common.extensions.isPlaying
import com.example.muzplayer.common.extensions.isPrepared
import com.example.muzplayer.domain.exoplayer.MusicServiceConnection
import com.example.muzplayer.domain.models.Song
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BottomBarViewModel @Inject constructor(
    private val musicServiceConnection: MusicServiceConnection
) : ViewModel() {

    val currentPlayingSong = musicServiceConnection.currentPlayingSong
    val playbackState = musicServiceConnection.playbackState

    var shuffleStates = mutableStateOf(1)

    init {
        musicServiceConnection.subscribe(
            MEDIA_ROOT_ID,
            object : MediaBrowserCompat.SubscriptionCallback() {
                override fun onChildrenLoaded(
                    parentId: String,
                    children: MutableList<MediaBrowserCompat.MediaItem>
                ) {
                    super.onChildrenLoaded(parentId, children)
                }
            })
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

    fun shufflePlaylist(shuffleState: Int) {
        when (shuffleState) {
            0 -> {
                musicServiceConnection.transportController.setShuffleMode(SHUFFLE_MODE_NONE)
                shuffleStates.value = 1
            }

            1 -> {
                musicServiceConnection.transportController.setShuffleMode(SHUFFLE_MODE_ALL)
                shuffleStates.value = 0
            }
        }
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