package com.musicapp.cosymusic.base

import android.os.Bundle
import androidx.fragment.app.Fragment

/**
 * @author Eternal Epoch
 * @date 2022/5/28 16:58
 */
abstract class BaseFragment: Fragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        initListeners()
        initObservers()
    }

    protected open fun initView() { }
    protected open fun initListeners() { }
    protected open fun initObservers() { }
}