package com.example.sound_scape

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sound_scape.databinding.FragmentPlaylistsBinding
import com.example.teste_per_app.playlists.FavoriteFragment
import com.example.teste_per_app.playlists.MostPlayedFragment
import com.example.teste_per_app.playlists.TracksFragment


class Playlists : Fragment(R.layout.fragment_playlists) {

    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!

    lateinit var mainactivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainactivity = activity as MainActivity
        navigateToFragment(TracksFragment())
        binding.favorites.setOnClickListener {
            navigateToFragment(FavoriteFragment())
        }
        binding.mostPlayed.setOnClickListener {
            navigateToFragment(MostPlayedFragment())
        }
        binding.tracks.setOnClickListener {
            navigateToFragment(TracksFragment())
        }

    }
    private fun navigateToFragment(fragment: Fragment) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.play_fragments, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}








