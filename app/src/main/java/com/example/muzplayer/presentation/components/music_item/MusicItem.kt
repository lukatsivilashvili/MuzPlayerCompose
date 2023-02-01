package com.example.muzplayer.presentation.components.music_item

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.muzplayer.common.base.BaseViewModel
import com.example.muzplayer.common.extensions.formatDuration
import com.example.muzplayer.domain.models.Song
import com.example.muzplayer.presentation.components.CustomCoilImage
import com.example.muzplayer.presentation.ui.theme.AppTheme

@Composable
fun MusicItem(
    music: Song,
    compSize: Dp = AppTheme.dimens.dimen64dp,
    viewModel: BaseViewModel?
) {
    val dimen8dp = AppTheme.dimens.dimen8dp
    val dimen22dp = AppTheme.dimens.dimen22dp
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(dimen8dp)
            .clickable {
                viewModel?.playOrToggleSong(music)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(compSize)
                .height(compSize)
                .clip(RoundedCornerShape(5.dp)),
            contentAlignment = Alignment.Center
        )
        {
            CustomCoilImage(uri = music.imageUrl)
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = music.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium,
                fontSize = 13.sp,
                fontWeight = Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(start = dimen22dp, end = dimen22dp)
            )
            Text(
                text = music.artist,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(start = dimen22dp, end = dimen22dp)
            )
        }
        Text(
            text = music.duration.formatDuration(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(start = dimen22dp, end = dimen22dp)
        )
    }
}
