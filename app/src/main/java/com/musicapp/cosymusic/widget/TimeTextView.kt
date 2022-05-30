package com.musicapp.cosymusic.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.musicapp.cosymusic.R
import com.musicapp.cosymusic.application.App
import com.musicapp.cosymusic.util.TimeUtil

/**
 * @author Eternal Epoch
 * @date 2022/5/29 23:52
 */
class TimeTextView: View {

    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet): super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int):
            super(context, attrs, defStyleAttr)

    companion object{
        const val TEXT_SIZE = 30
    }

    private var text = "00:00"
    private var textAlign = Paint.Align.LEFT

    private val textColor = ContextCompat.getColor(App.context, R.color.black)


    private val textPaint = Paint().apply {
        isAntiAlias = true
        textSize = TEXT_SIZE.toFloat()
        color = textColor
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        textPaint.textAlign = textAlign
        var startX = 0f
        if(textAlign == Paint.Align.RIGHT){
            startX = width.toFloat()
        }
        canvas.drawText(text, startX, TEXT_SIZE.toFloat(), textPaint)
    }

    fun setText(duration: Int){
        text = TimeUtil.formatDuration(duration)
        invalidate()
    }

    fun setAlignRight(){
        textAlign = Paint.Align.RIGHT
    }

}