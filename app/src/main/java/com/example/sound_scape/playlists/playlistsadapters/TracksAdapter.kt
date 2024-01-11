package com.example.teste_per_app.playlists.playlistsadapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sound_scape.R
import com.example.sound_scape.databinding.FavoriteViewBinding
import com.example.teste_per_app.settings.reporitoris_for_settings.AddSong
import com.squareup.picasso.Picasso

class TracksAdapter(private var musicList: ArrayList<AddSong>) :
    RecyclerView.Adapter<TracksAdapter.ViewHolder>() {

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
    fun setFilteredList(mList: ArrayList<AddSong>) {
        this.musicList = mList
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: FavoriteViewBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(currentMusic: AddSong, listener: OnItemClickListener?) {
            binding.songNameFV.text = currentMusic.albumname
            Picasso.get()
                .load(currentMusic.imageUrl)
                .fit()
                .centerInside()
                .placeholder(R.drawable.music_note_icon)
                .into(binding.songImgFV)
//            if (currentMusic.isRemote) {
//
//                Picasso.get()
//                    .load(currentMusic.imageUrl)
//                    .fit()
//                    .centerInside()
//                    .placeholder(R.drawable.music_note_icon)
//                    .into(binding.songImgFV)
//            } else {
//
//                Picasso.get()
//                    .load(currentMusic.imageUrl)
//                    .fit()
//                    .centerInside()
//                    .placeholder(R.drawable.music_note_icon)
//                    .into(binding.songImgFV)
//            }

            binding.root.setOnClickListener {
                listener?.onItemClick(adapterPosition)
            }
        }
    }
}
