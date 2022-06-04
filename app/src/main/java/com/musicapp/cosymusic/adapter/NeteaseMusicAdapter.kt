package com.musicapp.cosymusic.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.musicapp.cosymusic.R
import com.musicapp.cosymusic.application.App
import com.musicapp.cosymusic.model.netease.StandardMusicResponse.StandardMusicData
import com.musicapp.cosymusic.service.PlayerQueue
import com.musicapp.cosymusic.util.getArtistsString
import com.musicapp.cosymusic.util.toast


/**
 * @author Eternal Epoch
 * @date 2022/5/29 16:33
 */
class NeteaseMusicAdapter(private val musicData: List<StandardMusicData>) :
    ListAdapter<StandardMusicData, NeteaseMusicAdapter.ViewHolder>(DiffCallback) {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val musicName: TextView = view.findViewById(R.id.musicName)
        val artistName: TextView = view.findViewById(R.id.artistName)
        val albumImage: ImageView = view.findViewById(R.id.albumImage)
        val ivDiamond: ImageView = view.findViewById(R.id.ivDiamond)

        fun cancelAnimation(){
            itemView.clearAnimation()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_music,
            parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val music = musicData[position]
        Glide.with(App.context).load(music.album.picUrl).into(holder.albumImage)
        holder.musicName.text = music.name
        holder.artistName.text = getArtistsString(music.artists)
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

        holder.itemView.setOnClickListener {
            if(music.privilege.pl == 0){
                toast("网易云暂无版权")
                return@setOnClickListener
            }
            App.playerController.value?.savePlayList(musicData.toMutableList())
            //权宜之计，实际上不应该让PlayerQueue被除了PlayerService之外的地方访问
            PlayerQueue.currentPosition.value = position
            App.playerController.value?.playMusic(music, false)
        }

        setAnimation(holder.itemView)
    }

    override fun getItemCount() = musicData.size

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.cancelAnimation()
    }

    private fun setAnimation(viewToAnimation: View){
        val animation = AnimationUtils.loadAnimation(viewToAnimation.context,
            R.anim.anim_recycle_item)
        viewToAnimation.startAnimation(animation)
    }

    object DiffCallback: DiffUtil.ItemCallback<StandardMusicData>(){
        override fun areItemsTheSame(
            oldItem: StandardMusicData,
            newItem: StandardMusicData
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: StandardMusicData,
            newItem: StandardMusicData
        ): Boolean {
            return oldItem == newItem
        }
    }

}