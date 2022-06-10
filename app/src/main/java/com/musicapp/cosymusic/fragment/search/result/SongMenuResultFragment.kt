package com.musicapp.cosymusic.fragment.search.result

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.musicapp.cosymusic.activity.SongMenuActivity
import com.musicapp.cosymusic.adapter.SongMenuAdapter
import com.musicapp.cosymusic.base.BaseFragment
import com.musicapp.cosymusic.databinding.FragmentSongMenuResultBinding
import com.musicapp.cosymusic.util.KString
import com.musicapp.cosymusic.viewmodel.SearchViewModel

/**
 * @author Eternal Epoch
 * @date 2022/6/9 23:53
 */
class SongMenuResultFragment: BaseFragment() {

    private lateinit var _binding: FragmentSongMenuResultBinding
    private val binding get() = _binding

    private val viewModel:SearchViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSongMenuResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initView() {
        binding.lottieLoading.repeatCount = -1
        binding.lottieLoading.playAnimation()

        binding.rvSongMenuList.layoutManager = LinearLayoutManager(activity)
    }

    override fun initObservers() {
        viewModel.songMenuResult.observe(viewLifecycleOwner){ result ->
            val response = result.getOrNull()
            if(response != null){
                binding.lottieLoading.pauseAnimation()
                binding.lottieLoading.visibility = View.GONE
                binding.rvSongMenuList.adapter = SongMenuAdapter(response.playlists){ id ->
                    val intent = Intent(requireActivity(), SongMenuActivity::class.java).apply {
                        putExtra(KString.SONG_MENU_CLICKED_ID, id)
                    }
                    startActivity(intent)
                }
            }
        }
    }
}