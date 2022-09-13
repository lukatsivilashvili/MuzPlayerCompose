package com.example.muzplayer.domain.exoplayer.callbacks

import com.example.muzplayer.domain.exoplayer.MusicService
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
}