package com.example.muzplayer.presentation.ui.album_tracks_screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.muzplayer.domain.models.Album
import com.example.muzplayer.domain.models.Song
import com.example.muzplayer.presentation.components.CustomCoilImage
import com.example.muzplayer.presentation.components.music_item.MusicItem
import com.example.muzplayer.presentation.ui.theme.AppTheme.dimens

/**
 * Created by lukatsivilashvili on 25.01.23 11:18 AM.
 */

@Composable
fun AlbumTracksScreen(
    viewModel: AlbumTracksScreenViewModel = hiltViewModel(),
    navHostController: NavHostController
) {
    val trackList = viewModel.albumTracksFlow.collectAsState()
    val albumId = navHostController.previousBackStackEntry?.savedStateHandle?.get<Long>("albumId")
    LaunchedEffect(key1 = albumId) {
        if (albumId != null) {
            viewModel.loadAlbumTracksById(albumId)
        }
        Log.d("albobj", albumId.toString())
    }


    AlbumTracksScreenBody(
        album = Album(albumId = albumId ?: 0, artist = "Metro Boomin, Future, Don Toliver", title = "HEROES & VILLAINS", imageUrl = "content://media/external/audio/albumart/284497396809988859", songCount = 0),
        trackList = trackList.value,
        viewModel = viewModel
    )
}

@Composable
fun AlbumTracksScreenBody(
    album: Album?,
    modifier: Modifier = Modifier,
    trackList: List<Song>,
    viewModel: AlbumTracksScreenViewModel
) {
    val listState = rememberLazyListState()
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = modifier,
    ) {
        AlbumHeader(
            album = album,
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
        )

        AlbumTracksColumn(
            data = trackList,
            listState = listState,
            viewModel = viewModel,
            modifier = modifier.weight(2f)
        )
    }
}

@Composable
fun AlbumTracksColumn(
    data: List<Song>,
    viewModel: AlbumTracksScreenViewModel,
    listState: LazyListState? = null,
    modifier: Modifier = Modifier
) {
    if (listState != null) {
        LazyColumn(
            state = listState,
            modifier = modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            items(data) { musicItem ->
                MusicItem(music = musicItem, viewModel = viewModel)
            }
        }
    }
}


@Composable
private fun AlbumHeader(
    album: Album?,
    modifier: Modifier = Modifier,
) {
    Surface(
        color = MaterialTheme.colorScheme.secondaryContainer,
        modifier = modifier,
    ) {
        AlbumHeaderBackground(
            album = album,
            modifier = Modifier
                .fillMaxSize()
                .alpha(ContentAlpha.medium / 2),
        )


        AlbumHeaderForeground(
            album = album,
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(dimens.dimen16dp),
        )
    }
}

@Composable
private fun AlbumHeaderBackground(
    album: Album?,
    modifier: Modifier = Modifier,
) {
    CustomCoilImage(
        uri = album?.imageUrl,
        modifier = modifier
    )
}

@Composable
private fun AlbumHeaderForeground(
    album: Album?,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(
            space = dimens.dimen16dp,
            alignment = Alignment.Start,
        ),
        verticalAlignment = Alignment.Bottom,
        modifier = modifier,
    ) {
        AlbumHeaderImage(
            album = album,
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
                .clip(MaterialTheme.shapes.medium),
        )

        AlbumHeaderText(
            album = album,
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
        )
    }
}

@Composable
private fun AlbumHeaderImage(
    album: Album?,
    modifier: Modifier = Modifier,
) {
    Box(modifier) {
        CustomCoilImage(
            uri = album?.imageUrl,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth(),
        )
    }

}

@Composable
private fun AlbumHeaderText(
    album: Album?,
    modifier: Modifier = Modifier,
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(
            space = dimens.dimen4dp,
            alignment = Alignment.Bottom,
        ),
        horizontalAlignment = Alignment.End,
        modifier = modifier,
    ) {
        Text(
            text = album?.title ?: "No Data",
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.titleMedium,
        )

        Text(
            text = album?.artist ?: "No Data",
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.alpha(ContentAlpha.medium),
        )
    }

}