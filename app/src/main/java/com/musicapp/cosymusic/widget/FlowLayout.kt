package com.musicapp.cosymusic.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import com.musicapp.cosymusic.util.LogUtil
import kotlin.math.max

/**
 * @author Eternal Epoch
 * @date 2022/6/1 0:21
 */
//自定义流式布局，主要用于显示搜索历史等场景
class FlowLayout: ViewGroup {

    constructor(context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet): super(context, attrs)

    companion object{
        const val TAG = "FlowLayout"
    }

    //生成默认LayoutParams
    override fun generateDefaultLayoutParams(): LayoutParams {
        return MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    }

    override fun generateLayoutParams(p: LayoutParams?): LayoutParams {
        return MarginLayoutParams(p)
    }

    //生成在布局文件中设置的LayoutParams
    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    //测量布局
    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //获取测量模式，这里有三种模式
        //wrap_content -> MeasureSpec.UNSPECIFIED
        //match_parent -> MeasureSpec.AT_MOST
        //具体值 -> MeasureSpec.EXACTLY
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        //获取布局控件的宽高，这里我们设置的宽为match_parent，高为wrap_content,
        //这是计算出来的是手机屏幕的高，当然，我们不会用到这里的高，我们要从实际子View个数算出高
        val measureWidth = MeasureSpec.getSize(widthMeasureSpec)
        val measureHeight = MeasureSpec.getSize(heightMeasureSpec)

        //定义每一行的宽，循环子View累加
        var lineWidth = 0
        var lineHeight = 0
        //定义父容器的宽高
        var width = 0
        var height = 0
        //获取所有的子View并循环
        for(i in 0 until childCount){
            //获取View
            val childView = getChildAt(i)
            //测量子view这里一定要先测量，再调用getMeasuredWidth和getMeasuredHeight
            measureChild(childView, widthMeasureSpec, heightMeasureSpec)
            val lp = if(childView.layoutParams is MarginLayoutParams){
                childView.layoutParams as MarginLayoutParams
            }else{
                MarginLayoutParams(0, 0)
            }
            //获取子View的宽+左右外边距
            val childWidth = childView.measuredWidth + lp.leftMargin + lp.rightMargin
            //获取子View的高+上下边距
            val childHeight = childView.measuredHeight + lp.topMargin + lp.bottomMargin

            if(lineWidth + childWidth > measureWidth){
                width = lineWidth
                height += lineHeight
                lineWidth = childWidth
                lineHeight = childHeight
            }else{
                lineHeight = max(lineHeight, childHeight)
                lineWidth += childWidth
            }
            //这里需要单独对最后一个元素进行处理
            if(i == childCount - 1){
                height += lineHeight
                width = max(width, lineWidth)
            }
        }
        val resWidth = if(widthMode == MeasureSpec.EXACTLY) measureWidth
                            else width
        val resHeight = if(heightMode == MeasureSpec.EXACTLY) measureHeight
                            else height
        setMeasuredDimension(resWidth, resHeight)
    }


    //子View排列
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        //定义列宽和行高
        var lineWidth = 0
        var lineHeight = 0
        //定义上，左边距
        var top = 0
        var left = 0
        for(i in 0 until childCount){
            val childView = getChildAt(i)
            val layoutParams = childView.layoutParams as MarginLayoutParams
            //因为onMeasured方法已经执行完，所以这里我们直接获取子View的宽高
            val childWidth = childView.measuredWidth + layoutParams.leftMargin + layoutParams.rightMargin
            val childHeight = childView.measuredHeight + layoutParams.topMargin + layoutParams.bottomMargin
            if(childWidth + lineWidth > measuredWidth){
                top += lineHeight
                //因为换行了，所以left置为0
                left = 0
                lineWidth = childWidth
                lineHeight = childHeight
            }else{
                lineHeight = max(lineHeight, childHeight)
                lineWidth += childWidth
            }
            //计算子View的左上右下的位置
            val lc = left + layoutParams.leftMargin
            val tc = top + layoutParams.topMargin
            val rc= lc + childView.measuredWidth
            val bc = tc + childView.measuredHeight
            //布局
            childView.layout(lc, tc, rc, bc)
            //因为布局了该子布局，所以left要累加
            left += childWidth
        }
    }
}