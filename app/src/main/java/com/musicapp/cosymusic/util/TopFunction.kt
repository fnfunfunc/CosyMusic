package com.musicapp.cosymusic.util

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.musicapp.cosymusic.application.App

/**
 * @author Eternal Epoch
 * @date 2022/5/29 17:40
 */

/**
 * Toast消息
 */
fun toast(text: String){
    Toast.makeText(App.context, text, Toast.LENGTH_SHORT).show()
}

/**
 * 运行在主线程中
 */
fun runOnMainThread(runnable: Runnable){
    Handler(Looper.getMainLooper()).post(runnable)
}
