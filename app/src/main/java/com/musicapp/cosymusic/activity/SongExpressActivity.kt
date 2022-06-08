package com.musicapp.cosymusic.activity

import android.annotation.SuppressLint
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.musicapp.cosymusic.adapter.NeteaseMusicAdapter
import com.musicapp.cosymusic.base.BaseActivity
import com.musicapp.cosymusic.databinding.ActivitySongExpressBinding
import com.musicapp.cosymusic.model.netease.standard.StdMusicData
import com.musicapp.cosymusic.util.KString
import com.musicapp.cosymusic.util.LogUtil
import com.musicapp.cosymusic.util.toStandard
import com.musicapp.cosymusic.viewmodel.MainViewModel

class SongExpressActivity() : BaseActivity() {

    private val viewModel: MainViewModel by viewModels()

    private val showList = mutableListOf<StdMusicData>()

    private val binding by lazy{
        ActivitySongExpressBinding.inflate(layoutInflater)
    }

    private var position = 0

    private val targetObserver by lazy {
        when(position){
            0 -> viewModel.chineseSongList
            1 -> viewModel.euAmSongList
            2 -> viewModel.japaneseSongList
            else -> viewModel.koreanSongList
        }
    }

    private val adapter by lazy {
        NeteaseMusicAdapter(showList, true){

        }
    }

    override fun initView() {
        setContentView(binding.root)

        miniPlayer = binding.miniPlayer

        binding.lottieLoading.repeatCount = -1
        binding.lottieLoading.playAnimation()

        position = intent.getIntExtra(KString.SONG_EXPRESS_DETAIL, 0)
        binding.tvTitle.text = when(position){
            0 -> "华语"
            1 -> "欧美"
            2 -> "日本"
            else -> "韩国"
        }

        binding.rvSongExpress.layoutManager = LinearLayoutManager(this)
        binding.rvSongExpress.adapter = adapter

    }

    override fun initListeners() {
        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initObservers() {
        targetObserver.observe(this){ result ->
            val response = result.getOrNull()
            if(response != null){
                showList.clear()
                for(expressMusic in response){
                    showList.add(expressMusic.toStandard())
                }
                adapter.notifyDataSetChanged()
                binding.lottieLoading.pauseAnimation()
                binding.lottieLoading.visibility = View.GONE
            }else{
                LogUtil.e("SongExpressActivity", result.exceptionOrNull().toString())
            }
        }
    }
}