package com.example.muzplayer.presentation.ui.album_tracks_screen

import androidx.lifecycle.viewModelScope
import com.example.muzplayer.common.base.BaseViewModel
import com.example.muzplayer.domain.exoplayer.MusicServiceConnection
import com.example.muzplayer.domain.exoplayer.MusicSource
import com.example.muzplayer.domain.models.Song
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by lukatsivilashvili on 04.02.23 4:54 PM.
 */

@HiltViewModel
class AlbumTracksScreenViewModel @Inject constructor(
    musicServiceConnection: MusicServiceConnection,
    private val musicSource: MusicSource
) : BaseViewModel(musicServiceConnection = musicServiceConnection) {

    var albumTracksFlow = MutableStateFlow<List<Song>>(emptyList())


    fun loadAlbumTracksById(albumId: Long) = viewModelScope.launch(Dispatchers.IO) {
        fetchAlbumTracksById(albumId = albumId)
    }

    private suspend fun fetchAlbumTracksById(albumId: Long) {
        val albumTracks = musicSource.fetchAlbumTracksById(albumId = albumId)
        albumTracksFlow.value = albumTracks

    }
}