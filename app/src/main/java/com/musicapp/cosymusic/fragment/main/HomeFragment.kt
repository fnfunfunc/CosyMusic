package com.musicapp.cosymusic.fragment.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.musicapp.cosymusic.databinding.FragmentHomeBinding
import com.musicapp.cosymusic.fragment.BaseFragment

/**
 * @author Eternal Epoch
 * @date 2022/5/28 16:55
 */
class HomeFragment: BaseFragment() {

    private lateinit var _binding: FragmentHomeBinding

    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


}