package com.musicapp.cosymusic.fragment.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.musicapp.cosymusic.adapter.SearchSuggestAdapter
import com.musicapp.cosymusic.base.BaseFragment
import com.musicapp.cosymusic.databinding.FragmentSearchSuggestBinding
import com.musicapp.cosymusic.model.netease.SearchSuggestResponse
import com.musicapp.cosymusic.util.LogUtil
import com.musicapp.cosymusic.viewmodel.SearchViewModel

/**
 * @author Eternal Epoch
 * @date 2022/6/9 0:14
 */
class SearchSuggestFragment: BaseFragment() {

    private lateinit var _binding: FragmentSearchSuggestBinding
    private val binding get() = _binding

    private val viewModel: SearchViewModel by activityViewModels()

    private val searchSuggestList = mutableListOf<SearchSuggestResponse.Match>()

    private val searchSuggestAdapter by lazy {
        SearchSuggestAdapter(searchSuggestList){ position ->
            searchSuggestList[position].keyword.let{ word ->
                viewModel.searchSuggestListener.onSuggestClickLister(word)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchSuggestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initView() {
        binding.rvSearchSuggest.layoutManager = LinearLayoutManager(activity)
        binding.rvSearchSuggest.adapter = searchSuggestAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initObservers() {
        viewModel.searchSuggestData.observe(this) { result ->
            val response = result.getOrNull()
            if (response != null) {
                val matchList = response.allMatch ?: return@observe
                searchSuggestList.clear()
                searchSuggestList.addAll(matchList)
                searchSuggestAdapter.notifyDataSetChanged()
                viewModel.searchSuggestListener.onReceiveData()
            } else {
                LogUtil.e("SearchActivity", result.exceptionOrNull().toString())
            }
        }
    }
}

interface SearchSuggestListener{

    //搜索建议被点击时
    fun onSuggestClickLister(suggest: String)

    //Observer收到信息
    fun onReceiveData()
}