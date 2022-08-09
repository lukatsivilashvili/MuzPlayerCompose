package com.example.muzplayer.extensions

import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Size
import androidx.annotation.RequiresApi
import java.io.FileNotFoundException

@RequiresApi(Build.VERSION_CODES.Q)
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