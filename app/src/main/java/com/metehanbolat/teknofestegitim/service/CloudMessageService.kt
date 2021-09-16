package com.metehanbolat.teknofestegitim.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.metehanbolat.teknofestegitim.R
import com.metehanbolat.teknofestegitim.view.userviews.UserActivity

class CloudMessageService : FirebaseMessagingService() {
    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        val title = p0.notification!!.title
        val content = p0.notification!!.body

        createNotification(title,content!!)
    }

    private fun createNotification(title : String?, message : String?){
        val builder : NotificationCompat.Builder
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val intent = Intent(this, UserActivity::class.java)
        val intentToGo = PendingIntent.getActivity(applicationContext,1, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        val channelId = resources.getString(R.string.channel_id)
        val channelName = resources.getString(R.string.channel_name)
        val channelPromotion = resources.getString(R.string.channel_promotion)
        val channelPriority = NotificationManager.IMPORTANCE_HIGH

        var channel : NotificationChannel? = notificationManager.getNotificationChannel(channelId)

        if (channel == null){
            channel = NotificationChannel(channelId, channelName, channelPriority)
            channel.description = channelPromotion
            notificationManager.createNotificationChannel(channel)
        }

        builder = NotificationCompat.Builder(this, channelId)
        builder.setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_butterfly)
            .setContentIntent(intentToGo)
            .setAutoCancel(true)

        notificationManager.notify(1, builder.build())
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        Log.e("New Token", p0)
    }
}