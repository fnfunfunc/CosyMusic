package com.musicapp.cosymusic.activity


import android.content.Intent
import android.os.Build
import android.view.View
import android.view.WindowManager
import android.widget.SeekBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import coil.load
import coil.size.ViewSizeResolver
import coil.transform.BlurTransformation
import com.bumptech.glide.Glide
import com.dirror.lyricviewx.OnPlayClickListener
import com.dirror.lyricviewx.OnSingleClickListener
import com.musicapp.cosymusic.R
import com.musicapp.cosymusic.adapter.ArtistAdapter
import com.musicapp.cosymusic.application.App
import com.musicapp.cosymusic.base.BaseActivity
import com.musicapp.cosymusic.data.LyricViewData
import com.musicapp.cosymusic.databinding.ActivityPlayerBinding
import com.musicapp.cosymusic.ui.dialog.ArtistDialog
import com.musicapp.cosymusic.util.*
//import com.musicapp.cosymusic.player.Player
import com.musicapp.cosymusic.viewmodel.PlayerViewModel
import java.util.*


class PlayerActivity : BaseActivity() {

    private val binding by lazy { ActivityPlayerBinding.inflate(layoutInflater) }

    private val viewModel: PlayerViewModel by lazy {
        ViewModelProvider(this).get(PlayerViewModel::class.java)
    }

    override fun initView() {
        setContentView(binding.root)

        //屏幕适配
        (binding.musicName.layoutParams as ConstraintLayout.LayoutParams).apply {
            topMargin = StatusBarUtil.getStatusBarHeight(window, this@PlayerActivity)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        }

        val params: WindowManager.LayoutParams = window.attributes
        params.windowAnimations = R.style.player_activity_animation
        window.attributes = params

        viewModel.getLyricData(App.playerController.value?.musicData?.value?.id ?: -1L) //获取歌词

        binding.ttvStart.setText(0)
        binding.ttvEnd.setText(0)

        val blackColor = ContextCompat.getColor(this, R.color.black)
        binding.lyricView.apply {
            setLabel("聆听好音乐")
            setTimelineTextColor(ContextCompat.getColor(this@PlayerActivity,
                R.color.black))
            setCurrentColor(blackColor)
            setTimeTextColor(blackColor)
            setTimelineColor(blackColor.setAlpha(0.25f))
            setTimelineTextColor(blackColor)
            setNormalColor(blackColor.setAlpha(0.35f))
        }

        App.playerController.value?.musicData?.value?.let {
            binding.musicName.text = it.name
            binding.artistName.text = getArtistsString(it.artists)
            Glide.with(this).load(it.album.picUrl).into(binding.albumImage)
            binding.ivBackground.load(it.album.picUrl){
                size(ViewSizeResolver(binding.ivBackground))
                transformations(
                    BlurTransformation(this@PlayerActivity,
                    15f,
                    15f))
                crossfade(500)
            }

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

        binding.lyricView.setDraggable(true, object : OnPlayClickListener{
            override fun onPlayClick(time: Long): Boolean {
                App.playerController.value?.setProgress(time.toInt())
                return true
            }
        })

        binding.lyricView.setOnSingerClickListener(object : OnSingleClickListener{
            override fun onClick() {
                AnimationUtil.fadeIn(binding.clBody)
                binding.clLyric.visibility = View.INVISIBLE
            }
        })

        binding.clBody.setOnClickListener {
            AnimationUtil.fadeOut(binding.clBody, true)
            binding.clLyric.visibility = View.VISIBLE
        }

        binding.edgeTransparentView.setOnClickListener {
            AnimationUtil.fadeIn(binding.clBody)
            binding.clLyric.visibility = View.GONE
        }

        binding.artistName.setOnClickListener {
            if(App.playerController.value?.musicData?.value?.artists?.size == 1) {
                val intent = Intent(this, ArtistActivity::class.java).apply {
                    putExtra(
                        KString.ARTIST_ID,
                        App.playerController.value?.musicData?.value?.artists?.get(0)?.artistId
                    )
                }
                startActivity(intent)
            }else{
                App.playerController.value?.musicData?.value?.artists?.let {
                    ArtistDialog(it).show(supportFragmentManager, "")
                }
            }
        }
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
            binding.ivBackground.load(musicData.album.picUrl){
                size(ViewSizeResolver(binding.ivBackground))
                transformations(
                    BlurTransformation(this@PlayerActivity,
                        15f,
                        15f))
                crossfade(500)
            }
            binding.playProgressBar.max = musicData.duration
            binding.ttvEnd.setText(musicData.duration)

            viewModel.getLyricData(musicData.id) //获取新的歌词
        }

        viewModel.progress.observe(this){ progress ->
            binding.ttvStart.setText(progress)
            binding.playProgressBar.progress = progress
            binding.lyricView.updateTime(progress.toLong())
        }

        viewModel.lyricLiveData.observe(this){ result ->
            val response = result.getOrNull()
            if(response != null){
                val lyricViewData = LyricViewData(response.lyric?.lyric ?: "", "true")
                binding.lyricView.loadLyric(lyricViewData.lyric, lyricViewData.secondLyric)
            }
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