package com.example.muzplayer.ui.library_screen


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.muzplayer.components.MusicItem
import com.example.muzplayer.models.Song
import com.example.muzplayer.utils.Resource
import com.example.muzplayer.viewmodels.MainViewModel

@Composable
fun LibraryBody(
    viewModel: MainViewModel = hiltViewModel()
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
    ) {
        val songList = viewModel.mediaItems.observeAsState().value
        if (viewModel.mediaItems.value == Resource.Loading(null)) {
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
        } else {
            if (songList != null) {
                HomeContent(music = songList, viewModel = viewModel)
            }
        }
    }
}

@Composable
fun HomeContent(
    music: Resource<List<Song>>,
    viewModel: MainViewModel
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when (music) {
            is Resource.Success -> {
                items(music.data!!) { musicItem ->
                    MusicItem(music = musicItem, viewModel = viewModel)
                }
            }
            else -> {}
        }

    }
}