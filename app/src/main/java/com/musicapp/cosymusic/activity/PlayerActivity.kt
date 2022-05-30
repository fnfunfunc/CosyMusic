package com.musicapp.cosymusic.activity


import android.view.WindowManager
import android.widget.SeekBar
import com.bumptech.glide.Glide
import com.musicapp.cosymusic.R
import com.musicapp.cosymusic.application.App
import com.musicapp.cosymusic.base.BaseActivity
import com.musicapp.cosymusic.databinding.ActivityPlayerBinding
import com.musicapp.cosymusic.player.Player
import java.util.*


class PlayerActivity : BaseActivity() {

    private val binding by lazy { ActivityPlayerBinding.inflate(layoutInflater) }

    override fun initView() {
        setContentView(binding.root)
        val params: WindowManager.LayoutParams = window.attributes
        params.windowAnimations = R.style.player_activity_animation
        window.attributes = params

        binding.ttvStart.setText(0)
        binding.ttvEnd.setText(0)
        App.playSongData.value?.let {
            binding.musicName.text = it.name
            binding.artistName.text = it.artist?.get(0)?.name
            Glide.with(this).load(it.album.picUrl).into(binding.albumImage)

            //在有songData的情况下进行此操作
            binding.playProgressBar.max = Player.duration
            binding.ttvStart.setText(Player.currentPosition)
            binding.ttvEnd.setText(Player.duration)

            Timer().schedule(timerTask, 0, 900)
        }
    }


    override fun initListeners() {
        binding.playerController.setOnClickListener {
            if(Player.isPlaying){
                Player.pause()
                App.playState.value = false
            }else{
                Player.start()
                App.playState.value = true
            }
        }

        binding.playProgressBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(bar: SeekBar?, progress: Int, fromUser: Boolean) { }

            override fun onStartTrackingTouch(bar: SeekBar?){ }

            override fun onStopTrackingTouch(bar: SeekBar) {
                Player.seekTo(bar.progress)
            }
        })
    }

    override fun initObservers() {
        App.playState.observe(this){ isPlaying ->
            if(isPlaying){
                Glide.with(this).load(R.drawable.ic_pause).into(binding.playerController)
            }else{
                Glide.with(this).load(R.drawable.ic_play).into(binding.playerController)
            }
        }

        App.playSongData.observe(this){ musicData ->
            binding.musicName.text = musicData.name
            binding.artistName.text = musicData.artist?.get(0)?.name?:"未知"
            Glide.with(this).load(musicData.album.picUrl).into(binding.albumImage)
            binding.playProgressBar.max = musicData.duration
            binding.ttvEnd.setText(musicData.duration)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timerTask.cancel()
    }

    private val timerTask = object : TimerTask(){
        override fun run() {
            runOnUiThread {
                binding.ttvStart.setText(Player.currentPosition)
                binding.playProgressBar.progress = Player.currentPosition
            }
        }
    }
}