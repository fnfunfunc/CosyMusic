package com.musicapp.cosymusic.fragment.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.musicapp.cosymusic.R
import com.musicapp.cosymusic.adapter.HotSearchAdapter
import com.musicapp.cosymusic.application.App
import com.musicapp.cosymusic.base.BaseFragment
import com.musicapp.cosymusic.databinding.FragmentSearchHomeBinding
import com.musicapp.cosymusic.local.SearchHistory
import com.musicapp.cosymusic.model.netease.HotSearchResponse
import com.musicapp.cosymusic.util.LogUtil
import com.musicapp.cosymusic.util.toast
import com.musicapp.cosymusic.viewmodel.SearchViewModel

/**
 * @author Eternal Epoch
 * @date 2022/6/8 23:45
 * 搜索的主页
 */

class SearchHomeFragment(private val onTextViewClickListener:(String) -> Unit): BaseFragment() {

    private lateinit var _binding: FragmentSearchHomeBinding
    private val binding get() = _binding

    private val viewModel: SearchViewModel by activityViewModels()

    private val searchHistoryList = SearchHistory.readSearchHistory()

    //热搜数据
    private val hotSearchList = mutableListOf<HotSearchResponse.Data>()

    private val hotSearchAdapter by lazy {
        HotSearchAdapter(hotSearchList){
            hotSearchList[it].searchWord.let(onTextViewClickListener)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initView() {
        viewModel.getHotSearchResponse()

        binding.rvHotSearch.layoutManager = LinearLayoutManager(App.context)
        binding.rvHotSearch.adapter = hotSearchAdapter

        val totalSize = searchHistoryList.size

        if (totalSize == 0) {
            binding.clHistory.visibility = View.GONE
            return
        }

        val historyList = if (totalSize > 15) {  //如果多于15条
            searchHistoryList.slice(0 until 15)
        } else {
            searchHistoryList
        }

        for (searchHistory in historyList) {
            val textView = TextView(App.context).run {
                defaultSettings(searchHistory)
                this
            }
            binding.flowLayout.addView(textView)
        }

        if (totalSize > 15) {
            val textView = TextView(App.context).run {
                text = "..."
                layoutParams = ViewGroup.MarginLayoutParams(
                    ViewGroup.MarginLayoutParams.WRAP_CONTENT,
                    ViewGroup.MarginLayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(4, 8, 4, 8)
                }
                setTextAppearance(R.style.search_text_style)
                setBackgroundResource(R.drawable.bg_search_text)
                setTextColor(ContextCompat.getColor(App.context, R.color.shallow_black))
                setOnClickListener {
                    binding.flowLayout.removeView(this) //移除出去
                    for (searchHistory in searchHistoryList.slice(15 until totalSize)) {
                        val textView = TextView(App.context).run {
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
        binding.ivClearHistory.setOnClickListener {
            SearchHistory.clearSearchHistory()
            searchHistoryList.clear()
            binding.clHistory.visibility = View.GONE
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initObservers() {
        viewModel.hotSearchResultData.observe(this) { result ->
            val response = result.getOrNull()
            if (response != null) {
                hotSearchList.clear()
                hotSearchList.addAll(response)
                hotSearchAdapter.notifyDataSetChanged()
            } else {
                toast("未能获取到热搜列表")
                LogUtil.e("SearchActivity", result.exceptionOrNull().toString())
            }
        }
    }

    private fun TextView.defaultSettings(searchHistory: String) {
        text = searchHistory
        layoutParams = ViewGroup.MarginLayoutParams(
            ViewGroup.MarginLayoutParams.WRAP_CONTENT,
            ViewGroup.MarginLayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(4, 8, 4, 8)
        }
        setTextAppearance(R.style.search_text_style)
        setBackgroundResource(R.drawable.bg_search_text)
        setTextColor(ContextCompat.getColor(App.context, R.color.shallow_black))
        setOnClickListener {
            searchHistory.let(onTextViewClickListener)
        }
    }

}