package com.musicapp.cosymusic.util

import com.musicapp.cosymusic.application.App
import java.io.File
import java.math.BigDecimal
import kotlin.concurrent.thread

/**
 * @author Eternal Epoch
 * @date 2022/6/6 13:34
 */

/**
 * 图片缓存管理
 */
object ImageCacheManager {


    /**
     * 获取图片缓存大小
     */
    fun getImageCacheSize(): String{
        val size = getCacheSize("/image_cache") +
                getCacheSize("/image_manager_disk_cache")
        return getFormatSize(size.toDouble())
    }

    /**
     * 清除所有图片的缓存
     */
    fun clearImageCache(success: () -> Unit){
        thread {
            try {
                deleteFolderFile(App.context.cacheDir.path + "/image_cache")
                deleteFolderFile(App.context.cacheDir.path + "/image_manager_disk_cache")
            }catch (e: Exception){
                e.printStackTrace()
            }
            runOnMainThread{
                success()
            }
        }
    }

    /**
     * 获取缓存的大小
     */
    private fun getCacheSize(cacheDirName: String): Long{
        try {
            val file = File(App.context.cacheDir.path + cacheDirName)
            return getFolderSize(file)
        }catch (e: Exception){
            e.printStackTrace()
        }
        return 0L
    }

    /**
     * 获取指定文件夹内所有文件的大小
     */
    private fun getFolderSize(file: File): Long{
        var size = 0L
        try{
            val fileList = file.listFiles()!!
            for(fil in fileList){
                size += if(fil.isDirectory){
                    getFolderSize(fil)
                }else{
                    fil.length()
                }
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
        return size
    }

    /**
     * 删除指定目录下的文件，这里用于缓存的删除
     */
    private fun deleteFolderFile(filePath: String){
        if(filePath.isNotEmpty()){
            try {
                val file = File(filePath)
                if(file.isDirectory){
                    val files = file.listFiles()!!
                    for(fil in files){
                        deleteFolderFile(fil.absolutePath)
                    }
                }else{
                    file.delete()
                }
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    /**
     * 格式化存储单位
     * @param size 字节数
     * @return formatSize 转为一般的内存单位
     */
    private fun getFormatSize(size: Double): String{
        val kiloByte = size / 1024  //KB
        if(kiloByte < 1){
            return "$size Bytes"
        }
        val megaByte = kiloByte / 1024 //MB
        if(megaByte < 1){
            val result1 = BigDecimal(kiloByte.toString())
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB" //四舍五入
        }
        val gigaByte = megaByte / 1024
        if(gigaByte < 1){
            val result2 = BigDecimal(megaByte.toString())
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB"
        }
        val teraByte = megaByte / 1024
        if(teraByte < 1){
            val result3 = BigDecimal(gigaByte.toString())
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB"
        }
        val result4 = BigDecimal(teraByte.toString())
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB"
    }
}