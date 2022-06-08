package com.musicapp.cosymusic.fragment.artist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.marginBottom
import androidx.core.view.marginEnd
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.musicapp.cosymusic.R
import com.musicapp.cosymusic.adapter.ArtistDescAdapter
import com.musicapp.cosymusic.application.App
import com.musicapp.cosymusic.base.BaseFragment
import com.musicapp.cosymusic.databinding.FragmentArtistDescBinding
import com.musicapp.cosymusic.ui.dialog.IntroductionDialog
import com.musicapp.cosymusic.util.LogUtil
import com.musicapp.cosymusic.util.dp
import com.musicapp.cosymusic.util.dp2px
import com.musicapp.cosymusic.util.toast
import com.musicapp.cosymusic.viewmodel.ArtistViewModel

/**
 * @author Eternal Epoch
 * @date 2022/6/8 13:46
 */
class ArtistDescFragment: BaseFragment() {

    private lateinit var _binding: FragmentArtistDescBinding
    private val binding get() = _binding

    private val viewModel: ArtistViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArtistDescBinding.inflate(inflater, container, false)
        binding.lottieLoading.repeatCount = -1
        binding.lottieLoading.playAnimation()
        return binding.root
    }


    override fun initObservers() {
        viewModel.descResult.observe(viewLifecycleOwner){ result ->
            val response = result.getOrNull()
            if(response != null){
                binding.tvBriefIntro.text = response.briefDesc
                binding.lottieLoading.pauseAnimation()
                binding.lottieLoading.visibility = View.GONE
                binding.tvShowMoreDesc.visibility = View.VISIBLE
                binding.tvShowMoreDesc.setOnClickListener {
                    IntroductionDialog(response.introduction)
                        .show(requireActivity().supportFragmentManager, "")
                }
            }else{
                toast("获取歌手信息失败")
            }
        }
    }
}