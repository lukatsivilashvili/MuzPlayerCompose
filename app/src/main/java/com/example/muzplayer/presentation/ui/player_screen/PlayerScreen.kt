package com.example.muzplayer.presentation.ui.player_screen

import android.support.v4.media.session.PlaybackStateCompat
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.muzplayer.common.extensions.formatDuration
import com.example.muzplayer.common.extensions.isPlaying
import com.example.muzplayer.common.extensions.toSong
import com.example.muzplayer.domain.models.Song
import com.example.muzplayer.presentation.components.CustomCoilImage
import com.example.muzplayer.presentation.ui.bottom_bar.BottomBarViewModel

/**
 * Created by ltsivilashvili on 14.09.22
 */

@Composable
fun PlayerScreen(
    backPressedDispatcher: OnBackPressedDispatcher,
    bottomBarViewModel: BottomBarViewModel = hiltViewModel(),
    playerScreenViewModel: PlayerScreenViewModel = hiltViewModel()
) {
    val song = bottomBarViewModel.currentPlayingSong.value
    val songModel = song.let { it?.toSong(LocalContext.current) }

    AnimatedVisibility(
        visible = song != null && bottomBarViewModel.showPlayerFullScreen,
        enter = slideInVertically(
            initialOffsetY = { it }
        ),
        exit = slideOutVertically(
            targetOffsetY = { it }
        )) {
        if (song != null) {
            PlayerScreenBody(
                modifier = Modifier,
                song = songModel,
                backPressedDispatcher = backPressedDispatcher,
                bottomBarViewModel = bottomBarViewModel,
                playerScreenViewModel = playerScreenViewModel
            )
        }
    }
}

@Composable
fun PlayerScreenBody(
    modifier: Modifier = Modifier,
    backPressedDispatcher: OnBackPressedDispatcher,
    bottomBarViewModel: BottomBarViewModel,
    playerScreenViewModel: PlayerScreenViewModel,
    song: Song?
) {
    var sliderIsChanging by remember { mutableStateOf(false) }
    var localSliderValue by remember { mutableStateOf(0f) }

    val sliderProgress =
        if (sliderIsChanging) localSliderValue else playerScreenViewModel.currentPlayerPosition


    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                bottomBarViewModel.showPlayerFullScreen = false
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                androidx.compose.material3.MaterialTheme.colorScheme.secondaryContainer
            )
            .padding(start = 16.dp, end = 16.dp, bottom = 64.dp),

        ) {
        PlayerScreenCloseIcon(
            modifier = modifier
                .padding(start = 8.dp, bottom = 32.dp, top = 32.dp),
            onClose = { bottomBarViewModel.showPlayerFullScreen = false }
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxSize()
                .background(
                    androidx.compose.material3.MaterialTheme.colorScheme.secondaryContainer
                )
        ) {
            val playbackStateCompat by bottomBarViewModel.playbackState.observeAsState()

            PlayerScreenImage(songModel = song)
            PlayerScreenNames(songModel = song, modifier = modifier.padding(top = 16.dp))
            song?.duration?.let {
                PlayerSlider(
                    currentTime = playerScreenViewModel.currentPlaybackFormattedPosition,
                    totalTime = it.formatDuration(),
                    playbackProgress = sliderProgress,
                    onSliderChange = { newPosition ->
                        localSliderValue = newPosition
                        sliderIsChanging = true
                    },
                    onSliderChangeFinished = {
                        bottomBarViewModel.seekTo(playerScreenViewModel.currentSongDuration * localSliderValue)
                        sliderIsChanging = false
                    }
                )
            }
            PlayerControls(
                playNextSong = { bottomBarViewModel.skipToNextSong() },
                playPreviousSong = { bottomBarViewModel.skipToPreviousSong() },
                playOrToggleSong = {
                    if (song != null) {
                        bottomBarViewModel.playOrToggleSong(mediaItem = song, toggle = true)
                    }
                },
                playbackStateCompat = playbackStateCompat
            )
        }
        LaunchedEffect("playbackPosition") {
            playerScreenViewModel.updateCurrentPlaybackPosition()
        }

        DisposableEffect(backPressedDispatcher) {
            backPressedDispatcher.addCallback(backCallback)

            onDispose {
                backCallback.remove()
                bottomBarViewModel.showPlayerFullScreen = false
            }
        }
    }

}

