package com.musicapp.cosymusic.fragment.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.musicapp.cosymusic.databinding.FragmentDiscoverBinding
import com.musicapp.cosymusic.base.BaseFragment
import com.musicapp.cosymusic.viewmodel.MainViewModel
import androidx.fragment.app.*
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.musicapp.cosymusic.R
import com.musicapp.cosymusic.activity.SongExpressActivity
import com.musicapp.cosymusic.util.KString
import com.musicapp.cosymusic.util.LogUtil

/**
 * @author Eternal Epoch
 * @date 2022/5/28 17:02
 */
class DiscoverFragment: BaseFragment() {

    private lateinit var _binding: FragmentDiscoverBinding
    private val binding get() = _binding

    private val viewModel: MainViewModel by activityViewModels()    //共享MainActivity的viewModel

    private val euAm = SongExpressFragment(96)

    private val japan = SongExpressFragment(8)

    private val korea = SongExpressFragment(16)

    private val china = SongExpressFragment(7)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiscoverBinding.inflate(inflater, container, false)

        binding.vp2NewSong.offscreenPageLimit = 4
        binding.vp2NewSong.adapter = object : FragmentStateAdapter(this){
            override fun createFragment(position: Int): Fragment {
                return when(position){
                    1 ->   euAm //欧美
                    2 ->   japan   //日本
                    3 ->   korea    //韩国
                    else -> china  //华语
                }
            }

            override fun getItemCount() = 4
        }

        TabLayoutMediator(binding.songTypeTab, binding.vp2NewSong){ tab, position ->
            tab.text = when(position){
                0 -> getString(R.string.chinese_song)
                1 -> getString(R.string.eu_am)
                2 -> getString(R.string.japan)
                else -> getString(R.string.korea)
            }
        }.attach()

        viewModel.getSongExpressList()

        return binding.root
    }

    override fun initListeners() {
        binding.btnShowMore.setOnClickListener {
            val intent = Intent(activity, SongExpressActivity::class.java).apply {
                putExtra(KString.SONG_EXPRESS_DETAIL, binding.vp2NewSong.currentItem)
            }
            startActivity(intent)
        }
    }

    override fun initObservers() {
        viewModel.chineseSongList.observe(this){
            binding.btnShowMore.visibility = View.VISIBLE
        }
    }
}