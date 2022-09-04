package com.example.muzplayer.viewmodels

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.util.Log.d
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.muzplayer.exoplayer.MusicServiceConnection
import com.example.muzplayer.exoplayer.MusicSource
import com.example.muzplayer.extensions.isPlayEnabled
import com.example.muzplayer.extensions.isPlaying
import com.example.muzplayer.extensions.isPrepared
import com.example.muzplayer.models.Song
import com.example.muzplayer.utils.Constants.MEDIA_ROOT_ID
import com.example.muzplayer.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val musicServiceConnection: MusicServiceConnection,
    private val musicSource: MusicSource
) : ViewModel() {

    private var _mediaItems = MutableLiveData<Resource<List<Song>>>()
    var mediaItems: LiveData<Resource<List<Song>>> = _mediaItems

    var showPlayerFullScreen by mutableStateOf(false)

    private val currentPlayingSong = musicServiceConnection.currentPlayingSong

    private val playbackState = musicServiceConnection.playbackState

    init {
        loadLibraryContent()
    }

    private fun loadLibraryContent() = viewModelScope.launch(Dispatchers.IO){
        fetchSongs()
    }
    private suspend fun fetchSongs() {
        val allSongs = musicSource.fetchSongData()
        d("items", allSongs.toString())
        _mediaItems.postValue(Resource.Success(allSongs))
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