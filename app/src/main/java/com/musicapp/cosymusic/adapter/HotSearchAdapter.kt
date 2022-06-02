package com.musicapp.cosymusic.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.musicapp.cosymusic.R
import com.musicapp.cosymusic.application.App
import com.musicapp.cosymusic.model.netease.HotSearchResponse
import com.musicapp.cosymusic.util.BroadcastKString
import com.musicapp.cosymusic.util.KString
import com.musicapp.cosymusic.util.LogUtil

/**
 * @author Eternal Epoch
 * @date 2022/6/1 17:44
 */
class HotSearchAdapter(private val hotSearchList: List<HotSearchResponse.Data>):
    RecyclerView.Adapter<HotSearchAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val tvRank: TextView = view.findViewById(R.id.tvRank)
        val tvTopicName: TextView = view.findViewById(R.id.tvTopicName)
        val ivHotIcon: ImageView = view.findViewById(R.id.ivHotIcon)
        val tvHotValue: TextView = view.findViewById(R.id.tvHotValue)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_hot_search,
        parent, false)
        val holder = ViewHolder(view)
        holder.itemView.setOnClickListener {
            val position = holder.adapterPosition
            val intent = Intent(BroadcastKString.HOT_SEARCH_CLICKED)
            intent.putExtra(KString.HOT_SEARCH_CLICKED_POSITION, position)
            App.context.sendBroadcast(intent)
        }
        return holder
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hotSearch = hotSearchList[position]
        holder.tvRank.text = (position + 1).toString()
        holder.tvTopicName.text = hotSearch.searchWord
        holder.tvHotValue.text = hotSearch.score.toString()
        if(hotSearch.iconUrl != null){
            Glide.with(App.context).load(hotSearch.iconUrl).into(holder.ivHotIcon)
        }
        if(position < 3){
            holder.tvRank.setTextColor(ContextCompat.getColor(App.context, R.color.app_theme_color))
        }
    }

    override fun getItemCount() = hotSearchList.size
}