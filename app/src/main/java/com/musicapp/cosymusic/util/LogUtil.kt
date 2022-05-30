package com.musicapp.cosymusic.util

import android.util.Log

/**
 * @author Eternal Epoch
 * @date 2022/5/30 13:00
 */
object LogUtil {

    private const val VERBOSE = 1

    private const val DEBUG = 2

    private const val INFO = 3

    private const val WARN = 4

    private const val ERROR = 5

    private const val LEVEL = ERROR

    fun v(tag: String, msg: String){
        if(LEVEL <= VERBOSE){
            Log.v(tag, msg)
        }
    }

    fun d(tag: String, msg: String){
        if(LEVEL <= DEBUG){
            Log.d(tag, msg)
        }
    }

    fun i(tag: String, msg: String){
        if(LEVEL <= INFO){
            Log.i(tag, msg)
        }
    }

    fun w(tag: String, msg: String){
        if(LEVEL <= WARN){
            Log.w(tag, msg)
        }
    }

    fun e(tag: String, msg: String){
        if(LEVEL <= ERROR){
            Log.e(tag, msg)
        }
    }

}