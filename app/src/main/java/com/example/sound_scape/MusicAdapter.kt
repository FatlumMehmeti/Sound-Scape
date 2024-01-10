package com.example.sound_scape

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.sound_scape.databinding.MusicViewBinding
import com.example.sound_scape.player.MusicView
import com.squareup.picasso.Picasso

class MusicAdapter(thisContext: Context, mainactivity: MainActivity, musicList: ArrayList<Music>) :
    RecyclerView.Adapter<MusicAdapter.ViewHolder>() {

    private var mListener: OnItemClickListener? = null
    val mainactivity = mainactivity
    val thisContext = thisContext
    var musicList = musicList

    interface OnItemClickListener {
        fun onItemClick(position: Int, music: Music)
    }

    fun setOnItemClickListener(clickListener: OnItemClickListener) {
        mListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MusicViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(musicList,mainactivity,binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentEmp = musicList[position]
        holder.bind(position,currentEmp, mListener)
    }

    override fun getItemCount(): Int {
        return musicList.size
    }

    fun setFilteredList(mList: ArrayList<Music>) {
        this.musicList = mList
        notifyDataSetChanged()
    }

    class ViewHolder(musicList : ArrayList<Music>,mainactivity: MainActivity,private val binding: MusicViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val musicList = musicList
        val mainactivity = mainactivity

        fun bind(position:Int,currentMusic: Music, listener: OnItemClickListener?) {
            binding.songNameMV.text = currentMusic.songtitle
            binding.songAlbumMV.text = currentMusic.albumname

            // Load image from Firebase Storage URL
            // Load image from Firebase Realtime Database URL
            if (!currentMusic.imageUrl.isNullOrBlank()) {
                Picasso.get()
                    .load(currentMusic.imageUrl) // Firebase Realtime Database URL
                    .fit()
                    .centerInside()
                    .placeholder(R.drawable.music_note_icon)
                    .into(binding.imageMV)
            } else {
                Picasso.get()
                    .load(R.drawable.music_note_icon)
                    .fit()
                    .centerInside()
                    .placeholder(R.drawable.music_note_icon)
                    .into(binding.imageMV)
            }
                binding.songfragmenti.setOnClickListener{
                    listener?.onItemClick(adapterPosition, currentMusic)
                    mainactivity.replaceFragment(MusicView(position,musicList))
                    Log.d(musicList.size.toString(),"HIIIIIIIIIII")
                }

        }


    }
}