package com.musicapp.cosymusic.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.content.getSystemService
import com.musicapp.cosymusic.player.Player

class PlayerService : Service() {

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onCreate() {
        super.onCreate()
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                                    as NotificationManager
        val channel = NotificationChannel("cosy_music", "Cosy Music播放渠道",
            NotificationManager.IMPORTANCE_DEFAULT)
        channel.description = "用来显示音频控制器的通知"
        notificationManager.createNotificationChannel(channel)
    }

    override fun onDestroy() {
        super.onDestroy()
        Player.release()
    }
}