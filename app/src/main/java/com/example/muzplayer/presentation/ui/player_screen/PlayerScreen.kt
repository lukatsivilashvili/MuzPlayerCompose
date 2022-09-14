package com.example.muzplayer.presentation.ui.player_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
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

/**
 * Created by ltsivilashvili on 14.09.22
 */

@Preview(name = "PIXEL_3_XL", device = Devices.PIXEL_3_XL)
@Composable
fun PlayerScreen() {
    Column(

    ) {
        PlayerScreenBody()
    }
}

@Preview(group = "comps")
@Composable
fun PlayerScreenBody(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(androidx.compose.material3.MaterialTheme.colorScheme.surface)
            .padding(start = 16.dp, end = 16.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        PlayerScreenNames()
        PlayerSlider()
        PlayerControls()
    }
}

@Preview(group = "comps")
@Composable
fun PlayerScreenNames(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 8.dp)
    ) {
        Text(
            text = "HYDRA",
            style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.onSurface
        )
        Text(text = "FORGOTTENANCE, GTM")
    }
}

@Preview(group = "comps")
@Composable
fun PlayerControls(
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
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
                .padding(12.dp)
                .size(32.dp)
        )
        Icon(
            imageVector = Icons.Rounded.PlayCircle,
            contentDescription = "Play",
            modifier = modifier
                .clip(CircleShape)
                .size(64.dp)
                .padding(8.dp)
        )
        Icon(
            imageVector = Icons.Rounded.SkipNext,
            contentDescription = "Skip Next",
            modifier = modifier
                .clip(CircleShape)
                .padding(12.dp)
                .size(32.dp)
        )
    }
}

@Preview(group = "comps")
@Composable
fun PlayerSlider(
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