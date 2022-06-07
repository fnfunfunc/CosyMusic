package com.musicapp.cosymusic.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.musicapp.cosymusic.util.ImageCacheManager

/**
 * @author Eternal Epoch
 * @date 2022/6/6 15:48
 */
class SettingViewModel: ViewModel() {

    val imageCache = MutableLiveData<String>()

    fun getImageCache(){
        imageCache.value = ImageCacheManager.getImageCacheSize()
    }

    fun clearImageCache(success: () -> Unit){
        ImageCacheManager.clearImageCache {
            success()
            imageCache.value = ImageCacheManager.getImageCacheSize()
        }
    }
}