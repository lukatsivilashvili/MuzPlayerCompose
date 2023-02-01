package com.example.muzplayer.presentation.ui.player_screen

import androidx.compose.foundation.background
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
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.PlayCircle
import androidx.compose.material.icons.rounded.Repeat
import androidx.compose.material.icons.rounded.Shuffle
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.muzplayer.presentation.components.CustomCoilImage
import com.example.muzplayer.presentation.ui.theme.AppTheme

/**
 * Created by ltsivilashvili on 14.09.22
 */

@Preview(name = "PIXEL_XL", device = Devices.PIXEL_3_XL)
@Composable
fun PlayerScreenBodyPreview(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(androidx.compose.material3.MaterialTheme.colorScheme.surface)
            .padding(start = AppTheme.dimens.dimen16dp, end = AppTheme.dimens.dimen16dp, bottom = AppTheme.dimens.dimen32dp)
            .fillMaxSize()

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
    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenWidthDp.dp
    Box(
        modifier = Modifier
            .width(screenHeight)
            .height(screenHeight)
            .clip(RoundedCornerShape(5.dp)),
        contentAlignment = Alignment.Center
    )
    {     CustomCoilImage(
        uri = null,
        modifier = modifier
            .fillMaxWidth()
            .background(androidx.compose.material3.MaterialTheme.colorScheme.tertiaryContainer)
    ) }
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
            .padding(top = 32.dp)
    ) {

        Icon(
            imageVector = Icons.Rounded.Shuffle,
            contentDescription = "Shuffle Playlist",
            tint = androidx.compose.material3.MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = modifier

                .clip(CircleShape)
                .padding(16.dp)
                .size(35.dp)
        )

        Icon(
            imageVector = Icons.Rounded.SkipPrevious,
            contentDescription = "Skip Previous",
            modifier = modifier
                .clip(CircleShape)
                .padding(6.dp)
                .size(45.dp)
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
                .size(45.dp)
        )

        Icon(
            imageVector = Icons.Rounded.Repeat,
            contentDescription = "Repeat Song",
            tint = androidx.compose.material3.MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = modifier

                .clip(CircleShape)
                .padding(16.dp)
                .size(35.dp)
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