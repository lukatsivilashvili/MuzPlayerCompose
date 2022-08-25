package com.example.muzplayer.components

import android.os.Build
import android.support.v4.media.session.PlaybackStateCompat
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.muzplayer.R
import com.example.muzplayer.extensions.isPlaying
import com.example.muzplayer.extensions.toSong
import com.example.muzplayer.models.Song
import com.example.muzplayer.viewmodels.MainViewModel

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun HomeBottomBar(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel()
) {

    var offsetX by remember { mutableStateOf(0f) }
    val currentSong = viewModel.currentPlayingSong.value
    val playbackStateCompat by viewModel.playbackState.observeAsState()

    AnimatedVisibility(
        visible = currentSong != null,
        modifier = modifier
    ) {
        if (currentSong != null) {
            val song = currentSong.toSong(LocalContext.current)
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
                        MaterialTheme.colors.primary
                    ),
            ) {
                HomeBottomBarItem(
                    song = song!!,
                    playbackStateCompat = playbackStateCompat,
                    viewModel = viewModel
                )
            }
        }
    }
}


@Composable
fun HomeBottomBarItem(
    song: Song,
    playbackStateCompat: PlaybackStateCompat?,
    viewModel: MainViewModel
) {


    Box(
        modifier = Modifier
            .height(64.dp)
            .clickable(onClick = {
                viewModel.showPlayerFullScreen = true
            })

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            CoilImage(uri = if (song.hasArt) song.imageUrl else null)
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(vertical = 8.dp, horizontal = 32.dp),
            ) {
                Text(
                    song.title,
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Text(
                    song.subtitle,
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .graphicsLayer {
                            alpha = 0.60f
                        }

                )
            }
            val icon = if (playbackStateCompat?.isPlaying == false) {
                R.drawable.ic_round_play_arrow
            } else {
                R.drawable.ic_round_pause
            }
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

