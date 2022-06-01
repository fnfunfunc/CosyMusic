package com.musicapp.cosymusic.activity

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.musicapp.cosymusic.R
import com.musicapp.cosymusic.adapter.NeteaseMusicAdapter
import com.musicapp.cosymusic.base.BaseActivity
import com.musicapp.cosymusic.databinding.ActivitySearchBinding
import com.musicapp.cosymusic.local.SearchHistory
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
        NeteaseMusicAdapter(musicDataList)
    }

    private val searchHistoryList = SearchHistory.readSearchHistory()

    override fun initView() {
        setContentView(binding.root)

        miniPlayer = binding.miniPlayer

        binding.rvPlayList.visibility = View.GONE
        binding.rvPlayList.layoutManager = LinearLayoutManager(this)
        binding.rvPlayList.adapter = musicAdapter

        val totalSize = searchHistoryList.size
        val historyList =  if(totalSize > 15){  //如果多于15条
            searchHistoryList.slice(0 until 15)
        }else{
            searchHistoryList
        }

        for(searchHistory in historyList){
            val textView = TextView(this).run {
                defaultSettings(searchHistory)
                this
            }
            binding.flowLayout.addView(textView)
        }

        if(totalSize > 15){
            val textView = TextView(this).run {
                text = "..."
                layoutParams = MarginLayoutParams(MarginLayoutParams.WRAP_CONTENT,
                    MarginLayoutParams.WRAP_CONTENT).apply {
                    setMargins(4, 8, 4, 8)
                }
                setTextAppearance(R.style.search_text_style)
                setBackgroundResource(R.drawable.bg_search_text)
                setTextColor(ContextCompat.getColor(this@SearchActivity, R.color.shallow_black))
                setOnClickListener {
                    binding.flowLayout.removeView(this) //移除出去
                    for(searchHistory in searchHistoryList.slice(15 until totalSize)){
                        val textView = TextView(this@SearchActivity).run {
                            defaultSettings(searchHistory)
                            this
                        }
                        binding.flowLayout.addView(textView)
                    }
                }
                this
            }
            binding.flowLayout.addView(textView)
        }
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
                binding.clHistory.visibility = View.GONE
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
            SearchHistory.addSearchHistory(keywords)
        }
    }

    private fun TextView.defaultSettings(searchHistory: String){
        text = searchHistory
        layoutParams = MarginLayoutParams(MarginLayoutParams.WRAP_CONTENT,
            MarginLayoutParams.WRAP_CONTENT).apply {
            setMargins(4, 8, 4, 8)
        }
        setTextAppearance(R.style.search_text_style)
        setBackgroundResource(R.drawable.bg_search_text)
        setTextColor(ContextCompat.getColor(this@SearchActivity, R.color.shallow_black))
        setOnClickListener {
            binding.searchText.setText(searchHistory)
            search()
        }
    }
}