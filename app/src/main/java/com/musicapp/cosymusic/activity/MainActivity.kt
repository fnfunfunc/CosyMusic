package com.musicapp.cosymusic.activity

import android.content.Intent
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.musicapp.cosymusic.R
import com.musicapp.cosymusic.base.BaseActivity
import com.musicapp.cosymusic.databinding.ActivityMainBinding
import com.musicapp.cosymusic.fragment.main.DiscoverFragment
import com.musicapp.cosymusic.fragment.main.HomeFragment
import com.musicapp.cosymusic.viewmodel.MainViewModel

class MainActivity : BaseActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun initView() {
        super.initView()
        setContentView(binding.root)

        miniPlayer = binding.miniPlayer

        binding.viewPager2.offscreenPageLimit = 2
        binding.viewPager2.adapter = object : FragmentStateAdapter(this){
            override fun createFragment(position: Int): Fragment {
                return when(position){
                    0 -> HomeFragment() //个人主页
                    else -> DiscoverFragment()  //发现界面
                }
            }

            override fun getItemCount() = 2
        }

        TabLayoutMediator(binding.tabLayout, binding.viewPager2){ tab, position ->
            tab.text = when(position){
                0 -> getString(R.string.my)
                else -> getString(R.string.discover)
            }
        }.attach()

    }

    override fun initListeners() {
        binding.menuBtn.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        binding.searchBtn.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
    }
}