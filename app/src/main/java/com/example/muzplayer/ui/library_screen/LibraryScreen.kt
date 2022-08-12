package com.example.muzplayer.ui.library_screen


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.muzplayer.components.HomeBottomBar
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
            .background(MaterialTheme.colors.primary),
    ) {
        HomeContent(music = viewModel.mediaItems.value, viewModel = viewModel)
        HomeBottomBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
        )
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
            .padding(bottom = 64.dp)
            .background(MaterialTheme.colors.background)
    ) {
        when(music){
            is Resource.Success ->{
                items(music.data!!) { musicItem ->
                    MusicItem(music = musicItem, viewModel = viewModel)
                    Divider(color = Color.Black, thickness = 0.5.dp)
                }
            }
        }

    }
}