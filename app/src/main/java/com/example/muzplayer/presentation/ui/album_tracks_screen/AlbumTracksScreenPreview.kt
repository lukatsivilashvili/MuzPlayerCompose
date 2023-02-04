package com.example.muzplayer.presentation.ui.album_tracks_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.muzplayer.domain.models.Album
import com.example.muzplayer.domain.models.Song
import com.example.muzplayer.presentation.components.CustomCoilImage
import com.example.muzplayer.presentation.ui.library_screen.HomeContent
import com.example.muzplayer.presentation.ui.theme.AppTheme

/**
 * Created by lukatsivilashvili on 04.02.23 7:23 PM.
 */

@Preview
@Composable
fun AlbumTracksScreen(
) {
    val songsList = listOf(
        Song(
            mediaId = "1",
            title = "Shape of You",
            artist = "Ed Sheeran",
            duration = 233,
            songUrl = "https://example.com/shape-of-you",
            imageUrl = "https://example.com/shape-of-you-cover.jpg",
            albumId = 1
        ),
        Song(
            mediaId = "2",
            title = "Blinding Lights",
            artist = "The Weeknd",
            duration = 195,
            songUrl = "https://example.com/blinding-lights",
            imageUrl = "https://example.com/blinding-lights-cover.jpg",
            albumId = 2
        ),
        Song(
            mediaId = "3",
            title = "Levitating",
            artist = "Dua Lipa",
            duration = 201,
            songUrl = "https://example.com/levitating",
            imageUrl = "https://example.com/levitating-cover.jpg",
            albumId = 3
        ),
        Song(
            mediaId = "4",
            title = "Watermelon Sugar",
            artist = "Harry Styles",
            duration = 198,
            songUrl = "https://example.com/watermelon-sugar",
            imageUrl = "https://example.com/watermelon-sugar-cover.jpg",
            albumId = 4
        ),
        Song(
            mediaId = "5",
            title = "Adore You",
            artist = "Harry Styles",
            duration = 213,
            songUrl = "https://example.com/adore-you",
            imageUrl = "https://example.com/adore-you-cover.jpg",
            albumId = 5
        ),
        Song(
            mediaId = "6",
            title = "I Can't Stop Me",
            artist = "TWICE",
            duration = 192,
            songUrl = "https://example.com/i-cant-stop-me",
            imageUrl = "https://example.com/i-cant-stop-me-cover.jpg",
            albumId = 6
        ),
        Song(
            mediaId = "7",
            title = "Good 4 U",
            artist = "Olivia Rodrigo",
            duration = 170,
            songUrl = "https://example.com/good-4-u",
            imageUrl = "https://example.com/good-4-u-cover.jpg",
            albumId = 7
        ),
        Song(
            mediaId = "8",
            title = "Astronaut In The Ocean",
            artist = "Masked Wolf",
            duration = 190,
            songUrl = "https://example.com/astronaut-in-the-ocean",
            imageUrl = "https://example.com/astronaut-in-the-ocean-cover.jpg",
            albumId = 8
        ),
        Song(
            mediaId = "9",
            title = "Lose You To Love Me",
            artist = "Selena Gomez",
            duration = 215,
            songUrl = "https://example"
        )
    )
    AlbumTracksScreenBody(
        album = Album(
            albumId = 0,
            artist = "Jack Harlow",
            title = "You Cant Change Me",
            imageUrl = "",
            songCount = 2
        ),
        albumSongs = songsList,
        albumId = 1
    )
}

@Composable
fun AlbumTracksScreenBody(
    albumId: Int,
    album: Album,
    modifier: Modifier = Modifier,
    albumSongs: List<Song>,
) {
    val listState = rememberLazyListState()
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenWidthDp.dp
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = modifier,
    ) {
        AlbumHeader(
            album = album,
            modifier = Modifier
                .fillMaxSize()
                .height(screenHeight.times(0.3f))
                .weight(1f),
        )

        HomeContent(data = albumSongs, listState = listState)
    }
}


@Composable
private fun AlbumHeader(
    album: Album,
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
                .padding(AppTheme.dimens.dimen16dp),
        )
    }
}

@Composable
private fun AlbumHeaderBackground(
    album: Album,
    modifier: Modifier = Modifier,
) {
    CustomCoilImage(
        uri = "content://media/external/audio/albumart/1448572752748844055",
        modifier = modifier
    )
}

@Composable
private fun AlbumHeaderForeground(
    album: Album,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(
            space = AppTheme.dimens.dimen16dp,
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
    album: Album,
    modifier: Modifier = Modifier,
) {
    Box(modifier) {
        CustomCoilImage(
            uri = "content://media/external/audio/albumart/1448572752748844055",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth(),
        )
    }

}

@Composable
private fun AlbumHeaderText(
    album: Album,
    modifier: Modifier = Modifier,
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(
            space = AppTheme.dimens.dimen4dp,
            alignment = Alignment.Bottom,
        ),
        horizontalAlignment = Alignment.End,
        modifier = modifier,
    ) {
        Text(
            text = "Lil Skies",
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.titleMedium,
        )

        Text(
            text = "Test Album",
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.alpha(ContentAlpha.medium),
        )
    }

}