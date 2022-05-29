package com.musicapp.cosymusic.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.musicapp.cosymusic.R
import com.musicapp.cosymusic.application.MainApplication
import com.musicapp.cosymusic.databinding.MiniPlayerBinding
import com.musicapp.cosymusic.player.Player

abstract class BaseActivity : AppCompatActivity() {

    var miniPlayer: MiniPlayerBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCollector.addActivity(this)
        initView()
        registerPermission()
        initSettings()
        initData()
        initPlayer()
        initListeners()
        initObservers()
        initBroadcastReceivers()
    }

    protected open fun initView() { }
    protected open fun registerPermission() { }
    protected open fun initSettings() { }
    protected open fun initData() { }
    protected open fun initListeners() { }
    protected open fun initObservers() { }
    protected open fun initBroadcastReceivers() { }

    override fun onDestroy() {
        super.onDestroy()
        ActivityCollector.removeActivity(this)
    }

    private fun initPlayer(){
        miniPlayer?.run {
            playerController.setOnClickListener {
                if(Player.isPlaying){
                    Player.pause()
                    MainApplication.playState.value = false
                }else{
                    Player.start()
                    MainApplication.playState.value = true
                }
            }

            MainApplication.playState.observe(this@BaseActivity){ state ->
                if(state == true){
                    Glide.with(this@BaseActivity)
                        .load(R.drawable.ic_pause).into(playerController)
                }else{
                    Glide.with(this@BaseActivity)
                        .load(R.drawable.ic_play).into(playerController)
                }
            }

            MainApplication.playSongData.observe(this@BaseActivity){ musicData ->
                musicName.text = musicData.name
                artistName.text = musicData.artist?.get(0)?.name ?: "未知"
                Glide.with(this@BaseActivity).load(musicData.album.picUrl).into(albumImage)
            }
        }
    }
}