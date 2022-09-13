package com.example.muzplayer.presentation.ui.library_screen

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.muzplayer.domain.exoplayer.MusicServiceConnection
import com.example.muzplayer.domain.exoplayer.MusicSource
import com.example.muzplayer.common.extensions.isPlayEnabled
import com.example.muzplayer.common.extensions.isPlaying
import com.example.muzplayer.common.extensions.isPrepared
import com.example.muzplayer.domain.models.Song
import com.example.muzplayer.common.Constants.MEDIA_ROOT_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val musicServiceConnection: MusicServiceConnection,
    private val musicSource: MusicSource
) : ViewModel() {

    var mediaItems = MutableStateFlow<List<Song>>(emptyList())

    var showPlayerFullScreen by mutableStateOf(false)

    val currentPlayingSong = musicServiceConnection.currentPlayingSong

    val playbackState = musicServiceConnection.playbackState

    init {
        loadLibraryContent()
    }

    private fun loadLibraryContent() = viewModelScope.launch(Dispatchers.IO){
        fetchSongs()
    }
    private suspend fun fetchSongs() {
        val allSongs = musicSource.fetchSongData()
        Log.d("items", allSongs.toString())
        mediaItems.value = allSongs
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