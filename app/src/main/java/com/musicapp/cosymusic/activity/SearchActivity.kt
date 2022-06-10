package com.musicapp.cosymusic.activity

import android.content.Context
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.musicapp.cosymusic.R
import com.musicapp.cosymusic.base.BaseActivity
import com.musicapp.cosymusic.base.BaseFragment
import com.musicapp.cosymusic.databinding.ActivitySearchBinding
import com.musicapp.cosymusic.fragment.search.SearchHomeFragment
import com.musicapp.cosymusic.fragment.search.SearchResultFragment
import com.musicapp.cosymusic.fragment.search.SearchSuggestFragment
import com.musicapp.cosymusic.fragment.search.SearchSuggestListener
import com.musicapp.cosymusic.local.SearchHistory
import com.musicapp.cosymusic.viewmodel.SearchViewModel

class SearchActivity : BaseActivity() {

    private val binding by lazy {
        ActivitySearchBinding.inflate(layoutInflater)
    }

    private val viewModel by lazy {
        ViewModelProvider(this).get(SearchViewModel::class.java)
    }


    companion object{
        const val SEARCH_HOME = "search_home"
        const val SEARCH_SUGGEST = "search_suggest"
        const val SEARCH_RESULT = "search_result"
    }

    private val searchHomeFragment = SearchHomeFragment()

    private val searchSuggestFragment = SearchSuggestFragment()

    private val searchResultFragment = SearchResultFragment()


    private var isSearching: Boolean = false    //判断当前是否处于搜索状态

    override fun initViewModel() {
        viewModel.initOnTextViewClickListener = fun(str: String){
            isSearching = true
            binding.searchText.setText(str)
            binding.searchText.setSelection(str.length)
            search()
        }

        viewModel.initOnReceiveData = fun(){
            isSearching = false
            if(binding.fragmentContainer.getFragment<BaseFragment>() !is SearchResultFragment) {
                toggleFragment(SEARCH_RESULT)
            }
        }

        viewModel.initSearchSuggestListener = object : SearchSuggestListener{

            override fun onSuggestClickLister(suggest: String) {
                isSearching = true
                suggest.let {
                    binding.searchText.setText(it)
                    binding.searchText.setSelection(it.length)
                }
                search()
            }

            override fun onReceiveData() {
                if(binding.fragmentContainer.getFragment<BaseFragment>() !is SearchSuggestFragment) {
                    toggleFragment(SEARCH_SUGGEST)
                }
            }
        }
    }

    override fun initView() {
        setContentView(binding.root)

        toggleFragment(SEARCH_HOME)

        miniPlayer = binding.miniPlayer

        //文本框获取焦点
        binding.searchText.apply {
            isFocusable = true
            isFocusableInTouchMode = true
            requestFocus()
        }

    }

    override fun initListeners() {
        binding.returnBtn.setOnClickListener {
            finish()
        }

        binding.searchText.apply {
            addTextChangedListener {
                val content = binding.searchText.text.toString()
                if(content.isNotEmpty()){
                    binding.cancelBtn.visibility = View.VISIBLE
                    if(isSearching) return@addTextChangedListener
                    viewModel.getSearchSuggest(content)
                    toggleFragment(SEARCH_SUGGEST)
                }else{
                    binding.cancelBtn.visibility = View.INVISIBLE
                    if(isSearching) return@addTextChangedListener
                    toggleFragment(SEARCH_HOME)
                }
            }
            setOnEditorActionListener { textView, i, keyEvent ->
                //软键盘点击了搜索
                if (i == EditorInfo.IME_ACTION_SEARCH) {
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


    private fun search() {

        //关闭软键盘
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(window.decorView.windowToken, 0)

        val keywords = binding.searchText.text.toString()
        if (keywords.isNotEmpty()) {
            viewModel.getSearchResponse(keywords)
            toggleFragment(SEARCH_RESULT)
            SearchHistory.addSearchHistory(keywords)
        }
    }

    private fun toggleFragment(msg: String){
        val transaction = supportFragmentManager.beginTransaction()
        val fragment = when(msg){
            SEARCH_HOME -> searchHomeFragment
            SEARCH_SUGGEST -> searchSuggestFragment
            SEARCH_RESULT -> searchResultFragment
            else -> searchHomeFragment
        }
        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.commit()
    }
}