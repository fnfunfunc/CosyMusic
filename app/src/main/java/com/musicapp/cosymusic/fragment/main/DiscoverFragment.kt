package com.musicapp.cosymusic.fragment.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.musicapp.cosymusic.databinding.FragmentDiscoverBinding
import com.musicapp.cosymusic.base.BaseFragment
import com.musicapp.cosymusic.viewmodel.MainViewModel
import androidx.fragment.app.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.musicapp.cosymusic.R
import com.musicapp.cosymusic.activity.SongExpressActivity
import com.musicapp.cosymusic.activity.TopListActivity
import com.musicapp.cosymusic.adapter.RecommendMenuAdapter
import com.musicapp.cosymusic.model.netease.RecommendMenuResponse
import com.musicapp.cosymusic.util.KString
import com.musicapp.cosymusic.util.LogUtil
import com.musicapp.cosymusic.util.toast

/**
 * @author Eternal Epoch
 * @date 2022/5/28 17:02
 */
class DiscoverFragment: BaseFragment() {

    private lateinit var _binding: FragmentDiscoverBinding
    private val binding get() = _binding

    private val viewModel: MainViewModel by activityViewModels()    //共享MainActivity的viewModel

    private val euAm = SongExpressFragment().apply {
        arguments = Bundle().also {
            it.putInt("TYPE", 96)
        }
    }

    private val japan = SongExpressFragment().apply{
        arguments = Bundle().also {
            it.putInt("TYPE", 8)
        }
    }

    private val korea = SongExpressFragment().apply{
        arguments = Bundle().also {
            it.putInt("TYPE", 16)
        }
    }

    private val china = SongExpressFragment().apply {
        arguments = Bundle().also {
            it.putInt("TYPE", 7)
        }
    }

    private val recommendMenuList = mutableListOf<RecommendMenuResponse.Result>()

    private val recommendMenuAdapter by lazy {
        RecommendMenuAdapter(recommendMenuList)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiscoverBinding.inflate(inflater, container, false)

        binding.lottieLoading1.repeatCount = -1
        binding.lottieLoading1.playAnimation()
        binding.lottieLoading2.repeatCount = -1
        binding.lottieLoading2.pauseAnimation()

        binding.rvRecommend.layoutManager = GridLayoutManager(activity, 2,
            GridLayoutManager.HORIZONTAL , false)
        binding.rvRecommend.adapter = recommendMenuAdapter

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

            override fun containsItem(itemId: Long): Boolean {
                return true
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

        viewModel.getRecommendMenu()

        return binding.root
    }

    override fun initListeners() {
        binding.btnShowMore.setOnClickListener {
            val intent = Intent(activity, SongExpressActivity::class.java).apply {
                putExtra(KString.SONG_EXPRESS_DETAIL, binding.vp2NewSong.currentItem)
            }
            startActivity(intent)
        }

        binding.rankList.setOnClickListener {
            val intent = Intent(activity, TopListActivity::class.java)
            startActivity(intent)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initObservers() {
        viewModel.chineseSongList.observe(this){
            binding.lottieLoading1.pauseAnimation()
            binding.lottieLoading1.visibility = View.GONE //隐藏动画
            binding.btnShowMore.visibility = View.VISIBLE
        }

        viewModel.recommendMenuList.observe(this){ result ->
            binding.lottieLoading2.pauseAnimation()
            binding.lottieLoading2.visibility = View.GONE
            val response = result.getOrNull()
            if(response != null){
                recommendMenuList.clear()
                recommendMenuList.addAll(response)
                recommendMenuAdapter.notifyDataSetChanged()
            }else{
                toast("获取推荐歌单失败")
            }
        }
    }
}