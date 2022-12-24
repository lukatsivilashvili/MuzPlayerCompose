package com.example.muzplayer.presentation.ui.library_screen


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.muzplayer.domain.MediaType
import com.example.muzplayer.domain.models.Album
import com.example.muzplayer.domain.models.Song
import com.example.muzplayer.presentation.components.album_item.AlbumItem
import com.example.muzplayer.presentation.components.music_item.MusicItem

@Composable
fun LibraryBody(
    viewModel: MainViewModel = hiltViewModel(),
    listState: LazyListState
) {
    val songs = viewModel.mediaItems.collectAsState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
    ) {
        HomeContent(data = songs.value, viewModel = viewModel, listState = listState, dataType = MediaType.SONG)
    }
}

@Composable
fun<T> HomeContent(
    data: List<T>,
    dataType: MediaType,
    viewModel: MainViewModel,
    listState: LazyListState
) {
    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when(dataType){
            MediaType.SONG -> {
                items(data) { musicItem ->
                    MusicItem(music = musicItem as Song, viewModel = viewModel)
                }
            }
            MediaType.ALBUM -> {
                items(data) { musicItem ->
                    AlbumItem(album = musicItem as Album)
                }
            }
        }


    }
}