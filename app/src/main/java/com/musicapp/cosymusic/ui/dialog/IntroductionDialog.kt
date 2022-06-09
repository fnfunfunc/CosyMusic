package com.musicapp.cosymusic.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.musicapp.cosymusic.R
import com.musicapp.cosymusic.adapter.ArtistDescAdapter
import com.musicapp.cosymusic.application.App
import com.musicapp.cosymusic.databinding.DialogIntroductionBinding
import com.musicapp.cosymusic.model.netease.artist.ArtistDescResponse.Introduction

/**
 * @author Eternal Epoch
 * @date 2022/6/8 19:38
 * 歌手的详细信息
 */
class IntroductionDialog(private val introduction: List<Introduction>): BottomSheetDialogFragment() {

    private lateinit var _binding: DialogIntroductionBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogIntroductionBinding.inflate(inflater, container, false)
        binding.rvIntroduction.layoutManager = LinearLayoutManager(App.context)
        binding.rvIntroduction.adapter =  ArtistDescAdapter(introduction)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialog)
    }

}