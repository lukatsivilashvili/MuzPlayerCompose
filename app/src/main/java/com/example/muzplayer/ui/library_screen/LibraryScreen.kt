package com.example.muzplayer.ui.library_screen


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.muzplayer.components.MusicItem
import com.example.muzplayer.models.Song
import com.example.muzplayer.viewmodels.MainViewModel

@Composable
fun LibraryBody(
    viewModel: MainViewModel = hiltViewModel()
) {
    val songs = viewModel.mediaItems.collectAsState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
    ) {
        HomeContent(music = songs.value, viewModel = viewModel)
    }
}

@Composable
fun HomeContent(
    music: List<Song>,
    viewModel: MainViewModel
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        items(music) { musicItem ->
            MusicItem(music = musicItem, viewModel = viewModel)
        }

    }
}