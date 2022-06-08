package com.musicapp.cosymusic.activity

import android.os.Build
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import coil.load
import coil.size.ViewSizeResolver
import coil.transform.BlurTransformation
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.musicapp.cosymusic.R
import com.musicapp.cosymusic.base.BaseActivity
import com.musicapp.cosymusic.databinding.ActivityArtistBinding
import com.musicapp.cosymusic.fragment.artist.ArtistAlbumFragment
import com.musicapp.cosymusic.fragment.artist.ArtistDescFragment
import com.musicapp.cosymusic.fragment.artist.ArtistSingleSongFragment
import com.musicapp.cosymusic.util.KString
import com.musicapp.cosymusic.util.StatusBarUtil
import com.musicapp.cosymusic.util.toast
import com.musicapp.cosymusic.viewmodel.ArtistViewModel

class ArtistActivity : BaseActivity() {

    private val binding by lazy{
        ActivityArtistBinding.inflate(layoutInflater)
    }

    private val viewModel by lazy{
        ViewModelProvider(this).get(ArtistViewModel::class.java)
    }

    private val singleSongFragment = ArtistSingleSongFragment()

    private val albumFragment = ArtistAlbumFragment()

    private val descFragment = ArtistDescFragment()

    override fun initView() {
        setContentView(binding.root)

        //屏幕适配
        (binding.clTop.layoutParams as ConstraintLayout.LayoutParams).apply {
            topMargin = StatusBarUtil.getStatusBarHeight(window, this@ArtistActivity)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        }

        val id = intent.getLongExtra(KString.ARTIST_ID, 0L)
        viewModel.getArtistAllInfo(id)

        miniPlayer = binding.miniPlayer

        binding.viewPager2.apply {
            offscreenPageLimit = 4
            adapter = object : FragmentStateAdapter(this@ArtistActivity){
                override fun createFragment(position: Int): Fragment {
                    return when(position){
                        0 -> singleSongFragment
                        1 -> albumFragment
                        3 -> descFragment
                        else -> ArtistSingleSongFragment()
                    }
                }
                override fun getItemCount() = 4
            }
        }

        TabLayoutMediator(binding.tabLayoutArtist, binding.viewPager2){ tab, position ->
            tab.text = when(position){
                0 -> getString(R.string.single_song)
                1 -> getString(R.string.album)
                2 -> getString(R.string.mv)
                else -> getString(R.string.detail)
            }
        }.attach()
    }

    override fun initObservers() {
        viewModel.artistInfoResult.observe(this){ result ->
            val response = result.getOrNull()
            if(response != null){
                val artist = response.artist
                binding.tvArtistName.text = artist.name
                Glide.with(this).load(artist.cover).into(binding.ivArtistCover)
                binding.ivArtistBackground.load(artist.cover){
                    size(ViewSizeResolver(binding.ivArtistBackground))
                    transformations(BlurTransformation(this@ArtistActivity,
                        25f, 10f))
                    crossfade(300)
                }
            }else{
                toast("获取歌手信息失败")
            }
        }
    }
}