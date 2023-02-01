package com.example.muzplayer.presentation.components

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
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
    contentScale: ContentScale = ContentScale.Crop
) {
    AsyncImage(
        modifier = modifier
            .background(MaterialTheme.colorScheme.secondaryContainer),
        contentScale = contentScale,
        model = ImageRequest.Builder(LocalContext.current)
            .data(uri)
            .fallback(R.drawable.ic_default_artwork64)
            .crossfade(true)
            .build(),
        error = painterResource(R.drawable.ic_default_artwork64),
        contentDescription = "Music Art",
    )
}
