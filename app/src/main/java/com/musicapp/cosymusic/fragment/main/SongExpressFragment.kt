package com.musicapp.cosymusic.fragment.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.musicapp.cosymusic.adapter.NeteaseMusicAdapter
import com.musicapp.cosymusic.base.BaseFragment
import com.musicapp.cosymusic.databinding.FragmentSongExpressBinding
import com.musicapp.cosymusic.model.netease.standard.StdMusicData
import com.musicapp.cosymusic.util.toStandard
import com.musicapp.cosymusic.util.toast
import com.musicapp.cosymusic.viewmodel.MainViewModel

/**
 * @author Eternal Epoch
 * @date 2022/6/1 22:47
 */
class SongExpressFragment(): BaseFragment() {

    private lateinit var _binding: FragmentSongExpressBinding
    private val binding get() = _binding

    private val viewModel: MainViewModel by activityViewModels()

    private val musicList = mutableListOf<StdMusicData>()

    private val showList = mutableListOf<StdMusicData>()

    private val adapter by lazy { NeteaseMusicAdapter(showList, false){

    } }

    private val targetObserver by lazy {
        when(arguments?.get("TYPE")){
            96 -> viewModel.euAmSongList
            8 -> viewModel.japaneseSongList
            16 -> viewModel.koreanSongList
            else -> viewModel.chineseSongList
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSongExpressBinding.inflate(inflater, container, false)
        binding.rvSongExpress.layoutManager = GridLayoutManager(activity, 2)
        binding.rvSongExpress.adapter = adapter
        return binding.root
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun initObservers() {
        targetObserver.observe(viewLifecycleOwner){ result ->
            val expressList = result.getOrNull()
            if(expressList != null){
                musicList.clear()
                for(expressMusic in expressList){
                    musicList.add(expressMusic.toStandard())    //转换为标准音乐
                }
                showList.clear()
                showList.addAll(musicList.slice(0..5))
                adapter.notifyDataSetChanged()
            }else{
                toast("获取音乐速递失败")
            }
        }
    }

}