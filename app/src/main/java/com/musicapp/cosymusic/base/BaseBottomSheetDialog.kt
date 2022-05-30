package com.musicapp.cosymusic.base

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup.LayoutParams
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.musicapp.cosymusic.R

/**
 * @author Eternal Epoch
 * @date 2022/5/29 22:16
 */
abstract class BaseBottomSheetDialog(context: Context): BottomSheetDialog(context, R.style.default_bottom_sheet_dialog) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window?.setGravity(Gravity.BOTTOM)
        window?.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        window?.setWindowAnimations(R.style.bottom_dialog_animation)

        initView()
        initListeners()
    }

    protected open fun initView() { }
    protected open fun initListeners() { }

    override fun onStart() {
        super.onStart()
        // for landscape mode
        val behavior: BottomSheetBehavior<*> = behavior
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

}