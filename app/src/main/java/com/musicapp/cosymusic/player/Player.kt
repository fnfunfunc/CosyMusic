package com.musicapp.cosymusic.player

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri

/**
 * @author Eternal Epoch
 * @date 2022/5/29 17:11
 */
object Player {

    private val mediaPlayer = MediaPlayer()

    init {
        mediaPlayer.setOnPreparedListener{ it.start() }
    }

    val isPlaying get() = mediaPlayer.isPlaying

    val duration get() = mediaPlayer.duration

    val currentPosition get() = mediaPlayer.currentPosition

    fun prepare() = mediaPlayer.prepare()

    fun prepareAsync() = mediaPlayer.prepareAsync()

    fun setOnPreparedListener(listener: MediaPlayer.OnPreparedListener) =
        mediaPlayer.setOnPreparedListener(listener)

    fun start() = mediaPlayer.start()

    fun pause() = mediaPlayer.pause()

    fun reset() = mediaPlayer.reset()

    fun stop() = mediaPlayer.stop()

    fun release() = mediaPlayer.release()

    fun setDataSource(context: Context, uri: Uri) = mediaPlayer.setDataSource(context, uri)

}