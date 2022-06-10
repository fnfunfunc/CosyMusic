package com.musicapp.cosymusic.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.ViewSizeResolver
import coil.transform.RoundedCornersTransformation
import com.bumptech.glide.Glide
import com.musicapp.cosymusic.R
import com.musicapp.cosymusic.application.App
import com.musicapp.cosymusic.model.netease.SearchSongMenuResponse
import com.musicapp.cosymusic.util.dp2px

/**
 * @author Eternal Epoch
 * @date 2022/6/10 0:16
 */
class SongMenuAdapter(private val songMenuList: List<SearchSongMenuResponse.Playlists>,
                      private val onItemViewClickList: (Long) -> Unit):
    RecyclerView.Adapter<SongMenuAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val ivSongMenuCover: ImageView = view.findViewById(R.id.ivSongMenuCover)
        val tvSongMenuName: TextView = view.findViewById(R.id.tvSongMenuName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_song_menu,
        parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val songMenu = songMenuList[position]
        holder.tvSongMenuName.text = songMenu.name
        holder.ivSongMenuCover.load(songMenu.coverImgUrl){
            size(ViewSizeResolver(holder.ivSongMenuCover))
            transformations(RoundedCornersTransformation(dp2px(8f)))
            crossfade(300)
        }
        holder.itemView.setOnClickListener {
            songMenu.id.let(onItemViewClickList)
        }

        setAnimation(holder.itemView)
    }

    override fun getItemCount() = songMenuList.size

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        holder.itemView.clearAnimation()
    }

    private fun setAnimation(viewToAnimation: View){
        val animation = AnimationUtils.loadAnimation(App.context, R.anim.anim_recycle_item)
        viewToAnimation.startAnimation(animation)
    }
}