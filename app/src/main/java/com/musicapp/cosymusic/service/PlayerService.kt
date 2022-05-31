package com.musicapp.cosymusic.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.Handler
import android.os.Looper
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import androidx.lifecycle.MutableLiveData
import com.musicapp.cosymusic.R
import com.musicapp.cosymusic.application.App.Companion.mmkv
import com.musicapp.cosymusic.base.BaseMediaService
import com.musicapp.cosymusic.model.netease.MusicResponse
import com.musicapp.cosymusic.util.Config
import com.musicapp.cosymusic.util.LogUtil

class PlayerService : BaseMediaService() {

    private var mediaSession: MediaSessionCompat? = null

    private var mediaSessionCallback: MediaSessionCompat.Callback? = null

    private val playerController by lazy { PlayerController() }


    override fun onBind(intent: Intent) = playerController

    override fun onCreate() {
        //先创建通知
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                                    as NotificationManager
        val channel = NotificationChannel(
            CHANNEL_ID, "Cosy Music播放渠道",
            NotificationManager.IMPORTANCE_DEFAULT)
        channel.description = "用来显示音频控制器的通知"
        notificationManager.createNotificationChannel(channel)

        showNotification(playerController.musicData.value, playerController.getAlbumImageBitmap())

        //调用父类方法
        super.onCreate()
    }

    override fun initMediaSession() {
        //媒体会话的回调，Service通过该回调来控制mediaPlayer
        mediaSessionCallback = object : MediaSessionCompat.Callback(){

            override fun onPlay() {
                playerController.start()
                playerController.playState.value = true

                showNotification(playerController.musicData.value, playerController.getAlbumImageBitmap())
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
            // 指明支持的按键信息类型
            setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
            )
            //设置Callback
            setCallback(mediaSessionCallback, Handler(Looper.getMainLooper()))
            //把mediaSession设为Active，这样才可以开始接收信息
            isActive = true
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val playModeCode = intent.getIntExtra(Config.PLAY_MODE_CODE, -1)
        when(playModeCode){
            CODE_PREVIOUS -> playerController.playPrev()
            CODE_PLAY_OR_PAUSE -> playerController.changePlayState()
            CODE_NEXT -> playerController.playNext()
        }
        showNotification(playerController.musicData.value, null)
        //非粘性
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        playerController.release()
    }

    inner class PlayerController: Binder(), MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener{


        private val mediaPlayer = MediaPlayer()

        //专辑封面的bitmap
        private val albumCoverBitmap = MutableLiveData<Bitmap>()

        var isPrepared = false

        val playState = MutableLiveData<Boolean>().also {
            it.value = mediaPlayer.isPlaying
        }

        val isPlaying get() = mediaPlayer.isPlaying

        val duration get() = mediaPlayer.duration

        val currentPosition get() = mediaPlayer.currentPosition

        val musicData = MutableLiveData<MusicResponse.MusicData>()

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

        fun play(){
            if(isPrepared){
                mediaSessionCallback?.onPlay()
            }
        }

        fun reset() = mediaPlayer.reset()

        fun stop() = mediaPlayer.stop()

        fun release() = mediaPlayer.release()

        fun setDataSource(context: Context, uri: Uri) = mediaPlayer.setDataSource(context, uri)

        fun seekTo(msec: Int) = mediaPlayer.seekTo(msec)

        override fun onPrepared(p0: MediaPlayer?) {
            isPrepared = true
            play()

            val url = musicData.value?.album?.picUrl
            if(url != null){
                albumCoverBitmap.value = BitmapFactory.decodeFile(url)
            }
        }

        override fun onCompletion(p0: MediaPlayer?) {
            playNext()
        }

        /**
         * @param playNext 当前歌曲播放失败时是否自动播放下一首(Not implemented)
         */
        fun playMusic(playMusicData: MusicResponse.MusicData, playNext: Boolean = false){
            isPrepared = false  //尚未准备
            musicData.value = playMusicData

            //保存当前播放的歌曲
            mmkv.encode(Config.SERVICE_CURRENT_SONG, playMusicData)

            mediaPlayer.apply {
                reset()
                setDataSource(applicationContext, Uri.parse("https://music.163.com" +
                        "/song/media/outer/url?id=${playMusicData.id}.mp3"))
                prepareAsync()
                setOnPreparedListener(this@PlayerController)
                setOnCompletionListener(this@PlayerController)
            }

        }

        fun playPrev(){
            val prevMusic = PlayerQueue.getPrev()
            if(prevMusic != null){
                playMusic(prevMusic)
            }else{
                LogUtil.e("PlayerService", "上一首播放的歌曲为空")
            }
        }

        fun playNext(){
            val nextMusic = PlayerQueue.getNext()
            if(nextMusic != null){
                playMusic(nextMusic)
            }else{
                LogUtil.e("PlayerService", "下一首播放的歌曲为空")
            }
        }

        fun savePlayList(musicList: MutableList<MusicResponse.MusicData>){
            PlayerQueue.saveNormal(musicList)
        }

        fun getAlbumImageBitmap() = albumCoverBitmap.value

    }



    private fun showNotification(music: MusicResponse.MusicData?, bitmap: Bitmap?){
        val notification = NotificationCompat.Builder(this, CHANNEL_ID).apply {
            setSmallIcon(R.drawable.ic_launcher_foreground)
            setLargeIcon(bitmap)
            setContentTitle(music?.name)
            setContentText(music?.artist?.get(0)?.name ?: "")
            addAction(R.drawable.ic_play_prev_24, "Previous", getPendingIntentPrevious())
            addAction(getPlayerStateIcon(), "Change player state", getPendingIntentPlay())
            addAction(R.drawable.ic_play_next_24, "Next", getPendingIntentNext())
            setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(mediaSession?.sessionToken)
                    .setShowActionsInCompactView(0, 1, 2)
            )
            setOngoing(true)
        }.build()

        startForeground(START_FOREGROUND_ID, notification)
    }

    private fun getPlayerStateIcon() = if(playerController.isPlaying) R.drawable.ic_mini_pause
    else R.drawable.ic_mini_play

    private fun getPendingIntentPrevious(): PendingIntent{
        val intent = Intent(this, PlayerService::class.java)
        intent.putExtra(Config.PLAY_MODE_CODE, CODE_PREVIOUS)
        return buildServicePendingIntent(this, 1, intent)
    }

    private fun getPendingIntentPlay(): PendingIntent{
        val intent = Intent(this, PlayerService::class.java)
        intent.putExtra(Config.PLAY_MODE_CODE, CODE_PLAY_OR_PAUSE)
        return buildServicePendingIntent(this, 2, intent)
    }

    private fun getPendingIntentNext(): PendingIntent{
        val intent = Intent(this, PlayerService::class.java)
        intent.putExtra(Config.PLAY_MODE_CODE, CODE_NEXT)
        return buildServicePendingIntent(this, 3, intent)
    }

    private fun buildServicePendingIntent(context: Context, requestCode: Int, intent: Intent): PendingIntent {
        return PendingIntent.getForegroundService(context, requestCode, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    }

}