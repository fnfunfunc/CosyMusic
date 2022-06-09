package com.musicapp.cosymusic.fragment.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.musicapp.cosymusic.adapter.NeteaseMusicAdapter
import com.musicapp.cosymusic.base.BaseFragment
import com.musicapp.cosymusic.databinding.FragmentSearchMainBinding
import com.musicapp.cosymusic.model.netease.standard.StdMusicData
import com.musicapp.cosymusic.ui.dialog.MusicMoreDialog
import com.musicapp.cosymusic.util.LogUtil
import com.musicapp.cosymusic.util.toast
import com.musicapp.cosymusic.viewmodel.SearchViewModel

/**
 * @author Eternal Epoch
 * @date 2022/6/9 13:10
 * @desc 搜索主界面的Fragment，展示搜索的结果
 */
class SearchMainFragment(private val onReceiveData: () -> Unit): BaseFragment() {

    private lateinit var _binding: FragmentSearchMainBinding
    private val binding get() = _binding

    private val viewModel: SearchViewModel by activityViewModels()

    //音乐数据
    private var musicDataList = mutableListOf<StdMusicData>()

    private val musicAdapter by lazy {
        NeteaseMusicAdapter(musicDataList, true){
            MusicMoreDialog(requireActivity(),  it){
                toast("暂不支持删除")
            }.show(requireActivity().supportFragmentManager, null)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initView() {
        binding.lottieLoading.repeatCount = -1
        binding.lottieLoading.playAnimation()

        binding.rvPlayList.layoutManager = LinearLayoutManager(activity)
        binding.rvPlayList.adapter = musicAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initObservers() {
        viewModel.musicResultLiveData.observe(this) { result ->
            val musicResponse = result.getOrNull()
            if (musicResponse != null) {
                onReceiveData()
                binding.lottieLoading.pauseAnimation()
                binding.lottieLoading.visibility = View.GONE
                musicDataList.clear()
                musicDataList.addAll(musicResponse.songs)
                musicAdapter.notifyDataSetChanged()
                binding.rvPlayList.scrollToPosition(0)  //回到最上方
            } else {
                toast("未能查询到任何歌曲信息")
            }
        }
    }
}