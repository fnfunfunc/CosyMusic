package com.musicapp.cosymusic.util

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.core.animation.addListener


/**
 * @author Eternal Epoch
 * @date 2022/6/6 23:47
 */
object AnimationUtil {


    /**
     * 淡出
     * @param view 将要淡出的View
     * @param gone 是否设为gone
     */
    fun fadeOut(view: View, gone: Boolean) {
        val objectAnimator = ObjectAnimator.ofFloat(view, "alpha", view.alpha, 0f)
        objectAnimator.interpolator = LinearInterpolator()
        objectAnimator.duration = (view.alpha * 300 / 1.0f).toLong()
        objectAnimator.start()
        objectAnimator.addListener({
            if (gone) {
                view.visibility = View.GONE
            }
        })
    }

    /**
     * 淡入
     * @param view 将要淡入的view
     */
    fun fadeIn(view: View) {
        view.visibility = View.VISIBLE
        val objectAnimator = ObjectAnimator.ofFloat(view, "alpha", view.alpha, 1.0f)
        objectAnimator.interpolator = LinearInterpolator()
        objectAnimator.duration = (300 * (1.0f - view.alpha) / 1.0f).toLong()
        objectAnimator.start()
    }


}