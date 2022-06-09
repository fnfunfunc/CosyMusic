package com.musicapp.cosymusic.ui.dialog

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.musicapp.cosymusic.R
import com.musicapp.cosymusic.activity.ArtistActivity
import com.musicapp.cosymusic.adapter.ArtistAdapter
import com.musicapp.cosymusic.databinding.DialogArtistBinding
import com.musicapp.cosymusic.model.netease.standard.StdArtistInfo
import com.musicapp.cosymusic.util.KString

/**
 * @author Eternal Epoch
 * @date 2022/6/8 20:34
 * 如果一首歌有多个歌手，那么点击歌手后应该弹出该Dialog
 */
class ArtistDialog(val artists: List<StdArtistInfo>): BottomSheetDialogFragment() {

    private lateinit var _binding: DialogArtistBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogArtistBinding.inflate(inflater, container, false)
        binding.rvArtist.layoutManager = LinearLayoutManager(activity)
        binding.rvArtist.adapter = ArtistAdapter(artists){
            val intent = Intent(activity, ArtistActivity::class.java).apply {
                putExtra(KString.ARTIST_ID, it)
            }
            startActivity(intent)
        }
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialog)
    }

}