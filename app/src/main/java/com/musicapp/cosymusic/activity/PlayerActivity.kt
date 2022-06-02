package com.musicapp.cosymusic.activity


import android.view.WindowManager
import android.widget.SeekBar
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.musicapp.cosymusic.R
import com.musicapp.cosymusic.application.App
import com.musicapp.cosymusic.base.BaseActivity
import com.musicapp.cosymusic.databinding.ActivityPlayerBinding
//import com.musicapp.cosymusic.player.Player
import com.musicapp.cosymusic.viewmodel.PlayerViewModel
import com.musicapp.cosymusic.util.cancelCache
import com.musicapp.cosymusic.util.getArtistsString
import java.util.*


class PlayerActivity : BaseActivity() {

    private val binding by lazy { ActivityPlayerBinding.inflate(layoutInflater) }

    private val viewModel: PlayerViewModel by lazy {
        ViewModelProvider(this).get(PlayerViewModel::class.java)
    }

    override fun initView() {
        setContentView(binding.root)
        val params: WindowManager.LayoutParams = window.attributes
        params.windowAnimations = R.style.player_activity_animation
        window.attributes = params

        binding.ttvStart.setText(0)
        binding.ttvEnd.setText(0)
        App.playerController.value?.musicData?.value?.let {
            binding.musicName.text = it.name
            binding.artistName.text = getArtistsString(it.artists)
            Glide.with(this).load(it.album.picUrl).into(binding.albumImage)

            //在有songData的情况下进行此操作
            binding.playProgressBar.max = viewModel.duration.value?: 0
            binding.ttvStart.setText(viewModel.duration.value?:0)
            binding.ttvEnd.setText(viewModel.duration.value?: 0)

            Timer().schedule(timerTask, 0, 900)
        }
    }


    override fun initListeners() {
        binding.ivPlayerController.setOnClickListener {
            viewModel.changePlayState()
        }

        binding.playProgressBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(bar: SeekBar?, progress: Int, fromUser: Boolean) { }

            override fun onStartTrackingTouch(bar: SeekBar?){ }

            override fun onStopTrackingTouch(bar: SeekBar) {
                App.playerController.value?.seekTo(bar.progress)
            }
        })

        binding.ivPlayPrev.setOnClickListener { viewModel.playPrev() }

        binding.ivPlayNext.setOnClickListener { viewModel.playNext() }
    }

    override fun initObservers() {
        App.playerController.value?.playState?.observe(this){ isPlaying ->
            if(isPlaying){
                Glide.with(this).load(R.drawable.ic_pause)
                    .cancelCache()
                    .into(binding.ivPlayerController)
            }else{
                Glide.with(this).load(R.drawable.ic_play)
                    .cancelCache()
                    .into(binding.ivPlayerController)
            }
        }

        App.playerController.value?.musicData?.observe(this){ musicData ->
            binding.musicName.text = musicData.name
            binding.artistName.text = getArtistsString(musicData.artists)
            Glide.with(this).load(musicData.album.picUrl).into(binding.albumImage)
            binding.playProgressBar.max = musicData.duration
            binding.ttvEnd.setText(musicData.duration)
        }

        viewModel.progress.observe(this){ progress ->
            binding.ttvStart.setText(progress)
            binding.playProgressBar.progress = progress
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timerTask.cancel()
    }

    private val timerTask = object : TimerTask(){
        override fun run() {
            viewModel.refreshProgress()
        }
    }
}