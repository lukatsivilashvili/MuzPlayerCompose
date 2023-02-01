package com.example.muzplayer.presentation.ui.album_songs_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.muzplayer.domain.models.Album
import com.example.muzplayer.domain.models.Song

/**
 * Created by lukatsivilashvili on 25.01.23 11:18 AM.
 */

@Preview
@Composable
fun AlbumPreview() {
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
    AlbumScreen(
        album = Album(
            albumId = 0,
            artist = "Jack Harlow",
            title = "You Cant Change Me",
            imageUrl = "",
            songCount = 2
        ),
        albumSongs = songsList
    )
}

@Composable
fun AlbumScreen(
    album: Album,
    modifier: Modifier = Modifier,
    albumSongs: List<Song>,
) {
    Column(modifier = modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.surface)) {

    }
}