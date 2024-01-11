package com.example.sound_scape

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sound_scape.databinding.MusicViewBinding
import com.example.sound_scape.player.MusicView
import com.squareup.picasso.Picasso

class MusicAdapter(thisContext: Context, mainactivity: MainActivity, private var musicList: ArrayList<Music>) :
    RecyclerView.Adapter<MusicAdapter.ViewHolder>() {

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
        val binding = MusicViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(musicList,thisContext,mainactivity,binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentEmp = musicList[position]
        holder.bind(currentEmp, mListener)
    }

    override fun getItemCount(): Int {
        return musicList.size
    }

    fun setFilteredList(mList: ArrayList<Music>) {
        this.musicList = mList
        notifyDataSetChanged()
    }

    class ViewHolder(musicList : ArrayList<Music>,thisContext: Context,mainactivity: MainActivity,private val binding: MusicViewBinding) :
        RecyclerView.ViewHolder(binding.root) {


        val musicList = musicList
        val mainactivity = mainactivity
        val thisContext = thisContext

        fun bind(currentMusic: Music, listener: OnItemClickListener?) {
            binding.songNameMV.text = currentMusic.songtitle
            binding.songAlbumMV.text = currentMusic.albumname


            if (!currentMusic.imageUrl.isNullOrBlank()) {
                Picasso.get()
                    .load(currentMusic.imageUrl)
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
                    mainactivity.replaceFragment(MusicView(musicList))
                    Log.d("","po preket ")
                }

        }


    }
}