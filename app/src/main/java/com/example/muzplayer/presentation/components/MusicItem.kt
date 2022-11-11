package com.example.muzplayer.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.muzplayer.common.extensions.formatDuration
import com.example.muzplayer.domain.models.Song
import com.example.muzplayer.presentation.ui.library_screen.MainViewModel

@Composable
fun MusicItem(
    music: Song,
    compSize: Dp = 64.dp,
    viewModel: MainViewModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(8.dp)
            .clickable {
                viewModel.playOrToggleSong(music)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(compSize)
                .height(compSize)
                .clip(RoundedCornerShape(5.dp)),
            contentAlignment = Alignment.Center
        )
        {
            CustomCoilImage(uri = if (music.hasArt) music.imageUrl else null)
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = music.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium,
                fontSize = 13.sp,
                fontWeight = Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(start = 22.dp, end = 22.dp)
            )
            Text(
                text = music.subtitle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(start = 22.dp, end = 22.dp)
            )
        }
        Text(
            text = music.duration.formatDuration(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(start = 22.dp, end = 22.dp)
        )
    }
}


@Composable
fun MusicItemPreview(
    music: Song,
    compSize: Dp = 64.dp
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondary)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(compSize)
                .height(compSize)
                .clip(RoundedCornerShape(5.dp)),
            contentAlignment = Alignment.Center
        )
        { CustomCoilImage(uri = if (music.hasArt) music.imageUrl else null) }
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
        Text(
            text = music.duration.formatDuration(),
            fontSize = 11.sp,
            modifier = Modifier
                .padding(start = 22.dp, end = 22.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF0EAE2, device = Devices.PIXEL_4_XL)
@Composable
fun ItemPreview() {
    MusicItemPreview(
        music = Song(
            mediaId = "1",
            title = "Only God Can Judge Me",
            subtitle = "last trial",
            duration = 132123123,
            songUrl = "4:30"
        )
    )
}