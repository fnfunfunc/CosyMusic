package com.musicapp.cosymusic.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.musicapp.cosymusic.R
import com.musicapp.cosymusic.adapter.PlayListDialogAdapter
import com.musicapp.cosymusic.application.App
import com.musicapp.cosymusic.databinding.DialogPlayListBinding

/**
 * @author Eternal Epoch
 * @date 2022/6/4 21:48
 */
class PlayListDialog: BottomSheetDialogFragment() {

    private lateinit var _binding: DialogPlayListBinding

    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogPlayListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialog)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.rvPlayList.run {
            layoutManager = LinearLayoutManager(activity)
            App.playerController.value?.getCurrentPlayList()?.let {
                adapter = PlayListDialogAdapter(it)
                binding.tvPlayListItemNum.text = getString(R.string.play_list_num, it.size)
            }
            scrollToPosition(App.playerController.value?.getCurrentPlayPosition() ?: 0)
        }

        App.playerController.value?.musicData?.observe(viewLifecycleOwner){ musicData ->
            binding.rvPlayList.run {
                App.playerController.value?.getCurrentPlayList()?.let {
                    adapter = PlayListDialogAdapter(it)
                    binding.tvPlayListItemNum.text = getString(R.string.play_list_num, it.size)
                }
                scrollToPosition(App.playerController.value?.getCurrentPlayPosition() ?: 0)
            }
        }
    }
}