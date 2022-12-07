@file:JvmName("TextFieldWithoutPaddingKt")

package com.example.muzplayer.presentation.components.search_textfield

/**
 * Created by lukatsivilashvili on 07.12.22 12:07 PM.
 */

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun TextFieldWithoutPaddingPreview() {
    TextFieldWithoutPadding(placeholder = "Search", value = "asda", onValueChange = {}, modifier = Modifier)
}