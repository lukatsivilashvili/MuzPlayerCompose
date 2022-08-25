package com.example.muzplayer.extensions

import android.content.Context
import android.net.Uri
import android.util.Size
import java.io.FileNotFoundException

fun Uri?.checkHasArt(context: Context): Boolean {
    var hasArt = true
    try {
        if (this != null) {
            context.contentResolver.loadThumbnail(this, Size(64, 64), null)
        }
    } catch (_: FileNotFoundException) {
        hasArt = false
    }
    return hasArt
}