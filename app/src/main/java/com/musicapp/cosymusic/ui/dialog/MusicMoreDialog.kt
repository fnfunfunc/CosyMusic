package com.musicapp.cosymusic.ui.dialog

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.musicapp.cosymusic.R
import com.musicapp.cosymusic.application.App
import com.musicapp.cosymusic.databinding.DialogMusicMoreBinding
import com.musicapp.cosymusic.model.netease.standard.StdMusicData
import com.musicapp.cosymusic.util.toast

/**
 * @author Eternal Epoch
 * @date 2022/6/5 15:04
 */
class MusicMoreDialog(
    private val activity: Activity,
    private val musicData: StdMusicData,
    private val musicDeleteListener: () -> Unit
) : BottomSheetDialogFragment() {

    private lateinit var _binding: DialogMusicMoreBinding
    private val binding get() = _binding

    private var isCollected = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogMusicMoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialog)
    }

    override fun onStart() {
        super.onStart()
        isCollected = App.playerController.value?.isInMyFavorite(musicData) ?: false
        binding.tvAddFavor.text =  if(isCollected){
            getString(R.string.cancel_collect)
        }else{
            getString(R.string.add_to_my_favor)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.apply {


            llNextSong.setOnClickListener {
                App.playerController.value?.addToNextPlay(musicData)
                toast("成功添加到下一首播放")
                dismiss()   //退出Dialog
            }

            llAddFavor.setOnClickListener {
                if(!isCollected){
                    App.playerController.value?.addToMyFavorite(musicData)
                    toast("成功添加到我喜欢的音乐")
                }else{
                    App.playerController.value?.removeFromMyFavorite(musicData)
                    toast("成功从我喜欢的音乐中移除")
                }
                dismiss()
            }
        }
    }

}