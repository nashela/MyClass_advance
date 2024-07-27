package com.shelazh.myclass.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.api.Context
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.shelazh.myclass.R

class FirebaseMsgService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.d("firebase-token", token)
    }

    private fun showNotification(
        context: android.content.Context,
        title: String,
        message: String,
        id: String
    ) {
        val notificationId = id.hashCode()

        val notificationManager =
            context.getSystemService(android.content.Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "default",
                "Default Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(context, "default")
            .setSmallIcon(R.drawable.baseline_notifications)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        message.data.isNotEmpty().let {
            val title = message.data["title"] ?: "Default Title"
            val body = message.data["body"] ?: "Default Message"
            val id = message.data["id"] ?: "default_id"

            showNotification(applicationContext, title, body, id)
        }
        message.notification?.let {
            val title = it.title ?: "Default Title"
            val body = it.body ?: "Default Message"
            val id = message.messageId ?: "default_id"

            showNotification(applicationContext, title, body, id)
        }
    }
}