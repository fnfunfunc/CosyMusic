package com.musicapp.cosymusic.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.musicapp.cosymusic.fragment.search.SearchSuggestListener
import com.musicapp.cosymusic.network.Repository

/**
 * @author Eternal Epoch
 * @date 2022/5/29 15:30
 */
class SearchViewModel: ViewModel() {

    private val searchKeywordsLiveData = MutableLiveData<String>()

    val musicResult = Transformations.switchMap(searchKeywordsLiveData){ keywords ->
        Repository.getSearchMusicResult(keywords)
    }

    val artistResult = Transformations.switchMap(searchKeywordsLiveData){ keywords ->
        Repository.getSearchArtistResult(keywords)
    }

    val songMenuResult = Transformations.switchMap(searchKeywordsLiveData){ keywords ->
        Repository.getSearchSongMenuResult(keywords)
    }

    val albumResult = Transformations.switchMap(searchKeywordsLiveData){ keywords ->
        Repository.getSearchAlbumResult(keywords)
    }

    fun getSearchResponse(keywords: String){
        searchKeywordsLiveData.value = keywords
    }

    private val hotSearchLiveData = MutableLiveData<Any?>()

    val hotSearchResultData = Transformations.switchMap(hotSearchLiveData){
        Repository.getHotSearchResult()
    }

    fun getHotSearchResponse(){
        hotSearchLiveData.value = hotSearchLiveData.value
    }

    private val searchTextLiveData = MutableLiveData<String>()

    val searchSuggestData = Transformations.switchMap(searchTextLiveData){ keywords ->
        Repository.getSearchSuggest(keywords)
    }

    fun getSearchSuggest(keywords: String){
        searchTextLiveData.value = keywords
    }

    lateinit var initOnTextViewClickListener: (String) -> Unit

    val onTextViewClickListener get() = initOnTextViewClickListener

    lateinit var initOnReceiveData: () -> Unit

    val onReceiveData get() = initOnReceiveData

    lateinit var initSearchSuggestListener: SearchSuggestListener

    val searchSuggestListener get() = initSearchSuggestListener

}