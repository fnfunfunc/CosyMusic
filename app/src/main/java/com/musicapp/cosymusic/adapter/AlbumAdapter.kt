package com.musicapp.cosymusic.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.musicapp.cosymusic.R
import com.musicapp.cosymusic.application.App
import com.musicapp.cosymusic.model.netease.standard.StdAlbumDetailInfo
import com.musicapp.cosymusic.util.convertTimeStampToDate
import com.musicapp.cosymusic.util.getArtistsString

/**
 * @author Eternal Epoch
 * @date 2022/6/7 23:02
 */
class AlbumAdapter(private val albumList: List<StdAlbumDetailInfo>, private val itemClickListener: (Long) -> Unit):
    RecyclerView.Adapter<AlbumAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val tvAlbumName: TextView = view.findViewById(R.id.tvAlbumName)
        val ivAlbumCover: ImageView = view.findViewById(R.id.ivAlbumCover)
        val tvAlbumDesc: TextView = view.findViewById(R.id.tvAlbumDesc)
        fun cancelAnimation(){
            itemView.clearAnimation()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_album,
            parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val album = albumList[position]
        holder.tvAlbumName.text = album.name
        Glide.with(App.context).load(album.picUrl).into(holder.ivAlbumCover)
        holder.tvAlbumDesc.text = "${getArtistsString(album.artists)}  ${convertTimeStampToDate(album.publishTime)}"

        holder.itemView.setOnClickListener{
            album.id.let(itemClickListener)
        }

        setAnimation(holder.itemView)
    }

    override fun getItemCount() = albumList.size

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.cancelAnimation()
    }

    private fun setAnimation(viewToAnimation: View){
        val animation = AnimationUtils.loadAnimation(viewToAnimation.context,
            R.anim.anim_recycle_item)
        viewToAnimation.startAnimation(animation)
    }
}