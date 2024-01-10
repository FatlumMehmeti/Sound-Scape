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

            // RecyclerView setup
            _binding!!.musicRV.setHasFixedSize(true)

            // Retrieve and display total song count
            _binding!!.TotalSongs.text = "Total Songs: ${musicList.size}"

            getMusicData()
        }
    }
    private fun getMusicData() {
        dbRef = FirebaseDatabase.getInstance().getReference("Music")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                musicList.clear()

                // Check if the fragment is still attached
                if (!isAdded) return

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
//    private fun getMusicData() {
//        dbRef = FirebaseDatabase.getInstance().getReference("Music")
//
//        dbRef.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                musicList.clear()
//
//                // Check if the fragment is still attached
//                if (!isAdded) return
//
//                if (snapshot.exists()) {
//                    for (musicSnap in snapshot.children) {
//                        val musicData = musicSnap.getValue(Music::class.java)
//                        musicList.add(musicData!!)
//                    }
//
//                    // Notify the adapter about the data change
//                    musicAdapter.notifyDataSetChanged()
//
//                    // Update the total song count
//                    binding.TotalSongs.text = "Total Songs: ${musicList.size}"
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                // Handle onCancelled event if needed
//            }
//        })
//    }



    override fun onDestroyView() {
        super.onDestroyView()
        // Clean up the binding instance
        _binding = null
    }
}
