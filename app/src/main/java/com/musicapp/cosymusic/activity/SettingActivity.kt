package com.musicapp.cosymusic.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.musicapp.cosymusic.base.BaseActivity
import com.musicapp.cosymusic.databinding.ActivitySettingBinding
import com.musicapp.cosymusic.util.ImageCacheManager
import com.musicapp.cosymusic.util.toast
import com.musicapp.cosymusic.viewmodel.SettingViewModel

class SettingActivity : BaseActivity() {

    private val binding by lazy{
        ActivitySettingBinding.inflate(layoutInflater)
    }

    private val viewModel by lazy {
        ViewModelProvider(this).get(SettingViewModel::class.java)
    }

    override fun initView() {
        setContentView(binding.root)

        binding.tvImageCacheNum.text = ImageCacheManager.getImageCacheSize()

    }

    override fun initListeners() {
        binding.llClearImageCache.setOnClickListener {
            viewModel.clearImageCache {
                toast("清除图片缓存成功")
            }
        }

        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    override fun initObservers() {
        viewModel.imageCache.observe(this){ cache ->
            binding.tvImageCacheNum.text = cache
        }
    }
}