package com.musicapp.cosymusic.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.ViewSizeResolver
import coil.transform.RoundedCornersTransformation
import com.musicapp.cosymusic.R
import com.musicapp.cosymusic.application.App
import com.musicapp.cosymusic.model.netease.TopListResponse.TopList
import com.musicapp.cosymusic.util.BroadcastKString
import com.musicapp.cosymusic.util.KString
import com.musicapp.cosymusic.util.dp2px

/**
 * @author Eternal Epoch
 * @date 2022/6/5 0:28
 */
class TopListAdapter(private val topList: List<TopList>): RecyclerView.Adapter<TopListAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val tvTopListName: TextView = view.findViewById(R.id.tvTopListName)
        val tvUpdateFreq: TextView = view.findViewById(R.id.tvUpdateFreq)
        val ivTopListCover: ImageView = view.findViewById(R.id.ivTopListCover)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_top_list,
        parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val top = topList[position]
        holder.tvTopListName.text = top.name
        holder.tvUpdateFreq.text = top.updateFrequency
        holder.ivTopListCover.load(top.coverImgUrl){
            size(ViewSizeResolver(holder.ivTopListCover))
            transformations(RoundedCornersTransformation(dp2px(8f)))
            crossfade(300)
        }

        holder.itemView.setOnClickListener {
            val id = top.id
            val intent = Intent(BroadcastKString.RECOMMEND_MENU_CLICKED).apply {
                putExtra(KString.RECOMMEND_SONG_MENU_CLICKED_ID, id)
            }
            App.context.sendBroadcast(intent)
        }
    }

    override fun getItemCount() = topList.size

}