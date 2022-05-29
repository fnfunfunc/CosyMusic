package com.musicapp.cosymusic.util

import android.widget.Toast
import com.musicapp.cosymusic.application.MainApplication

/**
 * @author Eternal Epoch
 * @date 2022/5/29 17:40
 */

fun toast(text: String){
    Toast.makeText(MainApplication.context, text, Toast.LENGTH_SHORT).show()
}