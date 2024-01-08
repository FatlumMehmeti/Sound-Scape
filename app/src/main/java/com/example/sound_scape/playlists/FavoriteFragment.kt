package com.example.teste_per_app.playlists

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sound_scape.Music
import com.example.sound_scape.R
import com.example.sound_scape.databinding.FragmentFavoriteBinding
import com.example.teste_per_app.playlists.playlistsadapters.FavoriteAdapter
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

        // RecyclerView setup
        binding.favRec.setHasFixedSize(true)

        // Initialize musicList before creating the adapter
        musicList = arrayListOf()
        addDataToList()
        // Set up GridLayoutManager
        binding.favRec.layoutManager = GridLayoutManager(requireContext(), 4)

        // Initialize the adapter
        musicAdapter = FavoriteAdapter(musicList)
        binding.favRec.adapter = musicAdapter

        // Retrieve and display total song count
        //getMusicData()
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

//
//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.GridLayoutManager
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.example.sound_scape.Music
//import com.example.sound_scape.R
//import com.example.sound_scape.databinding.FragmentFavoriteBinding
//import com.example.teste_per_app.playlists.playlistsadapters.FavoriteAdapter
//import com.google.firebase.database.DataSnapshot
//import com.google.firebase.database.DatabaseError
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.FirebaseDatabase
//import com.google.firebase.database.ValueEventListener
//
//class FavoriteFragment : Fragment() {
//    private var binding: FragmentFavoriteBinding? = null
//    private lateinit var musicAdapter: FavoriteAdapter
//    private lateinit var musicList: ArrayList<Music>
//    private lateinit var dbRef: DatabaseReference
//
//
//
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_favorite, container, false)
//    }
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        if (binding != null) {
//            binding!!.favRec.layoutManager = GridLayoutManager(requireContext(),4)
//
//            musicList = arrayListOf()
//           // addDataToList()
//
//            musicAdapter = FavoriteAdapter(musicList)
//            binding!!.favRec.adapter = musicAdapter
//
//            // RecyclerView setup
//            binding!!.favRec.setHasFixedSize(true)
//
//            // Retrieve and display total song count
//
//            getMusicData()
//        }
//        binding!!.favRec.layoutManager = LinearLayoutManager(requireContext())
//
//        // Initialize musicList before creating the adapter
//        musicList = arrayListOf()
//
//        musicAdapter = FavoriteAdapter(musicList)
//        binding!!.favRec.adapter = musicAdapter
//
//        // RecyclerView setup
//        binding!!.favRec.setHasFixedSize(true)
//
//        // Retrieve and display total song count
//
////        getMusicData()
//    }
////    private fun addDataToList() {
////        musicList.add(Music("Java", "Prova","Prova", true,"https://firebasestorage.googleapis.com/v0/b/git-hubmusicapp.appspot.com/o/images%2F2024_01_02_20_22_32?alt=media&token=3fb18b6e-c540-4762-9cea-e029ecfcdd8a" ))
////        musicList.add(Music("Muxi", "Prova","Prova",false, null, R.drawable.illustration))
////        musicList.add(Music("Test", "Prova","Prova",false, null, R.drawable.home_icon))
////    }
//    private fun getMusicData() {
//        dbRef = FirebaseDatabase.getInstance().getReference("songs")
//
//        dbRef.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                musicList.clear()
//                if (snapshot.exists()) {
//                    for (musicSnap in snapshot.children) {
//                        val musicData = musicSnap.getValue(Music::class.java)
//                        musicList.add(musicData!!)
//                    }
//
//                    // Notify the adapter about the data change
//                    musicAdapter.notifyDataSetChanged()
//
//                    }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                // Handle onCancelled event if needed
//            }
//        })
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        // Clean up the binding instance
//        binding = null
//    }
//
//
//}