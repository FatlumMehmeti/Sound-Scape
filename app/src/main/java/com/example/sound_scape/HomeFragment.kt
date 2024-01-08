package com.example.sound_scape


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (_binding != null) {
            _binding!!.musicRV.layoutManager = LinearLayoutManager(requireContext())

            musicList = arrayListOf()
            addDataToList()

            musicAdapter = MusicAdapter(musicList)
            _binding!!.musicRV.adapter = musicAdapter

            // RecyclerView setup
            _binding!!.musicRV.setHasFixedSize(true)

            // Retrieve and display total song count
            _binding!!.TotalSongs.text = "Total Songs: ${musicList.size}"

//            getMusicData()
        }
//        binding.musicRV.setItemViewCacheSize(13)
//        binding.musicRV.layoutManager = LinearLayoutManager(requireContext())
//
//        // Initialize musicList before creating the adapter
//        musicList = arrayListOf()
//
//        musicAdapter = MusicAdapter(musicList)
//        binding.musicRV.adapter = musicAdapter
//
//        // RecyclerView setup
//        binding.musicRV.setHasFixedSize(true)
//
//        // Retrieve and display total song count
//        binding.TotalSongs.text = "Total Songs: ${musicList.size}"
//
//        getMusicData()
    }
    private fun addDataToList() {
        musicList.add(Music("Java", "Prova","Prova", true,"https://firebasestorage.googleapis.com/v0/b/git-hubmusicapp.appspot.com/o/images%2F2024_01_02_20_22_32?alt=media&token=3fb18b6e-c540-4762-9cea-e029ecfcdd8a" ))
        musicList.add(Music("Muxi", "Prova","Prova",false, null, R.drawable.illustration))
        musicList.add(Music("Test", "Prova","Prova",false, null, R.drawable.home_icon))
    }
    private fun getMusicData() {
        dbRef = FirebaseDatabase.getInstance().getReference("songs")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                musicList.clear()
                if (snapshot.exists()) {
                    for (musicSnap in snapshot.children) {
                        val musicData = musicSnap.getValue(Music::class.java)
                        musicList.add(musicData!!)
                    }

                    // Notify the adapter about the data change
                    musicAdapter.notifyDataSetChanged()

                    // Update the total song count
                    binding.TotalSongs.text = "Total Songs: ${musicList.size}"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled event if needed
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clean up the binding instance
        _binding = null
    }
}
