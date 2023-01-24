package com.example.muzplayer.presentation.ui.album_screen

import android.support.v4.media.MediaBrowserCompat
import androidx.lifecycle.viewModelScope
import com.example.muzplayer.common.Constants.MEDIA_ROOT_ID
import com.example.muzplayer.common.base.BaseViewModel
import com.example.muzplayer.domain.exoplayer.MusicServiceConnection
import com.example.muzplayer.domain.exoplayer.MusicSource
import com.example.muzplayer.domain.models.Album
import com.example.muzplayer.domain.models.Song
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumScreenViewModel @Inject constructor(
    private val musicSource: MusicSource,
    private val musicServiceConnection: MusicServiceConnection
) : BaseViewModel(musicServiceConnection = musicServiceConnection) {

    var albumItems = MutableStateFlow<List<Album>>(emptyList())


    init {
        loadAlbumsContent()
    }

    private fun loadAlbumsContent() = viewModelScope.launch(Dispatchers.IO) {
        fetchAlbums()
    }


    private suspend fun fetchAlbums() {
        val allSongs = musicSource.fetchAlbumData()
        albumItems.value = allSongs
    }

    fun searchSong(songsList: List<Song>, query: String): Int {
        var index = 0
        viewModelScope.launch {
            for (i in songsList) {
                if (i.title.contains(other = query, ignoreCase = true)) {
                    index = songsList.indexOf(i)
                    break
                }
            }
        }
        return index
    }

    override fun onCleared() {
        super.onCleared()
        musicServiceConnection.unsubscribe(
            MEDIA_ROOT_ID,
            object : MediaBrowserCompat.SubscriptionCallback() {})
    }
}