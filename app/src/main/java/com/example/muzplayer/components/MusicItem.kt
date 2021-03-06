package com.example.muzplayer.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.muzplayer.models.Song
import com.example.muzplayer.ui.theme.MuzPlayerTheme
import com.example.muzplayer.viewmodels.MainViewModel

@Composable
fun MusicItem(
    music: Song,
    viewModel: MainViewModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                viewModel.playOrToggleSong(music)
                viewModel.showPlayerFullScreen = true
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        CoilImage(uri = music.imageUrl)
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = music.title,
                fontSize = 13.sp,
                fontWeight = Bold,
                modifier = Modifier
                    .padding(start = 22.dp, end = 22.dp)
            )
            Text(
                text = music.subtitle,
                fontSize = 11.sp,
                modifier = Modifier
                    .padding(start = 22.dp, end = 22.dp)
            )
        }
    }
}

@Composable
fun CoilImage(uri: String) {
    Box(
        modifier = Modifier
            .width(64.dp)
            .height(64.dp)
            .clip(RoundedCornerShape(5.dp)),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(uri)
                .crossfade(true)
                .build(),
            contentDescription = "Music Art",
            contentScale = ContentScale.Fit,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF0EAE2, device = Devices.PIXEL_4_XL)
@Composable
fun MusicItemPreview() {
    MuzPlayerTheme() {
        MusicItem(
            music = Song(
                "1",
                "Only God Can Judge Me",
                "last trial",
                "2pac",
                "4:30"
            ),
            viewModel = hiltViewModel()
        )
    }
}