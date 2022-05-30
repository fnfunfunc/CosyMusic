package com.musicapp.cosymusic.service

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import com.musicapp.cosymusic.application.App

/**
 * @author Eternal Epoch
 * @date 2022/5/30 19:31
 */
class PlayerServiceConnection: ServiceConnection {

    override fun onServiceConnected(p0: ComponentName?, binder: IBinder?) {
        App.playerController.value = binder as PlayerService.PlayerController
    }

    /**
     * Service意外断开
     */
    override fun onServiceDisconnected(p0: ComponentName?) {
        App.playerController.value = null
    }

}