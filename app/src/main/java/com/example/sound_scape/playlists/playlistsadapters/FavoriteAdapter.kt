package com.example.sound_scape

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.sound_scape.databinding.FavoriteViewBinding
import com.example.sound_scape.databinding.MusicViewBinding
import com.example.sound_scape.player.MusicView
import com.squareup.picasso.Picasso

class FavoriteAdapter(thisContext: Context, mainactivity: MainActivity, private var musicList: ArrayList<Music>) :
    RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    private var mListener: OnItemClickListener? = null
    val mainactivity = mainactivity
    val thisContext = thisContext

    interface OnItemClickListener {
        fun onItemClick(position: Int, music: Music)
    }

    fun setOnItemClickListener(clickListener: OnItemClickListener) {
        mListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FavoriteViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(musicList,thisContext,mainactivity,binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentEmp = musicList[position]
        if(currentEmp.favorite == true) holder.bind(position,currentEmp, mListener)
    }

    override fun getItemCount(): Int {
        return musicList.size
    }

    fun setFilteredList(mList: ArrayList<Music>) {
        this.musicList = mList
        notifyDataSetChanged()
    }

    class ViewHolder(musicList : ArrayList<Music>,thisContext: Context,mainactivity: MainActivity,private val binding: FavoriteViewBinding) :
        RecyclerView.ViewHolder(binding.root) {


        val musicList = musicList
        val mainactivity = mainactivity
        val thisContext = thisContext

        fun bind(position:Int,currentMusic: Music, listener: OnItemClickListener?) {

                binding.songNameFV.text = currentMusic.songtitle

                // Load image from Firebase Storage URL
                // Load image from Firebase Realtime Database URL
                if (!currentMusic.imageUrl.isNullOrBlank()) {
                    Picasso.get()
                        .load(currentMusic.imageUrl) // Firebase Realtime Database URL
                        .fit()
                        .centerInside()
                        .placeholder(R.drawable.music_note_icon)
                        .into(binding.songImgFV)
                } else {
                    Picasso.get()
                        .load(R.drawable.music_note_icon)
                        .fit()
                        .centerInside()
                        .placeholder(R.drawable.music_note_icon)
                        .into(binding.songImgFV)
                }
                binding.playthis.setOnClickListener {
                    listener?.onItemClick(adapterPosition, currentMusic)
                    mainactivity.replaceFragment(MusicView(position,musicList))
                    Log.d("", "po preket ")
                }
        }

    }
}