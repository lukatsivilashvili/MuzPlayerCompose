package com.example.muzplayer.presentation.ui.player_screen

import android.support.v4.media.session.PlaybackStateCompat
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ContentAlpha
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bedtime
import androidx.compose.material.icons.rounded.BedtimeOff
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.PauseCircle
import androidx.compose.material.icons.rounded.PlayCircle
import androidx.compose.material.icons.rounded.Shuffle
import androidx.compose.material.icons.rounded.ShuffleOn
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.muzplayer.common.extensions.formatDuration
import com.example.muzplayer.common.extensions.isPlaying
import com.example.muzplayer.common.extensions.toSong
import com.example.muzplayer.domain.models.Song
import com.example.muzplayer.presentation.components.CrossFadeIcon
import com.example.muzplayer.presentation.components.CustomCoilImage
import com.example.muzplayer.presentation.components.SleepTimerDialog
import com.example.muzplayer.presentation.ui.bottom_bar.BottomBarViewModel
import kotlinx.coroutines.launch

/**
 * Created by ltsivilashvili on 14.09.22
 */

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PlayerScreen(
    backPressedDispatcher: OnBackPressedDispatcher,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    bottomBarViewModel: BottomBarViewModel = hiltViewModel(),
    playerScreenViewModel: PlayerScreenViewModel = hiltViewModel()
) {
    val song = bottomBarViewModel.currentPlayingSong.value
    val songModel = song.let { it?.toSong() }

    PlayerScreenBody(
        modifier = Modifier,
        song = songModel,
        backPressedDispatcher = backPressedDispatcher,
        bottomBarViewModel = bottomBarViewModel,
        playerScreenViewModel = playerScreenViewModel,
        bottomSheetScaffoldState = bottomSheetScaffoldState
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PlayerScreenBody(
    modifier: Modifier = Modifier,
    backPressedDispatcher: OnBackPressedDispatcher,
    bottomBarViewModel: BottomBarViewModel,
    playerScreenViewModel: PlayerScreenViewModel,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    song: Song?
) {
    var sliderIsChanging by remember { mutableStateOf(false) }
    var localSliderValue by remember { mutableStateOf(0f) }

    val sliderProgress =
        if (sliderIsChanging) localSliderValue else playerScreenViewModel.getCurrentPlayerPosition(
            song?.duration
        )

    val coroutineScope = rememberCoroutineScope()


    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                coroutineScope.launch {
                    bottomSheetScaffoldState.bottomSheetState.collapse()
                }
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
            onClose = {
                coroutineScope.launch {
                    bottomSheetScaffoldState.bottomSheetState.collapse()
                }
            }
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
            val openDialog = remember { mutableStateOf(false) }

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
                        bottomBarViewModel.seekTo(song.duration * localSliderValue)
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
                shuffleSongs = {
                    bottomBarViewModel.shufflePlaylist(bottomBarViewModel.shuffleStates.value)
                },
                playbackStateCompat = playbackStateCompat,
                viewModel = bottomBarViewModel,
                openDialog = { openDialog.value = true }
            )

            if (openDialog.value) {

                if (song != null) {
                    SleepTimerDialog(
                        isOpen = openDialog,
                        bottomBarViewModel = bottomBarViewModel,
                        song = song
                    )
                }
            }
        }
        LaunchedEffect("playbackPosition") {
            playerScreenViewModel.updateCurrentPlaybackPosition()
        }

        DisposableEffect(backPressedDispatcher) {
            backPressedDispatcher.addCallback(backCallback)

            onDispose {
                backCallback.remove()
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
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = androidx.compose.material3.MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = modifier.padding(top = 8.dp)
            )
        }
        songModel?.subtitle?.let {
            Text(
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
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
    viewModel: BottomBarViewModel,
    playNextSong: () -> Unit,
    playPreviousSong: () -> Unit,
    playOrToggleSong: () -> Unit,
    shuffleSongs: () -> Unit,
    openDialog: () -> Unit
) {

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(androidx.compose.material3.MaterialTheme.colorScheme.secondaryContainer)
            .padding(vertical = 8.dp)
    ) {
        CrossFadeIcon(
            targetState = viewModel.shuffleStates.value,
            modifier = modifier,
            iconVectorDisabled = Icons.Rounded.Shuffle,
            iconVectorEnabled = Icons.Rounded.ShuffleOn,
            contentDescription = "Shuffle Playlist",
            iconTint = androidx.compose.material3.MaterialTheme.colorScheme.onSecondaryContainer,
            onClickAction = { shuffleSongs.invoke() },
            paddingSize = 16.dp,
            size = 30.dp
        )

        Icon(
            imageVector = Icons.Rounded.SkipPrevious,
            contentDescription = "Skip Previous",
            tint = androidx.compose.material3.MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = modifier
                .clip(RoundedCornerShape(30.dp))
                .clickable {
                    playPreviousSong.invoke()
                }
                .clip(CircleShape)
                .padding(6.dp)
                .size(45.dp)
        )

        CrossFadeIcon(
            targetState = playbackStateCompat?.isPlaying ?: false,
            modifier = modifier,
            iconVectorDisabled = Icons.Rounded.PlayCircle,
            iconVectorEnabled = Icons.Rounded.PauseCircle,
            contentDescription = "Play-Pause",
            iconTint = androidx.compose.material3.MaterialTheme.colorScheme.onSecondaryContainer,
            onClickAction = { playOrToggleSong.invoke() },
            paddingSize = 0.dp,
            size = 90.dp
        )

        Icon(
            imageVector = Icons.Rounded.SkipNext,
            contentDescription = "Skip Next",
            tint = androidx.compose.material3.MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = modifier
                .clip(RoundedCornerShape(30.dp))
                .clickable {
                    playNextSong.invoke()
                }
                .clip(CircleShape)
                .padding(6.dp)
                .size(45.dp)
        )

        CrossFadeIcon(
            targetState = playbackStateCompat?.isPlaying ?: false,
            modifier = modifier,
            iconVectorDisabled = Icons.Rounded.Bedtime,
            iconVectorEnabled = Icons.Rounded.BedtimeOff,
            contentDescription = "Sleep-Timer",
            iconTint = androidx.compose.material3.MaterialTheme.colorScheme.onSecondaryContainer,
            onClickAction = { openDialog.invoke() },
            paddingSize = 16.dp,
            size = 30.dp
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