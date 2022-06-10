package com.musicapp.cosymusic.fragment.artist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.musicapp.cosymusic.activity.AlbumActivity
import com.musicapp.cosymusic.adapter.AlbumAdapter
import com.musicapp.cosymusic.adapter.NeteaseMusicAdapter
import com.musicapp.cosymusic.base.BaseFragment
import com.musicapp.cosymusic.databinding.FragmentArtistAlbumBinding
import com.musicapp.cosymusic.ui.dialog.MusicMoreDialog
import com.musicapp.cosymusic.util.KString
import com.musicapp.cosymusic.util.LogUtil
import com.musicapp.cosymusic.util.toast
import com.musicapp.cosymusic.viewmodel.ArtistViewModel

/**
 * @author Eternal Epoch
 * @date 2022/6/7 22:51
 */
class ArtistAlbumFragment: BaseFragment() {

    private lateinit var _binding: FragmentArtistAlbumBinding
    private val binding get() = _binding

    private val viewModel: ArtistViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArtistAlbumBinding.inflate(inflater, container, false)
        binding.lottieLoading.repeatCount = -1
        binding.lottieLoading.playAnimation()
        binding.rvArtistAlbum.layoutManager = LinearLayoutManager(activity)
        return binding.root
    }

    override fun initObservers() {
        viewModel.artistAlbumResult.observe(viewLifecycleOwner){ result ->
            val response = result.getOrNull()
            if(response != null){
                binding.rvArtistAlbum.adapter = AlbumAdapter(response){ id ->
                    val intent = Intent(activity, AlbumActivity::class.java).apply {
                        putExtra(KString.ALBUM_ID, id)
                    }
                    startActivity(intent)
                }
                binding.lottieLoading.pauseAnimation()
                binding.lottieLoading.visibility = View.GONE
            }else{
                toast("获取歌手专辑信息失败")
            }
        }
    }


}