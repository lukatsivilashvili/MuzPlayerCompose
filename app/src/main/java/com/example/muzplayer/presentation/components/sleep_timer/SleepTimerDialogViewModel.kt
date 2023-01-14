package com.example.muzplayer.presentation.components.sleep_timer

import com.example.muzplayer.common.base.BaseViewModel
import com.example.muzplayer.domain.exoplayer.MusicServiceConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SleepTimerDialogViewModel @Inject constructor(
    private val musicServiceConnection: MusicServiceConnection
) :
    BaseViewModel(musicServiceConnection = musicServiceConnection) {
}