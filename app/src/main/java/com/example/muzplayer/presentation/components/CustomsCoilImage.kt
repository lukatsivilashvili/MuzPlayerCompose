package com.example.muzplayer.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.muzplayer.R

@Composable
fun CustomCoilImage(
    uri: String?,
    modifier: Modifier = Modifier,
    compSize: Dp = 64.dp
) {
    Box(
        modifier = modifier
            .width(compSize)
            .height(compSize)
            .clip(RoundedCornerShape(5.dp)),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(uri)
                .fallback(R.drawable.ic_default_artwork256)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.ic_default_artwork256),
            contentDescription = "Music Art",
            contentScale = ContentScale.Fit,
        )
    }
}