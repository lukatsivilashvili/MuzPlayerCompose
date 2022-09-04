package com.example.muzplayer.ui.library_screen


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.muzplayer.components.MusicItem
import com.example.muzplayer.models.Song
import com.example.muzplayer.viewmodels.MainViewModel

@Composable
fun LibraryBody(
    viewModel: MainViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
    ) {
        HomeContent(music = state.songs, viewModel = viewModel)
        if (state.error.isNotBlank()) {
            Text(
                text = state.error,
                color = androidx.compose.material.MaterialTheme.colors.error,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .align(Alignment.Center)
            )
        }
        if (state.isLoading) {
            CircularProgressIndicator(
                color = androidx.compose.material.MaterialTheme.colors.onBackground,
                modifier = Modifier
                    .size(100.dp)
                    .fillMaxSize()

                    .align(Alignment.Center)
                    .padding(
                        top = 16.dp,
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 16.dp
                    )
            )
        }
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