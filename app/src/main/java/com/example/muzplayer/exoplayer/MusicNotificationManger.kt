package com.example.muzplayer.exoplayer

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.muzplayer.R
import com.example.muzplayer.extensions.checkHasArt
import com.example.muzplayer.utils.Constants.NOTIFICATION_ID
import com.example.muzplayer.utils.Constants.NOTIFICATION_LARGE_ICON_SIZE
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import kotlinx.coroutines.*

class MusicNotificationManger(
    private val context: Context,
    sessionToken: MediaSessionCompat.Token,
    notificationListener: PlayerNotificationManager.NotificationListener,
) {

    private val notificationManager: PlayerNotificationManager
    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)

    init {
        val channelId =
            createNotificationChannel("my_service", "My Background Service")
        val mediaController = MediaControllerCompat(context, sessionToken)
        notificationManager = PlayerNotificationManager.Builder(
            context,
            NOTIFICATION_ID,
            channelId,
            DescriptionAdapter(mediaController)
        )
            .setSmallIconResourceId(R.drawable.ic_music)
            .setNotificationListener(notificationListener)
            .build()
        notificationManager.setMediaSessionToken(sessionToken)
    }

    fun showNotification(player: Player) {
        notificationManager.setPlayer(player)
    }

    private inner class DescriptionAdapter(private val controller: MediaControllerCompat) :
        PlayerNotificationManager.MediaDescriptionAdapter {

        var currentIconUri: Uri? = null
        var currentBitmap: Bitmap? = null

        override fun createCurrentContentIntent(player: Player): PendingIntent? =
            controller.sessionActivity

        override fun getCurrentContentText(player: Player) =
            controller.metadata.description.subtitle.toString()

        override fun getCurrentContentTitle(player: Player) =
            controller.metadata.description.title.toString()

        override fun getCurrentLargeIcon(
            player: Player,
            callback: PlayerNotificationManager.BitmapCallback
        ): Bitmap? {
            val iconUri = getArt()
            return if (currentIconUri != iconUri || currentBitmap == null) {
                // Cache the bitmap for the current song so that successive calls to
                // `getCurrentLargeIcon` don't cause the bitmap to be recreated.
                currentIconUri = iconUri
                serviceScope.launch {
                    currentBitmap = resolveUriAsBitmap(iconUri)
                    currentBitmap?.let { callback.onBitmap(it) }
                }
                null
            } else {
                currentBitmap
            }
        }

        private suspend fun resolveUriAsBitmap(uri: Uri?): Bitmap? {
            val toLoad = uri ?: R.drawable.ic_default_artwork
            return withContext(Dispatchers.IO) {
                // Block on downloading artwork.
                Glide.with(context)
                    .asBitmap()
                    .load(toLoad)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .submit(NOTIFICATION_LARGE_ICON_SIZE, NOTIFICATION_LARGE_ICON_SIZE)
                    .get()
            }
        }

        private fun getArt(): Uri?{
            return if(controller.metadata.description.mediaUri.checkHasArt(context)){
                controller.metadata.description.iconUri
            }else{
                null
            }
        }
    }
    private fun createNotificationChannel(channelId: String, channelName: String): String{
        val chan = NotificationChannel(channelId,
            channelName, NotificationManager.IMPORTANCE_NONE)
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }
}