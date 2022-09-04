package com.example.muzplayer.viewmodels

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.muzplayer.exoplayer.MusicServiceConnection
import com.example.muzplayer.extensions.isPlayEnabled
import com.example.muzplayer.extensions.isPlaying
import com.example.muzplayer.extensions.isPrepared
import com.example.muzplayer.models.Song
import com.example.muzplayer.models.SongState
import com.example.muzplayer.repository.GetSongsUseCase
import com.example.muzplayer.utils.Constants.MEDIA_ROOT_ID
import com.example.muzplayer.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val musicServiceConnection: MusicServiceConnection,
    private val getSongsUseCase: GetSongsUseCase
) : ViewModel() {

    private var _songsState = mutableStateOf(SongState())
    val state: State<SongState> = _songsState

    var showPlayerFullScreen by mutableStateOf(false)

    private val currentPlayingSong = musicServiceConnection.currentPlayingSong

    private val playbackState = musicServiceConnection.playbackState

    init {
        fetchSongs()
    }

    private fun fetchSongs() {
        getSongsUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _songsState.value = SongState(songs = result.data ?: emptyList())
                }

                is Resource.Error -> {
                    _songsState.value =
                        SongState(error = result.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    _songsState.value = SongState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
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