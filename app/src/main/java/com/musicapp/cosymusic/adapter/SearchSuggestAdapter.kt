package com.musicapp.cosymusic.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.musicapp.cosymusic.R
import com.musicapp.cosymusic.application.App
import com.musicapp.cosymusic.model.netease.SearchSuggestResponse
import com.musicapp.cosymusic.util.BroadcastKString
import com.musicapp.cosymusic.util.KString

/**
 * @author Eternal Epoch
 * @date 2022/6/2 14:30
 */
class SearchSuggestAdapter(private val matchList: List<SearchSuggestResponse.Match>):
    RecyclerView.Adapter<SearchSuggestAdapter.ViewHolder>(){

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val tvSearchSuggest: TextView = view.findViewById(R.id.tvSearchSuggest)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search_suggest,
        parent, false)
        val viewHolder = ViewHolder(view)
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            val intent = Intent(BroadcastKString.SEARCH_SUGGEST_CLICKED).apply {
                putExtra(KString.SEARCH_SUGGEST_CLICKED_POSITION, position)
            }
            App.context.sendBroadcast(intent)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val match = matchList[position]
        holder.tvSearchSuggest.text = match.keyword
    }

    override fun getItemCount() = matchList.size

}