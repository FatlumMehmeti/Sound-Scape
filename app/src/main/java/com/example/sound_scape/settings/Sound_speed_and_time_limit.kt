package com.example.teste_per_app.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sound_scape.MainActivity
import com.example.sound_scape.R
import com.example.sound_scape.databinding.SoundSpeedAndTimeLimitBinding


class Sound_speed_and_time_limit : Fragment(R.layout.sound_speed_and_time_limit) {
    private var _binding: SoundSpeedAndTimeLimitBinding? = null
    private val binding get() = _binding!!

    lateinit var mainactivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SoundSpeedAndTimeLimitBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainactivity = activity as MainActivity
        binding.getBack.setOnClickListener {
            mainactivity.replaceFragment(Settings())
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}