@Composable
fun PlayerScreenCloseIcon(
    modifier: Modifier = Modifier,
    onClose: () -> Unit
) {
    Icon(
        imageVector = Icons.Rounded.KeyboardArrowDown,
        tint = androidx.compose.material3.MaterialTheme.colorScheme.onSecondaryContainer,
        contentDescription = "Skip Previous",
        modifier = modifier
            .clickable {
                onClose.invoke()
            }
            .clip(CircleShape)
            .size(30.dp)
    )
}

@Composable
fun PlayerScreenImage(
    modifier: Modifier = Modifier,
    songModel: Song?,
    compSize: Dp = 320.dp
) {
    Box(
        modifier = modifier
            .width(compSize)
            .height(compSize)
            .clip(RoundedCornerShape(5.dp)),
        contentAlignment = Alignment.Center
    )
    {
        CustomCoilImage(
            uri = songModel?.imageUrl,
            modifier = modifier
                .background(androidx.compose.material3.MaterialTheme.colorScheme.onTertiaryContainer)
                .fillMaxWidth()
        )
    }
}

@Composable
fun PlayerScreenNames(
    modifier: Modifier = Modifier,
    songModel: Song?
) {
    Column(
        modifier = modifier
            .padding(all = 8.dp)
            .fillMaxWidth()
    ) {
        songModel?.title?.let {
            Text(
                text = it,
                style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = androidx.compose.material3.MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = modifier.padding(top = 8.dp)
            )
        }
        songModel?.subtitle?.let {
            Text(
                text = it,
                color = androidx.compose.material3.MaterialTheme.colorScheme.onSecondaryContainer,
            )
        }
    }
}

@Composable
fun PlayerControls(
    modifier: Modifier = Modifier,
    playbackStateCompat: PlaybackStateCompat?,
    playNextSong: () -> Unit,
    playPreviousSong: () -> Unit,
    playOrToggleSong: () -> Unit
) {

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(androidx.compose.material3.MaterialTheme.colorScheme.secondaryContainer)
            .padding(vertical = 8.dp)
    ) {
        Icon(
            imageVector = Icons.Rounded.SkipPrevious,
            contentDescription = "Skip Previous",
            tint = androidx.compose.material3.MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = modifier
                .clickable {
                    playPreviousSong.invoke()
                }
                .clip(CircleShape)
                .padding(6.dp)
                .size(40.dp)
        )
        Icon(
            imageVector = if (playbackStateCompat?.isPlaying == false) Icons.Rounded.PlayCircle else Icons.Rounded.PauseCircle,
            contentDescription = "Play",
            tint = androidx.compose.material3.MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = modifier
                .clickable {
                    playOrToggleSong.invoke()
                }
                .clip(CircleShape)
                .size(90.dp)
                .padding(8.dp)
        )
        Icon(
            imageVector = Icons.Rounded.SkipNext,
            contentDescription = "Skip Next",
            tint = androidx.compose.material3.MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = modifier
                .clickable {
                    playNextSong.invoke()
                }
                .clip(CircleShape)
                .padding(6.dp)
                .size(40.dp)
        )
    }
}

@Composable
fun PlayerSlider(
    modifier: Modifier = Modifier,
    currentTime: String,
    totalTime: String,
    playbackProgress: Float,
    onSliderChange: (Float) -> Unit,
    onSliderChangeFinished: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(androidx.compose.material3.MaterialTheme.colorScheme.secondaryContainer)
    ) {
        androidx.compose.material3.Slider(
            value = playbackProgress,
            modifier = modifier
                .fillMaxWidth(),
            colors = SliderDefaults.colors(
                thumbColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                activeTrackColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                inactiveTrackColor = androidx.compose.material3.MaterialTheme.colorScheme.onSecondaryContainer,
                activeTickColor = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary,
                inactiveTickColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
            ),
            onValueChange = onSliderChange,
            onValueChangeFinished = onSliderChangeFinished
        )
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    currentTime,
                    style = MaterialTheme.typography.body2,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    totalTime,
                    style = MaterialTheme.typography.body2,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}