package com.musicapp.cosymusic.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 * @author Eternal Epoch
 * @date 2022/6/3 15:53
 */

/**
 * 一直获取焦点的文本
 */
class MarqueeTextView: AppCompatTextView {

    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet): super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int):
            super(context, attrs, defStyleAttr)

    override fun isFocused(): Boolean {
        return true
    }

}