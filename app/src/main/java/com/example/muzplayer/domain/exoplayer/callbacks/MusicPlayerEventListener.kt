package com.example.muzplayer.domain.exoplayer.callbacks

import com.example.muzplayer.domain.exoplayer.MusicService
import com.example.muzplayer.domain.exoplayer.MusicServiceConnection
import com.google.android.exoplayer2.Player
import javax.inject.Inject

class MusicPlayerEventListener @Inject constructor(
    private val musicService: MusicService,
    private val musicServiceConnection: MusicServiceConnection
) : Player.Listener {

    override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
        super.onPlayWhenReadyChanged(playWhenReady, reason)
        if (reason == Player.STATE_READY && !playWhenReady) {
            musicService.stopForeground(false)
        }
    }

    override fun onPositionDiscontinuity(
        oldPosition: Player.PositionInfo,
        newPosition: Player.PositionInfo,
        reason: Int
    ) {
        super.onPositionDiscontinuity(oldPosition, newPosition, reason)
        if (reason == Player.DISCONTINUITY_REASON_AUTO_TRANSITION) {
            musicServiceConnection.songEndCounter.value+=1
        }
    }
}