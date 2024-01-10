package com.example.teste_per_app.playlists

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.sound_scape.FavoriteAdapter
import com.example.sound_scape.MainActivity
import com.example.sound_scape.MostPlayedAdapter
import com.example.sound_scape.Music
import com.example.sound_scape.R
import com.example.sound_scape.databinding.FragmentFavoriteBinding
import com.example.sound_scape.databinding.FragmentMostPlayedBinding
import com.example.teste_per_app.settings.reporitoris_for_settings.AddSong
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MostPlayedFragment : Fragment() {
    private var _binding: FragmentMostPlayedBinding? = null
    private val binding get() = _binding!!

    private lateinit var musicAdapter: MostPlayedAdapter
    private lateinit var musicList: ArrayList<Music>
    private lateinit var dbRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMostPlayedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView setup
        binding.favRec.setHasFixedSize(true)

        // Initialize musicList before creating the adapter
        musicList = arrayListOf()
//        addDataToList()
        // Set up GridLayoutManager
        binding.favRec.layoutManager = GridLayoutManager(requireContext(), 4)

        // Initialize the adapter
        val mainActivity=activity as MainActivity
        musicAdapter = MostPlayedAdapter(requireContext(),mainActivity,musicList)
        _binding!!.favRec.adapter = musicAdapter

        // Retrieve and display total song count
        //getMusicData()
    }
//    private fun addDataToList() {
//        musicList.add(Music("Java", "Prova","Prova", true,"https://firebasestorage.googleapis.com/v0/b/git-hubmusicapp.appspot.com/o/images%2F2024_01_02_20_22_32?alt=media&token=3fb18b6e-c540-4762-9cea-e029ecfcdd8a" ))
//        musicList.add(Music("Muxi", "Prova","Prova",false, null, R.drawable.illustration))
//        musicList.add(Music("Test", "Prova","Prova",false, null, R.drawable.home_icon))
//    }
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