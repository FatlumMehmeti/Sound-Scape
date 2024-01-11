package com.example.teste_per_app.playlists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.sound_scape.databinding.FragmentTracksBinding
import com.example.teste_per_app.playlists.playlistsadapters.TracksAdapter
import com.example.teste_per_app.settings.reporitoris_for_settings.AddSong
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class TracksFragment : Fragment() {
    private var _binding: FragmentTracksBinding? = null
    private val binding get() = _binding!!

    private lateinit var musicAdapter: TracksAdapter
    private lateinit var musicList: ArrayList<AddSong>
    private lateinit var dbRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.favRec.setHasFixedSize(true)

        musicList = arrayListOf()
//        addDataToList()
        binding.favRec.layoutManager = GridLayoutManager(requireContext(), 4)

        musicAdapter = TracksAdapter(musicList)
        binding.favRec.adapter = musicAdapter

        getMusicData()
    }

    private fun getMusicData() {
        dbRef = FirebaseDatabase.getInstance().getReference("ADDED SONGS")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                musicList.clear()
                if (snapshot.exists()) {
                    for (musicSnap in snapshot.children) {
                        val musicData = musicSnap.getValue(AddSong::class.java)
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