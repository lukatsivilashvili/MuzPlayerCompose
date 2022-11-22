package com.example.muzplayer.presentation.ui.bottom_bar

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat.SHUFFLE_MODE_ALL
import android.support.v4.media.session.PlaybackStateCompat.SHUFFLE_MODE_NONE
import android.util.Log.d
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.muzplayer.common.Constants.MEDIA_ROOT_ID
import com.example.muzplayer.common.extensions.isPlayEnabled
import com.example.muzplayer.common.extensions.isPlaying
import com.example.muzplayer.common.extensions.isPrepared
import com.example.muzplayer.domain.exoplayer.MusicServiceConnection
import com.example.muzplayer.domain.models.Song
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject
import kotlin.system.exitProcess

@HiltViewModel
class BottomBarViewModel @Inject constructor(
    private val musicServiceConnection: MusicServiceConnection
) : ViewModel() {

    val currentPlayingSongScreen: MutableStateFlow<MediaMetadataCompat?> = musicServiceConnection.currentPlayingSong
    val currentSongFlow: MutableStateFlow<MediaMetadataCompat?> = musicServiceConnection.currentPlayingSong

    var currentPlayingSong: MediaMetadataCompat? = null
    val playbackState = musicServiceConnection.playbackState

    val _selectedTime = MutableLiveData<Int?>()
    private val selectedTime: LiveData<Int?> = _selectedTime
    private var selectedTimeValue: Int? = 0

    val _shouldWaitTillEnd = MutableLiveData<Boolean?>()
    private val shouldWaitTillEnd: LiveData<Boolean?> = _shouldWaitTillEnd
    private var shouldWaitTillEndValue: Boolean? = false




    var shuffleStates = mutableStateOf(false)

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

        viewModelScope.launch {
            currentSongFlow.collect{
                currentPlayingSong = it
            }
        }

//
//        shouldWaitTillEnd.observeForever {
//            shouldWaitTillEndValue = it
//        }
//
//        selectedTime.observeForever {
//            selectedTimeValue = it
//        }
//
//        currentPlayingSong.observeForever { song ->
//            d("myTag", song?.toSong().toString())
//            if (song != null) {
//                selectedTimeValue?.let { setTimer(song = song.toSong()!!, sleepTime = it) }
//
//            }
//        }

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

    fun shufflePlaylist(shuffleState: Boolean) {
        when (shuffleState) {
            true -> {
                musicServiceConnection.transportController.setShuffleMode(SHUFFLE_MODE_NONE)
                shuffleStates.value = false
            }

            false -> {
                musicServiceConnection.transportController.setShuffleMode(SHUFFLE_MODE_ALL)
                shuffleStates.value = true
            }
        }
    }

    fun playOrToggleSong(mediaItem: Song, toggle: Boolean = false) {
        val isPrepared = playbackState.value?.isPrepared ?: false
        if (isPrepared && mediaItem.mediaId ==
            currentPlayingSong?.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID)
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

    fun setTimer(sleepTime: Int, song: Song) {

        viewModelScope.launch {
            withContext(Dispatchers.IO) {

                val timer = Timer()
                val pauseTask = object : TimerTask() {
                    override fun run() {
                        playOrToggleSong(mediaItem = song, toggle = true)
                    }
                }

                val killTask = object : TimerTask() {
                    override fun run() {
                        exitProcess(1)
                    }

                }

                timer.run {
                    if (song.duration <= sleepTime * 60000.toLong()) {
                        schedule(killTask, (sleepTime * 60000 + 1000).toLong())
                        d("myTag", "kill $sleepTime")
                    } else {
                        schedule(pauseTask, sleepTime * 60000.toLong())
                        d("myTag", "pause $sleepTime")
                        schedule(killTask, (sleepTime * 60000 + 1000).toLong())
                    }
                }
            }

        }


    }

    override fun onCleared() {
        super.onCleared()
        musicServiceConnection.unsubscribe(
            MEDIA_ROOT_ID,
            object : MediaBrowserCompat.SubscriptionCallback() {})
    }
}