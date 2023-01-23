package com.example.muzplayer.presentation.components.album_item

/**
 * Created by lukatsivilashvili on 25.12.22 12:39 AM.
 */

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.muzplayer.domain.models.Album
import com.example.muzplayer.presentation.components.CustomCoilImage


@Composable
fun AlbumItemPreview(
    album: Album,
    compSize: Dp = 64.dp
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.secondary)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .width(compSize)
                .height(compSize)
                .clip(RoundedCornerShape(5.dp)),
            contentAlignment = Alignment.Center
        )
        { CustomCoilImage(uri = album.imageUrl) }
        Column() {
            Text(
                text = album.title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(start = 22.dp, end = 22.dp, top = 8.dp)
            )
            Text(
                text = album.artist,
                fontSize = 11.sp,
                modifier = Modifier
                    .padding(start = 22.dp, end = 22.dp)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF0EAE2, device = Devices.PIXEL_4_XL)
@Composable
fun ItemPreviews() {
    AlbumItemPreview(
        album = Album(
            albumId = 1,
            title = "Only God Can Judge Me",
            artist = "last trial",
            imageUrl = ""
        ),
        compSize = 200.dp
    )
}