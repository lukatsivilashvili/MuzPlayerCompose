package com.example.muzplayer.presentation.ui.album_songs_screen

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
 * Created by lukatsivilashvili on 25.01.23 1:13 PM.
 */
@HiltViewModel
class AlbumSongsScreenViewModel @Inject constructor(
    private val musicSource: MusicSource,
    private val musicServiceConnection: MusicServiceConnection
    ) :
    BaseViewModel(musicServiceConnection = musicServiceConnection) {

    var albumSongItems = MutableStateFlow<List<Song>>(emptyList())

    fun loadAlbumSongs(albumId: Long) = viewModelScope.launch(Dispatchers.IO) {
        fetchAlbumSongs(albumId)
    }

    private suspend fun fetchAlbumSongs(albumId: Long) {
        val allAlbumSongs = musicSource.fetchSongsFromAlbum(albumId)
        albumSongItems.value = allAlbumSongs
    }
}