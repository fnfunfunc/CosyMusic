package com.musicapp.cosymusic.activity

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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
import com.musicapp.cosymusic.adapter.HotSearchAdapter
import com.musicapp.cosymusic.adapter.NeteaseMusicAdapter
import com.musicapp.cosymusic.adapter.SearchSuggestAdapter
import com.musicapp.cosymusic.base.BaseActivity
import com.musicapp.cosymusic.databinding.ActivitySearchBinding
import com.musicapp.cosymusic.local.SearchHistory
import com.musicapp.cosymusic.model.netease.HotSearchResponse
import com.musicapp.cosymusic.model.netease.SearchSuggestResponse
import com.musicapp.cosymusic.model.netease.StandardMusicResponse
import com.musicapp.cosymusic.util.BroadcastKString
import com.musicapp.cosymusic.util.KString
import com.musicapp.cosymusic.util.LogUtil
import com.musicapp.cosymusic.util.toast
import com.musicapp.cosymusic.viewmodel.SearchViewModel

class SearchActivity : BaseActivity() {

    private val binding by lazy {
        ActivitySearchBinding.inflate(layoutInflater)
    }

    private val viewModel by lazy {
        ViewModelProvider(this).get(SearchViewModel::class.java)
    }

    //音乐数据
    private var musicDataList = mutableListOf<StandardMusicResponse.StandardMusicData>()

    private val musicAdapter by lazy {
        NeteaseMusicAdapter(musicDataList)
    }

    //热搜数据
    private val hotSearchList = mutableListOf<HotSearchResponse.Data>()

    private val hotSearchAdapter by lazy {
        HotSearchAdapter(hotSearchList)
    }

    //搜索建议数据
    private val searchSuggestList = mutableListOf<SearchSuggestResponse.Match>()

    private val searchSuggestAdapter by lazy {
        SearchSuggestAdapter(searchSuggestList)
    }


    private val searchHistoryList = SearchHistory.readSearchHistory()

    //广播
    private val hotSearchBroadcastReceiver = HotSearchBroadcastReceiver()

    private val searchSuggestBroadcastReceiver = SearchSuggestBroadcastReceiver()

    override fun requestData() {
        viewModel.getHotSearchResponse()
    }

    override fun initView() {
        setContentView(binding.root)

        miniPlayer = binding.miniPlayer

        binding.rvPlayList.visibility = View.GONE
        binding.rvPlayList.layoutManager = LinearLayoutManager(this)
        binding.rvPlayList.adapter = musicAdapter

        binding.rvHotSearch.layoutManager = LinearLayoutManager(this)
        binding.rvHotSearch.adapter = hotSearchAdapter

        binding.rvSearchSuggest.layoutManager = LinearLayoutManager(this)
        binding.rvSearchSuggest.adapter = searchSuggestAdapter

        val totalSize = searchHistoryList.size

        if(totalSize == 0){
            binding.clHistory.visibility = View.GONE
            return
        }

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
                    viewModel.getSearchSuggest(content)
                } else {
                    binding.cancelBtn.visibility = View.INVISIBLE
                    binding.rvSearchSuggest.visibility = View.GONE //搜索建议也置为不可见
                    binding.rvPlayList.visibility = View.GONE
                    binding.clHistory.visibility = View.VISIBLE
                    binding.clBody.visibility = View.VISIBLE
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

        binding.ivClearHistory.setOnClickListener {
            SearchHistory.clearSearchHistory()
            searchHistoryList.clear()
            binding.clHistory.visibility = View.GONE
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initObservers() {
        viewModel.musicResponseLiveData.observe(this) { result ->
            val musicResponse = result.getOrNull()
            if (musicResponse != null) {
                binding.clBody.visibility = View.GONE
                binding.clHistory.visibility = View.GONE
                binding.rvSearchSuggest.visibility = View.GONE
                binding.rvPlayList.visibility = View.VISIBLE
                musicDataList.clear()
                musicDataList.addAll(musicResponse.songs)
                musicAdapter.notifyDataSetChanged()
                binding.rvPlayList.scrollToPosition(0)  //回到最上方
            } else {
                Toast.makeText(this, "未能查询到任何歌曲信息", Toast.LENGTH_SHORT).show()
                LogUtil.e("SearchActivity", result.exceptionOrNull().toString())
            }
        }

        viewModel.hotSearchResponseData.observe(this){ result ->
            val response = result.getOrNull()
            if(response != null){
                hotSearchList.clear()
                hotSearchList.addAll(response)
                hotSearchAdapter.notifyDataSetChanged()
            }else{
                toast("未能获取到热搜列表")
                LogUtil.e("SearchActivity", result.exceptionOrNull().toString())
            }
        }

        viewModel.searchSuggestData.observe(this){ result ->
            val response = result.getOrNull()
            if(response != null){
                val matchList = response.allMatch ?: return@observe
                searchSuggestList.clear()
                searchSuggestList.addAll(matchList)
                searchSuggestAdapter.notifyDataSetChanged()
                binding.cancelBtn.visibility = View.VISIBLE
                binding.rvSearchSuggest.visibility = View.VISIBLE
                binding.clHistory.visibility = View.GONE
                binding.clBody.visibility = View.GONE
            }else{
                LogUtil.e("SearchActivity", result.exceptionOrNull().toString())
            }
        }
    }

    override fun initBroadcastReceivers() {
        val intentFilter1 = IntentFilter(BroadcastKString.HOT_SEARCH_CLICKED)
        registerReceiver(hotSearchBroadcastReceiver, intentFilter1)

        val intentFilter2 = IntentFilter(BroadcastKString.SEARCH_SUGGEST_CLICKED)
        registerReceiver(searchSuggestBroadcastReceiver, intentFilter2)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(hotSearchBroadcastReceiver)
        unregisterReceiver(searchSuggestBroadcastReceiver)
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

    inner class HotSearchBroadcastReceiver: BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent) {
            val position = p1.getIntExtra(KString.HOT_SEARCH_CLICKED_POSITION, -1)
            if(position != -1){
                binding.searchText.setText(hotSearchList[position].searchWord)
                search()
            }
        }
    }

    inner class SearchSuggestBroadcastReceiver: BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent) {
            val position = p1.getIntExtra(KString.SEARCH_SUGGEST_CLICKED_POSITION, -1)
            if(position != -1){
                binding.searchText.setText(searchSuggestList[position].keyword)
                search()
            }
        }
    }
}