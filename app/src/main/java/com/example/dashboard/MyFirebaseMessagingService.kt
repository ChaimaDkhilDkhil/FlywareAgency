package com.example.dashboard

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService() {
     val channelId="notification_channel"
     val channelName="com.example.dashboard"

    override fun onMessageReceived(message: RemoteMessage) {
        Log.e("MyFirebaseMessaging", "onMessageReceived: ${message.data}")

        if (message.notification != null) {
            Log.e("MyFirebaseMessaging", "Notification Body: ${message.notification!!.body}")
            generateNotification(message.notification!!.title!!,message.notification!!.body!!)
        }
    }

     fun getRemoteView(title:String,description: String):RemoteViews{
         val remoteViews = RemoteViews(packageName, R.layout.notification_layout)
         remoteViews.setTextViewText(R.id.notificationTitle,title)
         remoteViews.setTextViewText(R.id.notificationDescription,description)
         Log.e("MyFirebaseMessaging", remoteViews.toString())
         remoteViews.setImageViewResource(R.id.logoApp,R.drawable.logo)
         return remoteViews
     }
     fun generateNotification(title: String,description: String) {
         val intent= Intent(this, MainActivity::class.java)
         intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
         val pendingIntent = PendingIntent.getActivity(this, 0, intent,
             PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)
         var builder: NotificationCompat.Builder=NotificationCompat.Builder(applicationContext, channelId)
             .setSmallIcon(R.drawable.logo)
             .setAutoCancel(true)
             .setVibrate(longArrayOf(1000,1000,1000,1000))
             .setOnlyAlertOnce(true)
             .setContentIntent(pendingIntent)
         builder=builder.setContent(getRemoteView(title,description))
         builder.setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.logo))

         val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

         if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
             val notificationChannel= NotificationChannel(channelId, channelName,NotificationManager.IMPORTANCE_HIGH)
             notificationManager.createNotificationChannel(notificationChannel)
         }
         notificationManager.notify(0,builder.build())
     }
}