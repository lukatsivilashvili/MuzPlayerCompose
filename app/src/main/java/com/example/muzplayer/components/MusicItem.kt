package com.example.muzplayer.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.muzplayer.R
import com.example.muzplayer.models.Song
import com.example.muzplayer.viewmodels.MainViewModel

@Composable
fun MusicItem(
    music: Song,
    viewModel: MainViewModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primary)
            .padding(8.dp)
            .clickable {
                viewModel.playOrToggleSong(music)
                viewModel.showPlayerFullScreen = true
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        CoilImage(uri = if (music.hasArt) music.imageUrl else null)
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
fun CoilImage(uri: String?) {
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
                .fallback(R.drawable.ic_default_artwork)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.ic_default_artwork),
            contentDescription = "Music Art",
            contentScale = ContentScale.Fit,
        )
    }
}