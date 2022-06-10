package com.musicapp.cosymusic.fragment.search.result

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.musicapp.cosymusic.activity.AlbumActivity
import com.musicapp.cosymusic.adapter.AlbumAdapter
import com.musicapp.cosymusic.base.BaseFragment
import com.musicapp.cosymusic.databinding.FragmentAlbumResultBinding
import com.musicapp.cosymusic.util.KString
import com.musicapp.cosymusic.viewmodel.SearchViewModel

/**
 * @author Eternal Epoch
 * @date 2022/6/10 13:37
 */
class AlbumResultFragment: BaseFragment() {

    private lateinit var _binding: FragmentAlbumResultBinding
    private val binding get() = _binding

    private val viewModel: SearchViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumResultBinding.inflate(inflater, container, false)
        binding.rvAlbumList.layoutManager = LinearLayoutManager(requireContext())
        return binding.root
    }

    override fun initView() {
        binding.lottieLoading.repeatCount = -1
        binding.lottieLoading.playAnimation()
    }

    override fun initObservers() {
        viewModel.albumResult.observe(viewLifecycleOwner){ result ->
            val response = result.getOrNull()
            if(response != null){
                binding.lottieLoading.pauseAnimation()
                binding.lottieLoading.visibility = View.GONE
                binding.rvAlbumList.adapter = AlbumAdapter(response.albums){
                    val intent = Intent(requireActivity(), AlbumActivity::class.java).apply {
                        putExtra(KString.ALBUM_ID, it)
                    }
                    startActivity(intent)
                }
            }
        }
    }
}