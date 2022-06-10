package com.musicapp.cosymusic.fragment.search.result

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.musicapp.cosymusic.activity.ArtistActivity
import com.musicapp.cosymusic.adapter.ArtistDataAdapter
import com.musicapp.cosymusic.base.BaseFragment
import com.musicapp.cosymusic.databinding.FragmentArtistResultBinding
import com.musicapp.cosymusic.util.KString
import com.musicapp.cosymusic.util.toast
import com.musicapp.cosymusic.viewmodel.SearchViewModel

/**
 * @author Eternal Epoch
 * @date 2022/6/9 19:17
 */
class ArtistResultFragment : BaseFragment() {

    private lateinit var _binding: FragmentArtistResultBinding
    private val binding get() = _binding

    private val viewModel: SearchViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArtistResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initView() {
        binding.lottieLoading.repeatCount = -1
        binding.lottieLoading.playAnimation()

        binding.rvArtistList.layoutManager = LinearLayoutManager(activity)
    }

    override fun initObservers() {
        viewModel.artistResult.observe(viewLifecycleOwner) { result ->
            val response = result.getOrNull()
            if (response != null) {
                binding.lottieLoading.pauseAnimation()
                binding.lottieLoading.visibility = View.GONE
                binding.rvArtistList.adapter = ArtistDataAdapter(response.artists) {
                    val intent = Intent(requireActivity(), ArtistActivity::class.java).apply {
                        putExtra(KString.ARTIST_ID, it)
                    }
                    startActivity(intent)
                }
            } else {
                toast("获取歌手信息失败")
            }
        }
    }
}