package com.musicapp.cosymusic.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.musicapp.cosymusic.R
import com.musicapp.cosymusic.application.MainApplication
import com.musicapp.cosymusic.model.netease.MusicResponse
import com.musicapp.cosymusic.viewmodel.SearchViewModel


/**
 * @author Eternal Epoch
 * @date 2022/5/29 16:33
 */
class NeteaseMusicAdapter(private val viewModel: SearchViewModel,
                          private val musicData: List<MusicResponse.MusicData>) :
    RecyclerView.Adapter<NeteaseMusicAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val musicName: TextView = view.findViewById(R.id.musicName)
        val artistName: TextView = view.findViewById(R.id.artistName)
        val albumImage: ImageView = view.findViewById(R.id.albumImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_music,
            parent, false)
        val viewHolder = ViewHolder(view)
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            val music = musicData[position]
            MainApplication.playSongData.value = music
            viewModel.getMusicSourceById(music.id)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val music = musicData[position]
        Glide.with(MainApplication.context).load(music.album.picUrl).into(holder.albumImage)
        holder.musicName.text = music.name
        holder.artistName.text = music.artist?.get(0)?.name
    }

    override fun getItemCount() = musicData.size

}