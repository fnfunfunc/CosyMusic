package com.musicapp.cosymusic.fragment.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.musicapp.cosymusic.databinding.FragmentDiscoverBinding
import com.musicapp.cosymusic.fragment.BaseFragment

/**
 * @author Eternal Epoch
 * @date 2022/5/28 17:02
 */
class DiscoverFragment: BaseFragment() {

    private lateinit var _binding: FragmentDiscoverBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiscoverBinding.inflate(inflater, container, false)
        return binding.root
    }
}