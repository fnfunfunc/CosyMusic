package com.musicapp.cosymusic.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.musicapp.cosymusic.R
import com.musicapp.cosymusic.application.App
import com.musicapp.cosymusic.model.netease.StandardMusicResponse.StandardMusicData
import com.musicapp.cosymusic.util.getArtistsString

/**
 * @author Eternal Epoch
 * @date 2022/6/4 22:01
 */
class PlayListDialogAdapter(private val musicList: List<StandardMusicData>):
    RecyclerView.Adapter<PlayListDialogAdapter.ViewHolder>(){

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val tvMusicName: TextView = view.findViewById(R.id.tvMusicName)
        val tvArtistName: TextView = view.findViewById(R.id.tvArtistName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_music_in_play_list_dialog, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val music = musicList[position]
        val currentPlayPosition = App.playerController.value?.getCurrentPlayPosition()
        holder.tvMusicName.text = music.name
        holder.tvArtistName.text = getArtistsString(music.artists)
        if(position == currentPlayPosition){
            holder.tvMusicName.setTextColor(ContextCompat.getColor(App.context, R.color.app_theme_color))
            holder.tvArtistName.setTextColor(ContextCompat.getColor(App.context, R.color.app_theme_color))
        }else{
            holder.tvMusicName.setTextColor(ContextCompat.getColor(App.context, R.color.black))
            holder.tvArtistName.setTextColor(ContextCompat.getColor(App.context, R.color.black))
        }

        holder.itemView.setOnClickListener {
            App.playerController.value?.let {
                it.setCurrentPlayPosition(position)
                it.playMusic(music, false)
            }
        }
    }

    override fun getItemCount() = musicList.size ?: 0

}