package com.musicapp.cosymusic.activity

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.musicapp.cosymusic.adapter.NeteaseMusicAdapter
import com.musicapp.cosymusic.base.BaseActivity
import com.musicapp.cosymusic.databinding.ActivitySearchBinding
import com.musicapp.cosymusic.model.netease.MusicResponse
import com.musicapp.cosymusic.viewmodel.SearchViewModel

class SearchActivity : BaseActivity() {

    private val binding by lazy {
        ActivitySearchBinding.inflate(layoutInflater)
    }

    private val viewModel by lazy {
        ViewModelProvider(this).get(SearchViewModel::class.java)
    }

    private var musicDataList = mutableListOf<MusicResponse.MusicData>()

    private val musicAdapter by lazy {
        NeteaseMusicAdapter(viewModel, musicDataList)
    }

    override fun initView() {
        setContentView(binding.root)

        miniPlayer = binding.miniPlayer

        binding.rvPlayList.visibility = View.GONE
        binding.rvPlayList.layoutManager = LinearLayoutManager(this)
        binding.rvPlayList.adapter = musicAdapter
    }

    override fun initListeners() {
        binding.returnBtn.setOnClickListener {
            finish()
        }

        binding.searchText.apply {
            addTextChangedListener {
                val content = binding.searchText.text.toString()
                if (content.isNotEmpty()) {
                    binding.cancelBtn.visibility = View.VISIBLE
                } else {
                    binding.cancelBtn.visibility = View.INVISIBLE
                }
            }
            setOnEditorActionListener { textView, i, keyEvent ->
                //软键盘点击了搜索
                if(i == EditorInfo.IME_ACTION_SEARCH){
                    search()
                }
                false
            }
        }

        binding.cancelBtn.setOnClickListener {
            binding.searchText.setText("")
        }

        binding.searchBtn.setOnClickListener {
            search()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initObservers() {
        viewModel.musicResponseLiveData.observe(this) { result ->
            val musicResponse = result.getOrNull()
            if (musicResponse != null) {
                binding.clBody.visibility = View.GONE
                binding.rvPlayList.visibility = View.VISIBLE
                musicDataList.clear()
                musicDataList.addAll(musicResponse.songs)
                musicAdapter.notifyDataSetChanged()
                binding.rvPlayList.scrollToPosition(0)  //回到最上方
            } else {
                Toast.makeText(this, "未能查询到任何歌曲信息", Toast.LENGTH_SHORT).show()
                Log.e("SearchActivity", result.exceptionOrNull().toString())
            }
        }


    }


    private fun search(){
        //关闭软键盘
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(window.decorView.windowToken, 0)

        val keywords = binding.searchText.text.toString()
        if (keywords.isNotEmpty()) {
            viewModel.getSearchResponse(keywords)
        }
    }
}