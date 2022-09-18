package com.example.muzplayer.presentation.components

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.muzplayer.R

@Composable
fun CustomCoilImage(
    uri: String?,
    modifier: Modifier = Modifier,
) {
    AsyncImage(
        modifier = modifier
            .fillMaxHeight(),
        contentScale = ContentScale.Crop,
        model = ImageRequest.Builder(LocalContext.current)
            .data(uri)
            .fallback(R.drawable.ic_default_artwork256)
            .crossfade(true)
            .build(),
        placeholder = painterResource(R.drawable.ic_default_artwork256),
        contentDescription = "Music Art",
    )
}
