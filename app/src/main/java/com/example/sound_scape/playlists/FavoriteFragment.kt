package com.example.teste_per_app.playlists

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sound_scape.FavoriteAdapter
import com.example.sound_scape.MainActivity
import com.example.sound_scape.Music
import com.example.sound_scape.MusicAdapter
import com.example.sound_scape.R
import com.example.sound_scape.databinding.FragmentFavoriteBinding
import com.example.teste_per_app.settings.reporitoris_for_settings.AddSong
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FavoriteFragment : Fragment() {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var musicAdapter: FavoriteAdapter
    private lateinit var musicList: ArrayList<Music>
    private lateinit var dbRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.favRec.setHasFixedSize(true)

        musicList = arrayListOf()

        binding.favRec.layoutManager = GridLayoutManager(requireContext(), 4)

        musicList = arrayListOf()
        val mainActivity=activity as MainActivity
        musicAdapter = FavoriteAdapter(requireContext(),mainActivity,musicList)
        _binding!!.favRec.adapter = musicAdapter

        getMusicData()
    }

private fun getMusicData() {
    dbRef = FirebaseDatabase.getInstance().getReference("Music")

    dbRef.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            musicList.clear()

            if (!isAdded) return

            if (snapshot.exists()) {
                for (musicSnap in snapshot.children) {
                    val musicData = musicSnap.getValue(Music::class.java)
                    musicList.add(musicData!!)
                }
                musicAdapter.notifyDataSetChanged()
            }
        }

        override fun onCancelled(error: DatabaseError) {
        }
    })
}

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
