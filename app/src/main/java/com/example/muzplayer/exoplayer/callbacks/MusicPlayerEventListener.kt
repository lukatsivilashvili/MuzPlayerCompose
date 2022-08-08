package com.example.muzplayer.exoplayer.callbacks

import android.widget.Toast
import com.example.muzplayer.exoplayer.MusicService
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.Player
import javax.inject.Inject

class MusicPlayerEventListener @Inject constructor(
    private val musicService: MusicService
) : Player.Listener {

    override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
        super.onPlayWhenReadyChanged(playWhenReady, reason)
        if (reason == Player.STATE_READY && !playWhenReady) {
            musicService.stopForeground(false)
        }
    }

    override fun onPlayerError(error: ExoPlaybackException) {
        super.onPlayerError(error)
        Toast.makeText(musicService, "An unknown error", Toast.LENGTH_LONG).show()
    }
}