package com.example.teste_per_app.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sound_scape.MainActivity
import com.example.sound_scape.R
import com.example.sound_scape.databinding.FrequentlyAskedQuestionBinding

class Frequently_Asked_question : Fragment(R.layout.frequently_asked_question) {
    private var _binding: FrequentlyAskedQuestionBinding? = null
    private val binding get() = _binding!!

    lateinit var mainactivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FrequentlyAskedQuestionBinding.inflate(inflater, container, false)
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
        // Clean up the binding instance
        _binding = null
    }
}