package com.example.muzplayer.presentation.components.album_item

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.muzplayer.domain.models.Album
import com.example.muzplayer.presentation.components.CustomCoilImage
import com.example.muzplayer.presentation.ui.album_screen.AlbumScreenViewModel
import com.example.muzplayer.presentation.ui.theme.AppTheme

/**
 * Created by lukatsivilashvili on 25.12.22 12:43 AM.
 */

@Composable
fun AlbumItem(
    album: Album,
    viewModel: AlbumScreenViewModel,
    compSize: Dp = AppTheme.dimens.dimen200dp,
    modifier: Modifier = Modifier
) {
    val dimen22dp =AppTheme.dimens.dimen22dp
    val dimen8dp =AppTheme.dimens.dimen8dp

    Column(
        modifier = modifier
            .width(compSize)
            .background(MaterialTheme.colorScheme.surface)
            .padding(AppTheme.dimens.dimen8dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = modifier
                .width(compSize)
                .height(compSize)
                .clip(RoundedCornerShape(5.dp)),
            contentAlignment = Alignment.Center
        )
        {
            CustomCoilImage(uri = album.imageUrl)
        }
        Column() {
            Text(
                text = album.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = modifier
                    .padding(start = dimen22dp, end = dimen22dp, top = dimen8dp)
            )
            Text(
                text = album.artist,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = modifier
                    .padding(start = dimen22dp, end = dimen22dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}