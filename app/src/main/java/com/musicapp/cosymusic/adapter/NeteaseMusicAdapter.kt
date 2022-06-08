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
import com.musicapp.cosymusic.model.netease.standard.StdMusicData
import com.musicapp.cosymusic.util.getArtistsString
import com.musicapp.cosymusic.util.toast


/**
 * @author Eternal Epoch
 * @date 2022/5/29 16:33
 */

/**
 * @param showMore 是否展示更多的图标
 */
class NeteaseMusicAdapter(private val musicData: List<StdMusicData>,
                          private val showMore: Boolean = true,
                          private val ivMusicMoreClickedListener: (StdMusicData) -> Unit) :
    ListAdapter<StdMusicData, NeteaseMusicAdapter.ViewHolder>(DiffCallback) {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val musicName: TextView = view.findViewById(R.id.musicName)
        val artistName: TextView = view.findViewById(R.id.artistName)
        val albumImage: ImageView = view.findViewById(R.id.albumImage)
        val ivMusicInfo: ImageView = view.findViewById(R.id.ivMusicInfo)

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

        //网易云无版权的音乐及vip歌曲设为灰色
        if(music.privilege.pl == 0 || music.privilege.fee == 1){
            holder.musicName.setTextColor(ContextCompat.getColor(App.context ,R.color.grey))
            holder.artistName.setTextColor(ContextCompat.getColor(App.context ,R.color.grey))
        }else{
            holder.musicName.setTextColor(ContextCompat.getColor(App.context ,R.color.black))
            holder.artistName.setTextColor(ContextCompat.getColor(App.context ,R.color.black))
        }

        if(!showMore){
            holder.ivMusicInfo.visibility  = View.GONE
        }

        holder.itemView.setOnClickListener {
            if(music.privilege.pl == 0){
                toast("网易云暂无版权")
                return@setOnClickListener
            }

            if (music.privilege.fee == 1){
                toast("暂不支持播放网易云VIP歌曲")
                return@setOnClickListener
            }

            App.playerController.value?.let {
                it.savePlayList(musicData.toMutableList())
                it.setCurrentPlayPosition(position)
                it.playMusic(music, false)
            }

        }
        setAnimation(holder.itemView)

        holder.ivMusicInfo.setOnClickListener {
            music.let(ivMusicMoreClickedListener)
        }
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

    object DiffCallback: DiffUtil.ItemCallback<StdMusicData>(){
        override fun areItemsTheSame(
            oldItem: StdMusicData,
            newItem: StdMusicData
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: StdMusicData,
            newItem: StdMusicData
        ): Boolean {
            return oldItem == newItem
        }
    }

}