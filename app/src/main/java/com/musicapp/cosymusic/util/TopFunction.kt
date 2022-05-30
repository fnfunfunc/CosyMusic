package com.musicapp.cosymusic.util

import android.widget.Toast
import com.musicapp.cosymusic.application.App

/**
 * @author Eternal Epoch
 * @date 2022/5/29 17:40
 */

fun toast(text: String){
    Toast.makeText(App.context, text, Toast.LENGTH_SHORT).show()
}