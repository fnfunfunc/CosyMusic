package com.musicapp.cosymusic.fragment.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.musicapp.cosymusic.R
import com.musicapp.cosymusic.base.BaseFragment
import com.musicapp.cosymusic.databinding.FragmentSearchResultBinding
import com.musicapp.cosymusic.fragment.search.result.AlbumResultFragment
import com.musicapp.cosymusic.fragment.search.result.ArtistResultFragment
import com.musicapp.cosymusic.fragment.search.result.MusicResultFragment
import com.musicapp.cosymusic.fragment.search.result.SongMenuResultFragment
import com.musicapp.cosymusic.viewmodel.SearchViewModel

/**
 * @author Eternal Epoch
 * @date 2022/6/9 13:10
 * @desc 搜索主界面的Fragment，展示搜索的结果
 */
class SearchResultFragment: BaseFragment() {

    private lateinit var _binding: FragmentSearchResultBinding
    private val binding get() = _binding

    private val viewModel: SearchViewModel by activityViewModels()

    //private val musicResultFragment = MusicResultFragment()

    //private val artistResultFragment = ArtistResultFragment()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initView() {

        binding.vp2SearchResult.offscreenPageLimit = 4
        binding.vp2SearchResult.isSaveEnabled = false //不将原来的Fragment进行保存。以防重新进入该Fragment后加载子Fragment发生异常

        binding.vp2SearchResult.adapter = object : FragmentStateAdapter(this){
            override fun createFragment(position: Int): Fragment {
                return when(position){
                    0 -> MusicResultFragment()
                    1 -> ArtistResultFragment()
                    2 -> AlbumResultFragment()
                    else -> SongMenuResultFragment()
                }
            }

            override fun getItemCount() = 4
        }


        TabLayoutMediator(binding.searchTab, binding.vp2SearchResult){ tab, position ->
            tab.text = when(position){
                0 -> getString(R.string.single_song)
                1 -> getString(R.string.singer)
                2 -> getString(R.string.album)
                else -> getString(R.string.song_menu)
            }
        }.attach()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initObservers() {
        viewModel.musicResult.observe(this) { result ->
            val musicResponse = result.getOrNull()
            if (musicResponse != null) {
                viewModel.onReceiveData()
            }
        }
    }
}