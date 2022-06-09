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
class HotSearchAdapter(private val hotSearchList: List<HotSearchResponse.Data>,
                       private val itemViewClickListener: (Int) -> Unit):
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
        return ViewHolder(view)
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
        //以下这样写貌似有bug，会出现别的排名也变红的现象
        /*if(position < 3){
            holder.tvRank.setTextColor(ContextCompat.getColor(App.context, R.color.app_theme_color))
        }*/
        holder.tvRank.let {
            if(it.text == "1" || it.text == "2" || it.text == "3"){
                it.setTextColor(ContextCompat.getColor(App.context, R.color.app_theme_color))
            }else{
                it.setTextColor(ContextCompat.getColor(App.context, R.color.black))
            }
        }

        holder.itemView.setOnClickListener {
            position.let(itemViewClickListener)
        }
    }

    override fun getItemCount() = hotSearchList.size
}