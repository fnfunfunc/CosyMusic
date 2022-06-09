package com.musicapp.cosymusic.activity

import android.os.Build
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.size.ViewSizeResolver
import coil.transform.BlurTransformation
import coil.transform.RoundedCornersTransformation
import com.musicapp.cosymusic.R
import com.musicapp.cosymusic.adapter.NeteaseMusicAdapter
import com.musicapp.cosymusic.base.BaseActivity
import com.musicapp.cosymusic.databinding.ActivityAlbumBinding
import com.musicapp.cosymusic.ui.dialog.MusicMoreDialog
import com.musicapp.cosymusic.util.KString
import com.musicapp.cosymusic.util.StatusBarUtil
import com.musicapp.cosymusic.util.dp2px
import com.musicapp.cosymusic.util.toast
import com.musicapp.cosymusic.viewmodel.AlbumViewModel

class AlbumActivity : BaseActivity() {

    private val binding by lazy {
        ActivityAlbumBinding.inflate(layoutInflater)
    }

    private val viewModel by lazy {
        ViewModelProvider(this).get(AlbumViewModel::class.java)
    }

    override fun initView() {
        setContentView(binding.root)

        //屏幕适配
        (binding.clTop.layoutParams as ConstraintLayout.LayoutParams).apply {
            topMargin = StatusBarUtil.getStatusBarHeight(window, this@AlbumActivity)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        }

        miniPlayer = binding.miniPlayer

        val id = intent.getLongExtra(KString.ARTIST_ID, 0L)
        viewModel.getAlbumData(id)

        binding.lottieLoading.repeatCount = -1
        binding.lottieLoading.playAnimation()
        binding.rvAlbumSong.layoutManager = LinearLayoutManager(this)
        binding.ivPlayAll.setColorFilter(ContextCompat.getColor(this, R.color.app_theme_color))

        var rvAlbumScrollY = 0
        binding.rvAlbumSong.setOnScrollChangeListener { _, _, _, _, oldScrollY ->
            rvAlbumScrollY += oldScrollY
            if (rvAlbumScrollY < 0) {
                binding.tvTitle.let {
                    if (it.text == getString(R.string.album)) {
                        it.text = binding.tvAlbumName.text
                    }
                }
            } else {
                binding.tvTitle.text = getString(R.string.album)
            }
        }
    }

    override fun initObservers() {
        viewModel.albumDataResult.observe(this) { result ->
            val response = result.getOrNull()
            if (response != null) {
                binding.apply {
                    lottieLoading.pauseAnimation()
                    clLoading.visibility = View.GONE
                    rvAlbumSong.adapter = NeteaseMusicAdapter(response.songs) {
                        MusicMoreDialog(this@AlbumActivity, it) {
                            toast("暂不支持删除")
                        }.show(supportFragmentManager, "")
                    }

                    tvPlayAll.text = getString(R.string.play_all, response.songs.size)
                    response.album.let {
                        tvAlbumName.text = it.name
                        ivAlbumCover.load(it.picUrl){
                            size(ViewSizeResolver(ivAlbumCover))
                            transformations(RoundedCornersTransformation(dp2px(8f)))
                            crossfade(300)
                        }
                        ivBackground.load(it.picUrl){
                            size(ViewSizeResolver(ivBackground))
                            transformations(BlurTransformation(this@AlbumActivity,
                                25f, 10f))
                            crossfade(300)
                        }
                    }
                }

            }
        }
    }
}