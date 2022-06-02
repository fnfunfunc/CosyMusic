package com.musicapp.cosymusic.fragment.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.musicapp.cosymusic.activity.PlayHistoryActivity
import com.musicapp.cosymusic.databinding.FragmentHomeBinding
import com.musicapp.cosymusic.base.BaseFragment

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

    override fun initListeners() {
        binding.ivPlayHistory.setOnClickListener {
            val intent = Intent(activity, PlayHistoryActivity::class.java)
            startActivity(intent)
        }
    }
}