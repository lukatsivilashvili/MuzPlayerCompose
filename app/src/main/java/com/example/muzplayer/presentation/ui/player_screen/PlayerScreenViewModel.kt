package com.example.muzplayer.presentation.ui.player_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.muzplayer.common.Constants.UPDATE_PLAYER_POSITION_INTERVAL
import com.example.muzplayer.common.extensions.currentPlaybackPosition
import com.example.muzplayer.common.extensions.toSong
import com.example.muzplayer.domain.exoplayer.MusicServiceConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PlayerScreenViewModel @Inject constructor(
    musicServiceConnection: MusicServiceConnection
) : ViewModel() {

    private val playbackState = musicServiceConnection.playbackState

    var currentPlaybackPosition by mutableStateOf(0L)

    val currentPlayerPosition: Float
        get() {
            if (currentSongDuration > 0) {
                return currentPlaybackPosition.toFloat() / currentSongDuration
            }
            return 0f
        }

    val currentPlaybackFormattedPosition: String
        get() = formatLong(currentPlaybackPosition)

    val currentSongFormattedPosition: String
        get() = formatLong(currentSongDuration)


    val song = musicServiceConnection.currentPlayingSong.value
    val currentSongDuration: Long = song?.toSong()?.duration ?: 0L

    suspend fun updateCurrentPlaybackPosition() {
        val currentPosition = playbackState.value?.currentPlaybackPosition
        if (currentPosition != null && currentPosition != currentPlaybackPosition) {
            currentPlaybackPosition = currentPosition
        }
        delay(UPDATE_PLAYER_POSITION_INTERVAL)
        updateCurrentPlaybackPosition()
    }

    private fun formatLong(value: Long): String {
        val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
        return dateFormat.format(value)
    }
}