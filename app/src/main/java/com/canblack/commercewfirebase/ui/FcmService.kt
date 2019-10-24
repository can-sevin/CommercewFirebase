package com.canblack.commercewfirebase.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.canblack.commercewfirebase.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.*

class FcmService : FirebaseMessagingService() {
    private lateinit var notificationManager: NotificationManager
    private val ADMIN_CHANNEL_ID = "cansevin"

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        Log.i("new Token",p0)
        pushNewToken(p0)
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        p0.let { message->
            Log.i("FCM", message.data["message"].toString())

            notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                setupNotificationChannels()
            }

            val notificationId = Random().nextInt(60000)
            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val notificationBuilder = NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)  //a resource for your custom small icon
                .setContentTitle(message.data["title"]) //the "title" value you sent in your notification
                .setContentText(message.data["message"]) //ditto
                .setAutoCancel(true)  //dismisses the notification on click
                .setSound(defaultSoundUri)

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.notify(notificationId /* ID of notification */, notificationBuilder.build())

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun setupNotificationChannels() {
        val adminChannel =
            NotificationChannel(ADMIN_CHANNEL_ID, "Name",
                NotificationManager.IMPORTANCE_HIGH)
        adminChannel.description = "Desc"
        adminChannel.enableLights(true)
        adminChannel.lightColor = Color.RED
        adminChannel.enableVibration(true)
        notificationManager.createNotificationChannel(adminChannel)
    }


    private fun pushNewToken(p0: String) {
        // Token her yenilendiğinde, kendi server'ınızda
        // her kullanıcı için token'ı yenilemeniz gerekli
        // Telefonlarla bu token üzerinden iletişime geçiceksiniz !
    }

}
