package com.musicapp.cosymusic.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.musicapp.cosymusic.R
import com.musicapp.cosymusic.application.App
import com.musicapp.cosymusic.model.netease.MusicResponse
import com.musicapp.cosymusic.service.PlayerQueue
import com.musicapp.cosymusic.util.toast
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
        val ivDiamond: ImageView = view.findViewById(R.id.ivDiamond)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_music,
            parent, false)
        val viewHolder = ViewHolder(view)
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            val music = musicData[position]
            if(music.privilege.pl == 0){
                toast("网易云暂无版权")
                return@setOnClickListener
            }
            //App.playQueue.clear()
            //App.playQueue.addAll(musicData)
            App.playerController.value?.savePlayList(musicData.toMutableList())
            //权宜之计，实际上不应该让PlayerQueue被除了PlayerService之外的地方访问
            PlayerQueue.currentPosition.value = position
            App.playerController.value?.playMusic(music, false)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val music = musicData[position]
        Glide.with(App.context).load(music.album.picUrl).into(holder.albumImage)
        holder.musicName.text = music.name
        holder.artistName.text = music.artist?.get(0)?.name
        holder.ivDiamond.visibility = if(music.privilege.fee == 1){
            View.VISIBLE
        }else{
            View.INVISIBLE
        }
        //网易云无版权的音乐设为灰色
        if(music.privilege.pl == 0){
            holder.musicName.setTextColor(ContextCompat.getColor(App.context ,R.color.grey))
            holder.artistName.setTextColor(ContextCompat.getColor(App.context ,R.color.grey))
        }else{
            holder.musicName.setTextColor(ContextCompat.getColor(App.context ,R.color.black))
            holder.artistName.setTextColor(ContextCompat.getColor(App.context ,R.color.black))
        }
    }

    override fun getItemCount() = musicData.size

}