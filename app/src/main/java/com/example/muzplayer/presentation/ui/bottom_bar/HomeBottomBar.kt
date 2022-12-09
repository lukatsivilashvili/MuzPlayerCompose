package com.example.muzplayer.presentation.ui.bottom_bar

import android.support.v4.media.session.PlaybackStateCompat
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.muzplayer.R
import com.example.muzplayer.common.extensions.isPlaying
import com.example.muzplayer.common.extensions.toSong
import com.example.muzplayer.domain.models.Song
import com.example.muzplayer.presentation.components.CustomCoilImage

@Composable
fun HomeBottomBar(
    modifier: Modifier = Modifier,
    viewModel: BottomBarViewModel = hiltViewModel(),
    onBottomBarClick: () -> Unit
) {

    var offsetX by remember { mutableStateOf(0f) }
    val currentSong = viewModel.currentPlayingSong
    val playbackStateCompat = viewModel.playbackState.value

    AnimatedVisibility(
        visible = currentSong != null,
        modifier = modifier
    ) {
        if (currentSong != null) {
            val song = currentSong.toSong()
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragEnd = {
                                when {
                                    offsetX > 0 -> {
                                        viewModel.skipToPreviousSong()
                                    }
                                    offsetX < 0 -> {
                                        viewModel.skipToNextSong()
                                    }
                                }
                            },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                val (x, _) = dragAmount
                                offsetX = x
                            }
                        )

                    }
                    .background(
                        MaterialTheme.colorScheme.secondaryContainer
                    ),
            ) {
                HomeBottomBarItem(
                    song = song,
                    playbackStateCompat = playbackStateCompat,
                    viewModel = viewModel,
                    onClickEvent = {onBottomBarClick.invoke()}
                )
            }
        }
    }
}


@Composable
fun HomeBottomBarItem(
    song: Song,
    compSize: Dp = 64.dp,
    playbackStateCompat: PlaybackStateCompat?,
    viewModel: BottomBarViewModel,
    onClickEvent: () -> Unit
) {


    Box(
        modifier = Modifier
            .height(64.dp)
            .clickable(onClick = {
                onClickEvent.invoke()
            })

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .width(compSize)
                    .height(compSize)
                    .clip(RoundedCornerShape(5.dp)),
                contentAlignment = Alignment.Center
            )
            {
                CustomCoilImage(uri = song.imageUrl)
            }
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(vertical = 8.dp, horizontal = 16.dp),
            ) {
                Text(
                    song.title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Text(
                    song.subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .graphicsLayer {
                            alpha = 0.60f
                        }

                )
            }
            val icon =
                if (playbackStateCompat?.isPlaying == false) R.drawable.ic_round_play_arrow else R.drawable.ic_round_pause
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(icon)
                    .crossfade(true)
                    .build(),
                contentDescription = "Music Art",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(48.dp)
                    .clickable(
                        interactionSource = remember {
                            MutableInteractionSource()
                        },
                        indication = rememberRipple(
                            bounded = false,
                            radius = 24.dp
                        )
                    ) { viewModel.playOrToggleSong(song, true) }
            )
        }
    }
}

