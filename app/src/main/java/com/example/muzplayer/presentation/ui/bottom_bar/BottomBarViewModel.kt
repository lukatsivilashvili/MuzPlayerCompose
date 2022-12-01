package com.example.muzplayer.presentation.ui.bottom_bar

import android.os.CountDownTimer
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat.SHUFFLE_MODE_ALL
import android.support.v4.media.session.PlaybackStateCompat.SHUFFLE_MODE_NONE
import android.util.Log.d
import androidx.compose.runtime.mutableStateOf
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.system.exitProcess

@HiltViewModel
class BottomBarViewModel @Inject constructor(
    private val musicServiceConnection: MusicServiceConnection
) : ViewModel() {

    private val testPair = MutableStateFlow(Triple<Int?, Boolean?, Song?>(0, false, null))

    val currentPlayingSongScreen: MutableStateFlow<MediaMetadataCompat?> =
        musicServiceConnection.currentPlayingSong
    private val currentSongFlow: MutableStateFlow<MediaMetadataCompat?> =
        musicServiceConnection.currentPlayingSong
    private val songEndFLow: MutableStateFlow<Int> = musicServiceConnection.songEndCounter

    private val testFlow: MutableStateFlow<Triple<Int?, Boolean?, Song?>> = testPair

    var currentPlayingSong: MediaMetadataCompat? = null
    val playbackState = musicServiceConnection.playbackState
    var testTriple: Triple<Int?, Boolean?, Song?>? = null
    var songEndNums: Int? = null


    val shouldWaitTillEndFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private var shouldWaitTillEndValue: Boolean? = false

    private var countDownTimer: CountDownTimer? = null

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

        viewModelScope.launch(Dispatchers.IO) {
            launch {
                currentSongFlow.collect {
                    currentPlayingSong = it
                }
            }
            launch {
                songEndFLow.collect {
                    songEndNums = it
                    if (testTriple != null) {
                        if (testTriple?.second == true && testTriple?.first != songEndNums) {
                            testTriple?.third?.let { it1 ->
                                playOrToggleSong(
                                    mediaItem = it1,
                                    toggle = true
                                )
                            }
                            delay(1000)
                            exitProcess(1)
                        }
                    }
                }
            }

            launch {
                testFlow.collect {
                    testTriple = it
                }
            }
        }
    }

    fun setWaitTillEndValue() {
        viewModelScope.launch {
            shouldWaitTillEndFlow.collect {
                shouldWaitTillEndValue = it
            }
        }
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

    private fun handleTimerFinishAction(song: Song, currNum: Int?) {
        if (shouldWaitTillEndValue != null && !shouldWaitTillEndValue!!) {
            playOrToggleSong(mediaItem = song, toggle = true)
            exitProcess(1)
        } else {
            testPair.value = Triple(currNum, shouldWaitTillEndValue, song)
        }
    }

    fun setTimer(sleepTime: Int, song: Song) {
        var currentEndNum = songEndNums
        d("ntag", currentEndNum.toString())

        countDownTimer = object : CountDownTimer(sleepTime * 60000.toLong(), 1000) {
            override fun onTick(p0: Long) {
                currentEndNum = songEndNums
            }

            override fun onFinish() {
                handleTimerFinishAction(song = song, currNum = currentEndNum)
            }
        }.start()


    }

    override fun onCleared() {
        super.onCleared()
        musicServiceConnection.unsubscribe(
            MEDIA_ROOT_ID,
            object : MediaBrowserCompat.SubscriptionCallback() {})
        if (countDownTimer != null) {
            countDownTimer?.cancel()
        }
    }
}