package com.musicapp.cosymusic.fragment.search.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.musicapp.cosymusic.adapter.NeteaseMusicAdapter
import com.musicapp.cosymusic.base.BaseFragment
import com.musicapp.cosymusic.databinding.FragmentMusicResultBinding
import com.musicapp.cosymusic.ui.dialog.MusicMoreDialog
import com.musicapp.cosymusic.util.toast
import com.musicapp.cosymusic.viewmodel.SearchViewModel

/**
 * @author Eternal Epoch
 * @date 2022/6/9 18:54
 */
class MusicResultFragment: BaseFragment() {

    private lateinit var _binding: FragmentMusicResultBinding
    private val binding get() = _binding

    private val viewModel: SearchViewModel by activityViewModels()

    companion object{
        private var isResume = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMusicResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initView() {
        binding.lottieLoading.repeatCount = -1
        binding.lottieLoading.playAnimation()

        binding.rvPlayList.layoutManager = LinearLayoutManager(activity)
    }

    override fun initObservers() {
        viewModel.musicResult.observe(viewLifecycleOwner){ result ->
            val response = result.getOrNull()
            if(response != null){
                if(isResume){   //避免加载上一次的数据
                    isResume = false
                    return@observe
                }
                binding.lottieLoading.pauseAnimation()
                binding.lottieLoading.visibility = View.GONE
                binding.rvPlayList.adapter = NeteaseMusicAdapter(response.songs){
                    MusicMoreDialog(requireActivity(), it){
                        toast("暂不支持删除")
                    }.show(requireActivity().supportFragmentManager, "")
                }
                binding.rvPlayList.scrollToPosition(0)  //回到最上方
            }else{
                toast("加载歌曲列表失败")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isResume = true
    }
}