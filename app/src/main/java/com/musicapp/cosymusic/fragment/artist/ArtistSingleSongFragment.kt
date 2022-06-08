package com.musicapp.cosymusic.fragment.artist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.musicapp.cosymusic.adapter.NeteaseMusicAdapter
import com.musicapp.cosymusic.base.BaseFragment
import com.musicapp.cosymusic.databinding.FragmentArtistSingleSongBinding
import com.musicapp.cosymusic.ui.dialog.MusicMoreDialog
import com.musicapp.cosymusic.util.toast
import com.musicapp.cosymusic.viewmodel.ArtistViewModel

/**
 * @author Eternal Epoch
 * @date 2022/6/7 19:45
 */
class ArtistSingleSongFragment: BaseFragment() {

    private lateinit var _binding : FragmentArtistSingleSongBinding
    private val binding get() = _binding

    private val viewModel: ArtistViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArtistSingleSongBinding.inflate(inflater, container, false)
        binding.lottieLoading.repeatCount = -1
        binding.lottieLoading.playAnimation()
        return binding.root
    }

    override fun initObservers() {
        viewModel.artistSingleSongResult.observe(viewLifecycleOwner){ result ->
            val response = result.getOrNull()
            if(response != null){
                binding.rvSingleSong.layoutManager = LinearLayoutManager(activity)
                binding.rvSingleSong.adapter = NeteaseMusicAdapter(response, showMore = true){
                    MusicMoreDialog(requireActivity(), it){
                        toast("暂不支持删除")
                    }.show(requireActivity().supportFragmentManager, "")
                }
                binding.lottieLoading.pauseAnimation()
                binding.lottieLoading.visibility = View.GONE
            }else{
                toast("获取歌手单曲失败")
            }
        }
    }
}