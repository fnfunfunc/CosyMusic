package com.musicapp.cosymusic.adapter

import android.annotation.SuppressLint
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
import com.bumptech.glide.Glide
import com.musicapp.cosymusic.R
import com.musicapp.cosymusic.application.App
import com.musicapp.cosymusic.model.netease.RecommendMenuResponse
import com.musicapp.cosymusic.util.BroadcastKString
import com.musicapp.cosymusic.util.KString
import com.musicapp.cosymusic.util.dp2px

/**
 * @author Eternal Epoch
 * @date 2022/6/2 19:42
 */
class RecommendMenuAdapter(private val menuList: List<RecommendMenuResponse.Result>):
    RecyclerView.Adapter<RecommendMenuAdapter.ViewHolder>(){

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val ivMenuCover: ImageView = view.findViewById(R.id.ivMenuCover)
        val tvMenuName: TextView = view.findViewById(R.id.tvMenuName)
        val tvPlayNum: TextView = view.findViewById(R.id.tvPlayNum)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recommend_song_menu,
         parent, false)
        val viewHolder = ViewHolder(view)
        viewHolder.itemView.setOnClickListener{
            val position = viewHolder.adapterPosition
            val songMenu = menuList[position]
            val intent = Intent(BroadcastKString.RECOMMEND_MENU_CLICKED).apply {
                putExtra(KString.SONG_MENU_CLICKED_ID, songMenu.id)
            }
            App.context.sendBroadcast(intent)
        }
        return viewHolder
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val menu = menuList[position]
        holder.tvMenuName.text = menu.name
        holder.tvPlayNum.text = "${menu.playCount / 10000}万播放"
        Glide.with(App.context).load(menu.picUrl).into(holder.ivMenuCover)
        holder.ivMenuCover.load(menu.picUrl){
            size(ViewSizeResolver(holder.ivMenuCover))
            transformations(RoundedCornersTransformation(dp2px(8f)))
            crossfade(300)
        }
    }


    override fun getItemCount() = menuList.size
}