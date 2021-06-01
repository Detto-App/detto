package com.dettoapp.detto.UtilityClasses

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

class CustomApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        createFileProgressNotificationChannel()
    }

    private fun createFileProgressNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(
                Constants.PROGRESS_CHANNEL_ID, Constants.PROGRESS_CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW
            ).also { channel ->
                (applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(
                    channel
                )
            }
        }
    }
}