package com.musicapp.cosymusic.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.Handler
import android.os.Looper
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import androidx.lifecycle.MutableLiveData
import com.musicapp.cosymusic.application.App
import com.musicapp.cosymusic.base.BaseMediaService
import com.musicapp.cosymusic.player.Player

class PlayerService : BaseMediaService() {

    private var mediaSession: MediaSessionCompat? = null

    private var mediaSessionCallback: MediaSessionCompat.Callback? = null

    private val playerController by lazy { PlayerController() }


    override fun onBind(intent: Intent) = playerController

    override fun onCreate() {
        //先创建通知
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                                    as NotificationManager
        val channel = NotificationChannel("cosy_music", "Cosy Music播放渠道",
            NotificationManager.IMPORTANCE_DEFAULT)
        channel.description = "用来显示音频控制器的通知"
        notificationManager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(this, "cosy_music")
            .setContentTitle("CosyMusic后台服务")
            .build()
        startForeground(1, notification)

        //调用父类方法
        super.onCreate()
    }

    override fun initMediaSession() {
        //媒体会话的回调，Service通过该回调来控制mediaPlayer
        mediaSessionCallback = object : MediaSessionCompat.Callback(){

            override fun onPlay() {
                playerController.start()
                playerController.playState.value = true
            }

            override fun onPause() {
                playerController.pause()
                playerController.playState.value = false
            }

            override fun onSeekTo(pos: Long) {
                if(playerController.isPrepared){
                    playerController.seekTo(pos.toInt())
                }
            }


        }

        mediaSession = MediaSessionCompat(this, "todo").apply {
            //设置Callback
            setCallback(mediaSessionCallback, Handler(Looper.getMainLooper()))
            //把mediaSession设为Active，这样才可以开始接收信息
            isActive = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        playerController.release()
    }

    inner class PlayerController: Binder(){

        val mediaPlayer = MediaPlayer()

        init {
            mediaPlayer.setOnPreparedListener{ it.start() }
            mediaPlayer.setOnCompletionListener {
                val size = App.playQueue.size
                val nextPosition = (App.playPositionInQueue + 1) % size
                App.playPositionInQueue = nextPosition
                App.getMusicSourceById(App.playQueue[nextPosition].id)
                App.playSongData.value = App.playQueue[nextPosition]
                App.playState.value = false
                changeSong = true
            }
        }

        var changeSong = false

        var isPrepared = false

        val playState = MutableLiveData<Boolean>().also {
            it.value = mediaPlayer.isPlaying
        }

        val isPlaying get() = mediaPlayer.isPlaying

        val duration get() = mediaPlayer.duration

        val currentPosition get() = mediaPlayer.currentPosition

        fun prepare() = mediaPlayer.prepare()

        fun prepareAsync() = mediaPlayer.prepareAsync()

        fun setOnPreparedListener(listener: MediaPlayer.OnPreparedListener) =
            mediaPlayer.setOnPreparedListener(listener)

        fun changePlayState(){
            if(mediaPlayer.isPlaying){
                mediaSessionCallback?.onPause()
            }else{
                mediaSessionCallback?.onPlay()
            }
            playState.value = mediaPlayer.isPlaying
        }

        fun start() = mediaPlayer.start()

        fun pause() = mediaPlayer.pause()

        fun reset() = mediaPlayer.reset()

        fun stop() = mediaPlayer.stop()

        fun release() = mediaPlayer.release()

        fun setDataSource(context: Context, uri: Uri) = mediaPlayer.setDataSource(context, uri)

        fun seekTo(msec: Int) = mediaPlayer.seekTo(msec)
    }



}