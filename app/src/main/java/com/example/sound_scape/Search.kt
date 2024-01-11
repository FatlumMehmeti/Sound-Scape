package com.example.sound_scape

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sound_scape.databinding.FragmentSearchBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Locale

class Search : Fragment(R.layout.fragment_search) {

    private var _binding: FragmentSearchBinding? = null
    private var musicList = ArrayList<Music>()
    private lateinit var adapter: MusicAdapter
    private lateinit var dbRef: DatabaseReference

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.selectSearchSongs.setHasFixedSize(true)
        binding.selectSearchSongs.layoutManager = LinearLayoutManager(requireContext())
        val mainactivity = activity as MainActivity
        adapter = MusicAdapter(requireContext(),mainactivity,musicList)
        binding.selectSearchSongs.adapter = adapter
        binding.searchSongs.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }
        })

        getMusicData()

    }

    private fun getMusicData() {
        dbRef = FirebaseDatabase.getInstance().getReference("Music")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                musicList.clear()
                if (snapshot.exists()) {
                    for (musicSnap in snapshot.children) {
                        val musicData = musicSnap.getValue(Music::class.java)
                        musicList.add(musicData!!)
                    }

                    adapter.notifyDataSetChanged()

                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun filterList(query: String?) {
        if (query != null) {
            val filteredList = ArrayList<Music>()
            for (i in musicList) {
                if (i.songtitle.lowercase(Locale.ROOT).contains(query)) {
                    filteredList.add(i)
                }
                else  if (i.songtitle.uppercase(Locale.ROOT).contains(query)) {
                    filteredList.add(i)
                }
            }

            if (filteredList.isEmpty()) {
                Toast.makeText(requireContext(), "No Data found", Toast.LENGTH_SHORT).show()
            } else {
                adapter.setFilteredList(filteredList)
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}