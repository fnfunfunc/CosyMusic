package com.musicapp.cosymusic.activity

import android.os.Build
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.ViewSizeResolver
import coil.transform.BlurTransformation
import coil.transform.RoundedCornersTransformation
import com.musicapp.cosymusic.R
import com.musicapp.cosymusic.adapter.NeteaseMusicAdapter
import com.musicapp.cosymusic.base.BaseActivity
import com.musicapp.cosymusic.databinding.ActivitySongMenuBinding
import com.musicapp.cosymusic.model.netease.StandardMusicResponse
import com.musicapp.cosymusic.util.*
import com.musicapp.cosymusic.util.StatusBarUtil.getStatusBarHeight
import com.musicapp.cosymusic.viewmodel.SongMenuViewModel

class SongMenuActivity : BaseActivity() {

    private val binding by lazy {
        ActivitySongMenuBinding.inflate(layoutInflater)
    }

    private val viewModel by lazy {
        ViewModelProvider(this).get(SongMenuViewModel::class.java)
    }

    private val musicList = mutableListOf<StandardMusicResponse.StandardMusicData>()

    private var tracksIdList = emptyList<Long>()

    private val adapter by lazy {
        NeteaseMusicAdapter(musicList)
    }

    override fun initView() {
        setContentView(binding.root)

        //屏幕适配
        (binding.clTitleBar.layoutParams as ConstraintLayout.LayoutParams).apply {
            topMargin = getStatusBarHeight(window, this@SongMenuActivity)
        }

        miniPlayer = binding.miniPlayer

        binding.lottieLoading.repeatCount = -1
        binding.lottieLoading.playAnimation()

        binding.ivPlayAll.setColorFilter(ContextCompat.getColor(this, R.color.app_theme_color))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        }

        val id = intent.getLongExtra(KString.RECOMMEND_SONG_MENU_CLICKED_ID, 0L)

        viewModel.getSongMenuById(id)

        binding.rvMenuSong.layoutManager = LinearLayoutManager(this)
        binding.rvMenuSong.adapter = adapter


        var rvMenuSongScrollY = 0
        binding.rvMenuSong.setOnScrollChangeListener { _, _, _, _, oldScrollY ->
            rvMenuSongScrollY += oldScrollY
            if(rvMenuSongScrollY < 0){
                binding.tvTitle.let {
                    if(it.text == getString(R.string.song_menu)){
                        it.text = binding.tvMenuName.text
                    }
                }
            }else{
                binding.tvTitle.text = getString(R.string.song_menu)
            }
        }
    }

    override fun initListeners() {
        binding.rvMenuSong.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //暂停滑动时，如果已经加载到底部
                    if (binding.rvMenuSong.isSlideToBottom()) {
                        getMusic()
                    }
                }
            }
        })

        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    override fun initObservers() {
        viewModel.songMenuResponse.observe(this) { result ->
            val response = result.getOrNull()
            if (response != null) {
                tracksIdList = response.trackIds.map { it.id }
                getMusic()
                binding.ivMenuCover.load(response.coverImgUrl) {
                    size(ViewSizeResolver(binding.ivMenuCover))
                    transformations(RoundedCornersTransformation(dp2px(8f)))
                    crossfade(300)
                }
                binding.ivBackground.load(response.coverImgUrl) {
                    size(ViewSizeResolver(binding.ivBackground))
                    transformations(
                        BlurTransformation(
                            this@SongMenuActivity,
                            25f,
                            10f
                        )
                    )
                    crossfade(300)
                }
                binding.tvMenuName.text = response.name
                binding.tvMenuDesc.text = response.description
                binding.tvPlayAll.text = getString(R.string.play_all, response.trackCount)
            } else {
                toast("获取歌单详细信息失败")
                LogUtil.e("SongMenuActivity", result.exceptionOrNull().toString())
            }
        }

        viewModel.menuMusicResponse.observe(this) { result ->
            val response = result.getOrNull()
            if (response != null) {
                for (music in response) {
                    musicList.add(music.toStandard())
                }
                binding.lottieLoading.pauseAnimation()
                binding.clLoading.visibility = View.GONE
                adapter.notifyItemRangeChanged(musicList.size - response.size - 1,
                    response.size)
                getMusic()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun getMusic(){
        tracksIdList = if (tracksIdList.size >= 50) {
            viewModel.getMusicByTracksId(tracksIdList.slice(0 until 50))
            tracksIdList.slice(50 until tracksIdList.size)
        } else {
            viewModel.getMusicByTracksId(tracksIdList)
            emptyList()
        }
    }

}