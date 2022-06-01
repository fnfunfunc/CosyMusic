package com.musicapp.cosymusic.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.musicapp.cosymusic.adapter.NeteaseMusicAdapter
import com.musicapp.cosymusic.base.BaseActivity
import com.musicapp.cosymusic.databinding.ActivityPlayHistoryBinding
import com.musicapp.cosymusic.local.PlayHistory

class PlayHistoryActivity : BaseActivity() {

    private val binding by lazy {
        ActivityPlayHistoryBinding.inflate(layoutInflater)
    }

    private val playHistoryList by lazy{ PlayHistory.readPlayHistory() }

    private val adapter by lazy { NeteaseMusicAdapter(playHistoryList) }

    override fun initView() {
        setContentView(binding.root)

        miniPlayer = binding.miniPlayer

        binding.rvPlayHistoryList.let {
            it.layoutManager = LinearLayoutManager(this@PlayHistoryActivity)
            it.adapter = adapter
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initListeners() {
        binding.btnClear.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("确定清除播放历史？")
                .setNegativeButton("取消"){ _, _ -> }
                .setPositiveButton("确定"){ _, _ ->
                    PlayHistory.clearPlayHistory()
                    playHistoryList.clear()
                    adapter.notifyDataSetChanged()
                }
                .show()
        }
    }
}