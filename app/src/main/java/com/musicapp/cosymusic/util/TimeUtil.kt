package com.musicapp.cosymusic.util

/**
 * @author Eternal Epoch
 * @date 2022/5/29 23:47
 */
object TimeUtil {

    private const val SEC = 1_000
    private const val MIN = 60_000
    private const val HOUR = 3_600_000

    fun formatDuration(duration: Int) = run {
        val hour = duration / HOUR
        val min = (duration % HOUR) / MIN
        val sec = (duration % MIN) / SEC
        if(hour == 0){
            String.format("%02d:%02d", min, sec)
        }else{
            String.format("%02d:%02d:%02d", hour, min, sec)
        }
    }
}