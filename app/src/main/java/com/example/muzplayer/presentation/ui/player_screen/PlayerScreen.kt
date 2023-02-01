package com.example.muzplayer.presentation.ui.player_screen

import android.support.v4.media.session.PlaybackStateCompat
import androidx.activity.compose.BackHandler
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.muzplayer.common.extensions.formatDuration
import com.example.muzplayer.common.extensions.isPlaying
import com.example.muzplayer.common.extensions.toSong
import com.example.muzplayer.domain.models.Song
import com.example.muzplayer.presentation.components.CrossFadeIcon
import com.example.muzplayer.presentation.components.CustomCoilImage
import com.example.muzplayer.presentation.components.sleep_timer.SleepTimerDialog
import com.example.muzplayer.presentation.ui.bottom_bar.BottomBarViewModel
import com.example.muzplayer.presentation.ui.theme.AppTheme
import kotlinx.coroutines.launch

/**
 * Created by ltsivilashvili on 14.09.22
 */

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PlayerScreen(
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    bottomBarViewModel: BottomBarViewModel = hiltViewModel(),
    playerScreenViewModel: PlayerScreenViewModel = hiltViewModel()
) {
    val song = bottomBarViewModel.currentPlayingSongScreen.collectAsState()
    val songModel = song.value?.toSong()

    PlayerScreenBody(
        modifier = Modifier,
        song = songModel,
        bottomBarViewModel = bottomBarViewModel,
        playerScreenViewModel = playerScreenViewModel,
        bottomSheetScaffoldState = bottomSheetScaffoldState
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PlayerScreenBody(
    modifier: Modifier = Modifier,
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

    BackHandler(enabled = true) {
        coroutineScope.launch {
            bottomSheetScaffoldState.bottomSheetState.collapse()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                androidx.compose.material3.MaterialTheme.colorScheme.surface
            )
            .padding(start = AppTheme.dimens.dimen16dp, end = 16.dp),

        ) {
        PlayerScreenCloseIcon(
            modifier = modifier
                .padding(start = AppTheme.dimens.dimen8dp, top = AppTheme.dimens.dimen16dp),
            onClose = {
                coroutineScope.launch {
                    bottomSheetScaffoldState.bottomSheetState.collapse()
                }
            }
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = modifier
                .fillMaxSize()
        ) {
            val playbackStateCompat = bottomBarViewModel.playbackState.value
            val openDialog = remember { mutableStateOf(false) }

            PlayerScreenImage(songModel = song, modifier = modifier.padding(
                start = AppTheme.dimens.dimen8dp,
                end = AppTheme.dimens.dimen8dp
            ))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = modifier
            ) {

                PlayerScreenNames(songModel = song)
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
) {
    val configuration = LocalConfiguration.current

    val screenHeight = if(configuration.screenHeightDp <= 740) configuration.screenWidthDp.dp.times(0.75f) else configuration.screenWidthDp.dp.times(1f)
    Box(
        modifier = Modifier
            .width(screenHeight)
            .height(screenHeight)
            .clip(RoundedCornerShape(5.dp)),
        contentAlignment = Alignment.Center
    )
    {
        CustomCoilImage(
            uri = songModel?.imageUrl,
            modifier = modifier
                .fillMaxSize()
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
            .padding(start = AppTheme.dimens.dimen8dp, top = AppTheme.dimens.dimen16dp)
            .fillMaxWidth()
    ) {
        songModel?.title?.let {
            Text(
                text = it,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = androidx.compose.material3.MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
        songModel?.artist?.let {
            Text(
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                text = it,
                color = androidx.compose.material3.MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = modifier.padding(bottom = AppTheme.dimens.dimen8dp)
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
            .padding(top = AppTheme.dimens.dimen32dp)
    ) {
        CrossFadeIcon(
            targetState = viewModel.shuffleStates.value,
            modifier = modifier,
            iconVectorDisabled = Icons.Rounded.Shuffle,
            iconVectorEnabled = Icons.Rounded.ShuffleOn,
            contentDescription = "Shuffle Playlist",
            iconTint = androidx.compose.material3.MaterialTheme.colorScheme.onSecondaryContainer,
            onClickAction = { shuffleSongs.invoke() },
            paddingSize = AppTheme.dimens.dimen16dp,
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
            targetState = false,
            modifier = modifier,
            iconVectorDisabled = Icons.Rounded.Bedtime,
            iconVectorEnabled = Icons.Rounded.BedtimeOff,
            contentDescription = "Sleep-Timer",
            iconTint = androidx.compose.material3.MaterialTheme.colorScheme.onSecondaryContainer,
            onClickAction = { openDialog.invoke() },
            paddingSize = AppTheme.dimens.dimen16dp,
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
                .padding(start = AppTheme.dimens.dimen8dp, end = AppTheme.dimens.dimen8dp),
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