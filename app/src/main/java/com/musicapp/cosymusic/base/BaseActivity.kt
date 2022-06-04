package com.musicapp.cosymusic.base

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import coil.load
import coil.size.ViewSizeResolver
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.musicapp.cosymusic.R
import com.musicapp.cosymusic.activity.ActivityCollector
import com.musicapp.cosymusic.activity.PlayerActivity
import com.musicapp.cosymusic.application.App
import com.musicapp.cosymusic.databinding.MiniPlayerBinding
import com.musicapp.cosymusic.ui.dialog.PlayListDialog
import com.musicapp.cosymusic.util.cancelCache
import com.musicapp.cosymusic.util.getArtistsString

abstract class BaseActivity : AppCompatActivity() {

    var miniPlayer: MiniPlayerBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCollector.addActivity(this)

        requestData()
        initView()
        registerPermission()
        initData()
        initPlayer()
        initListeners()
        initObservers()
        initBroadcastReceivers()

    }

    protected open fun requestData() { }
    protected open fun initView() { }
    protected open fun registerPermission() { }
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

            ivPlayerQueue.setOnClickListener {
                PlayListDialog().show(supportFragmentManager, null)
            }

            App.playerController.observe(this@BaseActivity){ playerController ->
                playerController?.let {
                    it.playState.observe(this@BaseActivity){state ->
                        if(state){
                            Glide.with(this@BaseActivity)
                                .load(R.drawable.ic_mini_pause)
                                .cancelCache()
                                .into(ivPlayerController)
                        }else{
                            Glide.with(this@BaseActivity)
                                .load(R.drawable.ic_mini_play)
                                .cancelCache()
                                .into(ivPlayerController)
                        }
                    }

                    it.musicData.observe(this@BaseActivity){ musicData ->
                        musicName.text = musicData.name

                        artistName.text = getArtistsString(musicData.artists)

                        /*Glide.with(this@BaseActivity)
                            .load(musicData.album.picUrl)
                            .into(albumImage)*/
                        albumImage.load(musicData.album.picUrl){
                            size(ViewSizeResolver(albumImage))
                            allowHardware(false)
                        }
                    }
                }
            }

        }
    }


}