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
import com.example.muzplayer.domain.models.Song
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
        HomeContent(music = songs.value, viewModel = viewModel, listState = listState)
    }
}

@Composable
fun HomeContent(
    music: List<Song>,
    viewModel: MainViewModel,
    listState: LazyListState
) {
    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        items(music) { musicItem ->
            MusicItem(music = musicItem, viewModel = viewModel)
        }

    }
}