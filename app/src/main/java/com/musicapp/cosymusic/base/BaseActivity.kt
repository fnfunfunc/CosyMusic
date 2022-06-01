package com.musicapp.cosymusic.base

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.musicapp.cosymusic.R
import com.musicapp.cosymusic.activity.ActivityCollector
import com.musicapp.cosymusic.activity.PlayerActivity
import com.musicapp.cosymusic.application.App
import com.musicapp.cosymusic.databinding.MiniPlayerBinding

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

            root.setOnClickListener {
                val intent = Intent(this@BaseActivity, PlayerActivity::class.java)
                this@BaseActivity.startActivity(intent)
            }

            ivPlayerController.setOnClickListener {
                App.playerController.value?.changePlayState()
            }

            App.playerController.observe(this@BaseActivity){ playerController ->
                playerController?.let {
                    it.playState.observe(this@BaseActivity){state ->
                        if(state){
                            Glide.with(this@BaseActivity)
                                .load(R.drawable.ic_mini_pause).into(ivPlayerController)
                        }else{
                            Glide.with(this@BaseActivity)
                                .load(R.drawable.ic_mini_play).into(ivPlayerController)
                        }
                    }

                    it.musicData.observe(this@BaseActivity){ musicData ->
                        musicName.text = musicData.name
                        artistName.text = musicData.artist?.get(0)?.name ?: "未知"
                        Glide.with(this@BaseActivity).load(musicData.album.picUrl).into(albumImage)
                    }
                }
            }

        }
    }


}