package com.example.muzplayer.presentation.ui.player_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.PlayCircle
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.muzplayer.presentation.components.CustomCoilImage
import com.example.muzplayer.presentation.ui.bottom_bar.BottomBarViewModel

/**
 * Created by ltsivilashvili on 14.09.22
 */

@Composable
fun PlayerScreenPreview(
    bottomBarViewModel: BottomBarViewModel = hiltViewModel()
) {
    AnimatedVisibility(
        visible = bottomBarViewModel.showPlayerFullScreen,
        enter = slideInVertically(
            initialOffsetY = { it }
        ),
        exit = slideOutVertically(
            targetOffsetY = { it }
        )) {
        PlayerScreenBodyPreview()
    }
}

@Preview(name = "PIXEL_3_XL", device = Devices.PIXEL_3_XL)
@Composable
fun PlayerScreenBodyPreview(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(androidx.compose.material3.MaterialTheme.colorScheme.surface)
            .padding(start = 16.dp, end = 16.dp, bottom = 64.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        PlayerScreenCloseIconPreview()
        PlayerScreenImagePreview()
        PlayerScreenNamesPreview()
        PlayerSliderPreview()
        PlayerControlsPreview()
    }
}

@Composable
fun PlayerScreenCloseIconPreview(
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector = Icons.Rounded.KeyboardArrowDown,
        contentDescription = "Skip Previous",
        modifier = modifier
            .clip(CircleShape)
            .padding(bottom = 64.dp)
            .size(30.dp)
    )
}

@Preview(group = "comps")
@Composable
fun PlayerScreenImagePreview(
    modifier: Modifier = Modifier
) {
    CustomCoilImage(
        uri = null,
        compSize = 350.dp,
        modifier = modifier
            .padding(bottom = 64.dp)
            .background(androidx.compose.material3.MaterialTheme.colorScheme.onTertiaryContainer)
            .fillMaxWidth()
            .background(androidx.compose.material3.MaterialTheme.colorScheme.tertiaryContainer)
    )
}

@Preview(group = "comps")
@Composable
fun PlayerScreenNamesPreview(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(all = 8.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "HYDRA",
            style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.onSurface,
            modifier = modifier.padding(top = 8.dp)
        )
        Text(text = "FORGOTTENANCE, GTM")
    }
}

@Preview(group = "comps")
@Composable
fun PlayerControlsPreview(
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(androidx.compose.material3.MaterialTheme.colorScheme.surface)
            .padding(vertical = 8.dp)
    ) {
        Icon(
            imageVector = Icons.Rounded.SkipPrevious,
            contentDescription = "Skip Previous",
            modifier = modifier
                .clip(CircleShape)
                .padding(6.dp)
                .size(40.dp)
        )
        Icon(
            imageVector = Icons.Rounded.PlayCircle,
            contentDescription = "Play",
            modifier = modifier
                .clip(CircleShape)
                .size(90.dp)
                .padding(8.dp)
        )
        Icon(
            imageVector = Icons.Rounded.SkipNext,
            contentDescription = "Skip Next",
            modifier = modifier
                .clip(CircleShape)
                .padding(6.dp)
                .size(40.dp)
        )
    }
}

@Preview(group = "comps")
@Composable
fun PlayerSliderPreview(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(androidx.compose.material3.MaterialTheme.colorScheme.surface)
    ) {
        androidx.compose.material3.Slider(
            value = 1f,
            modifier = modifier
                .fillMaxWidth(),
            colors = SliderDefaults.colors(
                thumbColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                activeTrackColor = androidx.compose.material3.MaterialTheme.colorScheme.secondary,
                inactiveTrackColor = androidx.compose.material3.MaterialTheme.colorScheme.onSecondaryContainer,
            ),
            onValueChange = {},
            onValueChangeFinished = {}
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
                    "0:00",
                    style = MaterialTheme.typography.body2
                )
            }
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    "4:51",
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}