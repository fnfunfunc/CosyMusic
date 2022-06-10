package com.musicapp.cosymusic.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.musicapp.cosymusic.R
import com.musicapp.cosymusic.model.netease.standard.StdArtistInfo

/**
 * @author Eternal Epoch
 * @date 2022/6/8 20:42
 */
class ArtistInfoAdapter(private val artists: List<StdArtistInfo>,
                        private val itemClickListener: (Long) -> Unit):
    RecyclerView.Adapter<ArtistInfoAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val tvArtistName: TextView = view.findViewById(R.id.tvArtistName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_artist_info,
            parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val artist = artists[position]
        holder.tvArtistName.text = artist.name
        holder.itemView.setOnClickListener {
            artist.artistId?.let(itemClickListener)
        }
    }

    override fun getItemCount() = artists.size
}