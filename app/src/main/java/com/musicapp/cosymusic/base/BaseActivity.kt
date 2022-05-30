package com.musicapp.cosymusic.base

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.musicapp.cosymusic.R
import com.musicapp.cosymusic.activity.ActivityCollector
import com.musicapp.cosymusic.activity.PlayerActivity
import com.musicapp.cosymusic.application.App
import com.musicapp.cosymusic.databinding.MiniPlayerBinding
import com.musicapp.cosymusic.player.Player
import com.musicapp.cosymusic.util.toast

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

    override fun onResume() {
        super.onResume()
        App.musicSourceResponse.observe(this) { result ->
            if(!Player.changeSong) return@observe
            val response = result.getOrNull()
            if (response != null) {
                val url = response[0].url
                Player.reset()
                Player.setDataSource(App.context, Uri.parse(url))
                Player.prepareAsync()
                Player.changeSong = false
                App.playState.value = true
            } else {
                toast("播放失败，请重试")
            }
        }
    }

    override fun onPause() {
        super.onPause()
        App.musicSourceResponse.removeObservers(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityCollector.removeActivity(this)
    }

    private fun initPlayer(){
        miniPlayer?.run {
            root.setOnClickListener {
                val intent = Intent(this@BaseActivity, PlayerActivity::class.java)
                this@BaseActivity.startActivity(intent)
            }

            playerController.setOnClickListener {
                if(Player.isPlaying){
                    Player.pause()
                    App.playState.value = false
                }else{
                    Player.start()
                    App.playState.value = true
                }
            }

            App.playState.observe(this@BaseActivity){ state ->
                if(state == true){
                    Glide.with(this@BaseActivity)
                        .load(R.drawable.ic_mini_pause).into(playerController)
                }else{
                    Glide.with(this@BaseActivity)
                        .load(R.drawable.ic_mini_play).into(playerController)
                }
            }

            App.playSongData.observe(this@BaseActivity){ musicData ->
                musicName.text = musicData.name
                artistName.text = musicData.artist?.get(0)?.name ?: "未知"
                Glide.with(this@BaseActivity).load(musicData.album.picUrl).into(albumImage)
            }
        }
    }

}