package com.example.sound_scape


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sound_scape.databinding.FragmentHomeBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private lateinit var musicAdapter: MusicAdapter
    private lateinit var musicList: ArrayList<Music>
    private lateinit var dbRef: DatabaseReference

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (_binding != null) {
            _binding!!.musicRV.layoutManager = LinearLayoutManager(requireContext())

            musicList = arrayListOf()
            val mainActivity=activity as MainActivity
            musicAdapter = MusicAdapter(requireContext(),mainActivity,musicList)
            _binding!!.musicRV.adapter = musicAdapter

            _binding!!.musicRV.setHasFixedSize(true)

            _binding!!.TotalSongs.text = "Total Songs: ${musicList.size}"

            getMusicData()
        }
    }
    private fun getMusicData() {
        dbRef = FirebaseDatabase.getInstance().getReference("Music")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                musicList.clear()

                if (!isAdded) return

                if (snapshot.exists()) {
                    for (musicSnap in snapshot.children) {
                        val newMusic = Music(
                            musicSnap.child("songtitle").getValue().toString(),
                            musicSnap.child("artistname").getValue().toString(),
                            musicSnap.child("albumname").getValue().toString(),
                            musicSnap.child("releaseyear").getValue().toString(),
                            musicSnap.child("genre").getValue().toString(),
                            musicSnap.child("imageUrl").getValue().toString(),
                            musicSnap.child("favorite").getValue().toString().toBoolean(),
                            musicSnap.child("songPlayed").getValue().toString().toLong(),
                            musicSnap.child("audioUrl").getValue().toString()
                        )
                        musicList.add(newMusic)
                    }

                    musicAdapter.notifyDataSetChanged()

                    binding.TotalSongs.text = "Total Songs: ${musicList.size}"
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
