package com.example.muzplayer.ui.library_screen


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.muzplayer.MusicScreen
import com.example.muzplayer.components.MusicItem
import com.example.muzplayer.models.Song
import com.example.muzplayer.utils.Resource
import com.example.muzplayer.viewmodels.MainViewModel

@Composable
fun LibraryBody(
    viewModel: MainViewModel = hiltViewModel()
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(MusicScreen.Home.background),
        horizontalArrangement = Arrangement.Center
    ) {
        HomeContent(music = viewModel.mediaItems.value, viewModel = viewModel)
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
            .background(MaterialTheme.colors.background)
    ) {
        when(music){
            is Resource.Success ->{
                items(music.data!!) { musicItem ->
                    MusicItem(music = musicItem, viewModel = viewModel)
                }
            }
        }

    }
}