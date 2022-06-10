package com.musicapp.cosymusic.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.musicapp.cosymusic.R
import com.musicapp.cosymusic.application.App
import com.musicapp.cosymusic.model.netease.standard.StdArtistData
import de.hdodenhof.circleimageview.CircleImageView

/**
 * @author Eternal Epoch
 * @date 2022/6/9 19:35
 */
class ArtistDataAdapter(private val artistList: List<StdArtistData>,
                        private val itemViewClickListener: (Long) -> Unit):
    RecyclerView.Adapter<ArtistDataAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val ivArtistCover: CircleImageView = view.findViewById(R.id.ivArtistCover)
        val tvArtistName: TextView = view.findViewById(R.id.tvArtistName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_artist_data,
        parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val artist = artistList[position]
        Glide.with(App.context).load(artist.picUrl).into(holder.ivArtistCover)
        holder.tvArtistName.text = artist.name

        holder.itemView.setOnClickListener {
            artist.artistId?.let(itemViewClickListener)
        }

        setAnimation(holder.itemView)
    }

    override fun getItemCount() = artistList.size

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.clearAnimation()
    }

    private fun setAnimation(viewToAnimation: View){
        val animation = AnimationUtils.loadAnimation(App.context, R.anim.anim_recycle_item)
        viewToAnimation.startAnimation(animation)
    }
}