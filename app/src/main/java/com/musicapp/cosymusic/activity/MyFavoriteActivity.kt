package com.musicapp.cosymusic.activity

import android.os.Build
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.size.ViewSizeResolver
import coil.transform.BlurTransformation
import coil.transform.RoundedCornersTransformation
import com.musicapp.cosymusic.R
import com.musicapp.cosymusic.adapter.NeteaseMusicAdapter
import com.musicapp.cosymusic.application.App
import com.musicapp.cosymusic.base.BaseActivity
import com.musicapp.cosymusic.databinding.ActivitySongMenuBinding
import com.musicapp.cosymusic.ui.dialog.MusicMoreDialog
import com.musicapp.cosymusic.util.StatusBarUtil
import com.musicapp.cosymusic.util.dp2px

class MyFavoriteActivity : BaseActivity() {

    private val binding by lazy {
        ActivitySongMenuBinding.inflate(layoutInflater) //直接使用SongMenuActivity的布局
    }

    private val favoriteList = App.playerController.value?.getFavoriteList() ?: emptyList()

    private val adapter by lazy {
        NeteaseMusicAdapter(favoriteList, true){
            MusicMoreDialog(this, it){
                //TODO("将要进行删除的操作")
            }.show(supportFragmentManager, null)
        }
    }

    override fun initView() {
        //屏幕适配
        (binding.clTitleBar.layoutParams as ConstraintLayout.LayoutParams).apply {
            topMargin = StatusBarUtil.getStatusBarHeight(window, this@MyFavoriteActivity)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        }

        setContentView(binding.root)

        miniPlayer = binding.miniPlayer

        binding.tvMenuName.text = getString(R.string.my_love)
        binding.tvMenuDesc.text = getString(R.string.my_love_desc)
        binding.ivMenuCover.load(R.drawable.cv_love){
            size(ViewSizeResolver(binding.ivMenuCover))
            transformations(RoundedCornersTransformation(dp2px(8f)))
            crossfade(300)
        }

        binding.ivBackground.load(R.drawable.cv_love) {
            size(ViewSizeResolver(binding.ivBackground))
            transformations(
                BlurTransformation(
                    this@MyFavoriteActivity,
                    25f,
                    10f
                )
            )
            crossfade(300)
        }

        binding.ivPlayAll.setColorFilter(ContextCompat.getColor(this, R.color.app_theme_color))
        binding.tvPlayAll.text = getString(R.string.play_all, favoriteList.size)

        binding.clLoading.visibility = View.GONE //不显示动画

        binding.rvMenuSong.layoutManager = LinearLayoutManager(this)
        binding.rvMenuSong.adapter = adapter
    }
}