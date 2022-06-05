package com.musicapp.cosymusic.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.musicapp.cosymusic.adapter.TopListAdapter
import com.musicapp.cosymusic.base.BaseActivity
import com.musicapp.cosymusic.databinding.ActivityTopListBinding
import com.musicapp.cosymusic.util.BroadcastKString
import com.musicapp.cosymusic.util.KString
import com.musicapp.cosymusic.util.toast
import com.musicapp.cosymusic.viewmodel.TopListViewModel

class TopListActivity : BaseActivity() {

    private val binding by lazy {
        ActivityTopListBinding.inflate(layoutInflater)
    }

    private val viewModel by lazy {
        ViewModelProvider(this).get(TopListViewModel::class.java)
    }

    private val topListClickedReceiver = TopListClickedReceiver()

    override fun initView() {
        setContentView(binding.root)
        miniPlayer = binding.miniPlayer
        viewModel.getTopListResponse()
        binding.rvTopList.layoutManager = LinearLayoutManager(this)
    }

    override fun initObservers() {
        viewModel.topListResponse.observe(this) { result ->
            val response = result.getOrNull()
            if (response != null) {
                binding.rvTopList.adapter = TopListAdapter(response)
            } else {
                toast("获取排行榜信息失败")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter(BroadcastKString.RECOMMEND_MENU_CLICKED)
        registerReceiver(topListClickedReceiver, intentFilter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(topListClickedReceiver)
    }

    inner class TopListClickedReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val id = intent.getLongExtra(KString.RECOMMEND_SONG_MENU_CLICKED_ID, 0L)
            if (id != 0L) {
                val intentToSongMenu = Intent(this@TopListActivity,
                    SongMenuActivity::class.java).apply {
                    putExtra(KString.RECOMMEND_SONG_MENU_CLICKED_ID, id)
                }
                startActivity(intentToSongMenu)
            }
        }
    }
}