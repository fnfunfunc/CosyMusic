package com.musicapp.cosymusic.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.musicapp.cosymusic.R
import com.musicapp.cosymusic.model.netease.artist.ArtistDescResponse

/**
 * @author Eternal Epoch
 * @date 2022/6/8 17:31
 */
class ArtistDescAdapter(private val introduction: List<ArtistDescResponse.Introduction>):
    RecyclerView.Adapter<ArtistDescAdapter.ViewHolder>(){

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val tvDescTitle: TextView = view.findViewById(R.id.tvDescTitle)
        val tvDescContent: TextView = view.findViewById(R.id.tvDescContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_description, parent,
        false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val desc = introduction[position]
        holder.tvDescTitle.text = desc.title
        holder.tvDescContent.text = desc.content
    }

    override fun getItemCount() = introduction.size
}