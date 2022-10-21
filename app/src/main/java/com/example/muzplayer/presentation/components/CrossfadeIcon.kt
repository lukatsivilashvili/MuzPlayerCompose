package com.example.muzplayer.presentation.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun<T> CrossFadeIcon(
    targetState: Boolean,
    modifier: Modifier,
    iconVectorDisabled: ImageVector,
    iconVectorEnabled: ImageVector,
    contentDescription: String,
    iconTint: Color,
    onClickAction: () -> T,
    paddingSize: Dp,
    size: Dp
) {
    Crossfade(targetState = targetState) {
        if (!it) {
            Icon(
                imageVector = iconVectorDisabled,
                contentDescription = contentDescription,
                tint = iconTint,
                modifier = modifier
                    .clip(RoundedCornerShape(30.dp))
                    .clickable {
                        onClickAction.invoke()
                    }
                    .clip(CircleShape)
                    .padding(paddingSize)
                    .size(size)
            )
        }else{
            Icon(
                imageVector = iconVectorEnabled,
                contentDescription = contentDescription,
                tint = iconTint,
                modifier = modifier
                    .clip(RoundedCornerShape(30.dp))
                    .clickable {
                        onClickAction.invoke()
                    }
                    .clip(CircleShape)
                    .padding(paddingSize)
                    .size(size)
            )
        }
    }
}