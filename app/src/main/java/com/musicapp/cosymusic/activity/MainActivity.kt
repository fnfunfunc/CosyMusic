package com.musicapp.cosymusic.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.musicapp.cosymusic.R
import com.musicapp.cosymusic.base.BaseActivity
import com.musicapp.cosymusic.databinding.ActivityMainBinding
import com.musicapp.cosymusic.fragment.main.DiscoverFragment
import com.musicapp.cosymusic.fragment.main.HomeFragment
import com.musicapp.cosymusic.util.*
import com.musicapp.cosymusic.viewmodel.MainViewModel
import eightbitlab.com.blurview.RenderScriptBlur

class MainActivity : BaseActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private val songMenuClickedReceiver = SongMenuClickedReceiver()


    override fun initView() {
        super.initView()

        //屏幕适配
        (binding.titleBar.layoutParams as ConstraintLayout.LayoutParams).apply {
            topMargin = StatusBarUtil.getStatusBarHeight(window, this@MainActivity)
        }

        if(SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        }

        miniPlayer = binding.miniPlayer


        setContentView(binding.root)

        window.navigationBarColor = ContextCompat.getColor(this, R.color.transparent_color)

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

        val decorView = window.decorView
        binding.blurViewPlay.setupWith(decorView.findViewById(R.id.clTotal))
            .setFrameClearDrawable(decorView.background)
            .setBlurAlgorithm(RenderScriptBlur(this))
            .setBlurRadius(20f)
            .setHasFixedTransformationMatrix(true)

    }

    override fun initObservers() {
        viewModel.statusBarHeight.observe(this) {
            (binding.titleBar.layoutParams as ConstraintLayout.LayoutParams).apply {
                height = 56.dp() + it
            }
            (binding.viewPager2.layoutParams as ConstraintLayout.LayoutParams).apply {
                topMargin = 56.dp() + it
            }
        }
        viewModel.navigationBarHeight.observe(this) {
            binding.miniPlayer.root.updateLayoutParams<ConstraintLayout.LayoutParams> {
                bottomMargin = it
            }
            binding.blurViewPlay.updateLayoutParams<ConstraintLayout.LayoutParams> {
                height = 64.dp() + it
            }
        }
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

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter(BroadcastKString.RECOMMEND_MENU_CLICKED)
        registerReceiver(songMenuClickedReceiver, intentFilter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(songMenuClickedReceiver)
    }

    inner class SongMenuClickedReceiver: BroadcastReceiver(){
        override fun onReceive(context: Context?, fromIntent: Intent) {
            val id = fromIntent.getLongExtra(KString.RECOMMEND_SONG_MENU_CLICKED_ID, 0L)
            val intent = Intent(this@MainActivity, SongMenuActivity::class.java).apply {
                putExtra(KString.RECOMMEND_SONG_MENU_CLICKED_ID, id)
            }
            startActivity(intent)
        }
    }
}