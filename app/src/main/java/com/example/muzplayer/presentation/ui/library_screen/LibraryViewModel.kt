package com.example.muzplayer.presentation.ui.library_screen

import android.support.v4.media.MediaBrowserCompat
import androidx.lifecycle.viewModelScope
import com.example.muzplayer.common.Constants.MEDIA_ROOT_ID
import com.example.muzplayer.common.base.BaseViewModel
import com.example.muzplayer.data.SimpleDataStoreCache
import com.example.muzplayer.domain.MediaType
import com.example.muzplayer.domain.exoplayer.MusicServiceConnection
import com.example.muzplayer.domain.exoplayer.MusicSource
import com.example.muzplayer.domain.models.Song
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val musicSource: MusicSource,
    private val musicServiceConnection: MusicServiceConnection
) : BaseViewModel(musicServiceConnection = musicServiceConnection) {

    var songItems = MutableStateFlow<List<Song>>(emptyList())


    init {
        loadLibraryContent()
    }

    private fun loadLibraryContent() = viewModelScope.launch(Dispatchers.IO) {
        fetchSongs()
    }

    private suspend fun fetchSongs() {

        val cachedSongsList = SimpleDataStoreCache.songsMap[MediaType.SONG]
        if (!cachedSongsList.isNullOrEmpty()){
            songItems.value = cachedSongsList
        }else{
            val allSongs = musicSource.fetchSongData()
            songItems.value = allSongs
            SimpleDataStoreCache.songsMap[MediaType.SONG] = allSongs
        }

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