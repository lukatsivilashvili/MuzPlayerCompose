package com.example.muzplayer.presentation.ui.bottom_bar

import android.os.CountDownTimer
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat.SHUFFLE_MODE_ALL
import android.support.v4.media.session.PlaybackStateCompat.SHUFFLE_MODE_NONE
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.muzplayer.common.Constants.MEDIA_ROOT_ID
import com.example.muzplayer.common.base.BaseViewModel
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
) : BaseViewModel(musicServiceConnection = musicServiceConnection) {


    val currentPlayingSongScreen: MutableStateFlow<MediaMetadataCompat?> =
        musicServiceConnection.currentPlayingSong

    private val currentSongFlow: MutableStateFlow<MediaMetadataCompat?> =
        musicServiceConnection.currentPlayingSong

    private val songEndFLow: MutableStateFlow<Int> = musicServiceConnection.songEndCounter

    private val shouldStopTriple = MutableStateFlow(Triple<Int?, Boolean?, Song?>(0, false, null))
    private val shouldStopFlow: MutableStateFlow<Triple<Int?, Boolean?, Song?>> = shouldStopTriple

    var shouldStopTripleValue: Triple<Int?, Boolean?, Song?>? = null
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
                    if (shouldStopTripleValue != null) {
                        if (shouldStopTripleValue?.second == true && shouldStopTripleValue?.first != songEndNums) {
                            shouldStopTripleValue?.third?.let { it1 ->
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
                shouldStopFlow.collect {
                    shouldStopTripleValue = it
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


    private fun handleTimerFinishAction(song: Song, currNum: Int?) {
        if (shouldWaitTillEndValue != null && !shouldWaitTillEndValue!!) {
            playOrToggleSong(mediaItem = song, toggle = true)
            exitProcess(1)
        } else {
            shouldStopTriple.value = Triple(currNum, shouldWaitTillEndValue, song)
        }
    }

    fun setTimer(sleepTime: Int, song: Song) {
        var currentEndNum = songEndNums

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