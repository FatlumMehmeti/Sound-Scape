package com.example.teste_per_app.playlists.playlistsadapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sound_scape.Music
import com.example.sound_scape.R
import com.example.sound_scape.databinding.FavoriteViewBinding
import com.squareup.picasso.Picasso

class MostPlayedAdapter(private var musicList: ArrayList<Music>) :
    RecyclerView.Adapter<MostPlayedAdapter.ViewHolder>() {

    private var mListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: OnItemClickListener) {
        mListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FavoriteViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
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

    class ViewHolder(private val binding: FavoriteViewBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(currentMusic: Music, listener: OnItemClickListener?) {
            binding.songNameFV.text = currentMusic.albumName
            if (currentMusic.isRemote) {
                // Load from remote URL
                Picasso.get()
                    .load(currentMusic.imageUrl)
                    .fit()
                    .centerInside()
                    .placeholder(R.drawable.music_note_icon)
                    .into(binding.songImgFV)
            } else {
                // Load from drawable resource
                Picasso.get()
                    .load(currentMusic.imageUrl)
                    .fit()
                    .centerInside()
                    .placeholder(R.drawable.music_note_icon) // Optional placeholder image
                    .into(binding.songImgFV)
            }

            binding.root.setOnClickListener {
                listener?.onItemClick(adapterPosition)
            }
        }
    }
}
