package com.example.teste_per_app.settings

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import com.example.sound_scape.MainActivity
import com.example.sound_scape.R
import com.example.sound_scape.activity.EditAccountActivity
import com.example.sound_scape.databinding.FragmentSettingsBinding


class Settings : Fragment(R.layout.fragment_settings) {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!


    private lateinit var mainactivity: MainActivity

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainactivity = activity as MainActivity

        sharedPreferences =
            requireActivity().getSharedPreferences("nightMode", Context.MODE_PRIVATE)
        val nightMode = sharedPreferences.getBoolean("nightMode", false)

        binding.nigtMode2.isChecked = nightMode

        binding.nigtMode2.setOnCheckedChangeListener { _, isChecked ->
            mainactivity.replaceFragment(Settings())
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) {
                    AppCompatDelegate.MODE_NIGHT_YES

                } else {
                    AppCompatDelegate.MODE_NIGHT_NO
                }
            )

            sharedPreferences.edit {
                putBoolean("nightMode", isChecked)
            }
            mainactivity.restartActivity()

        }
        //notificaton
//        binding.notifications2.setOnCheckedChangeListener { _, isChecked ->
//                // Handle the switch state change, e.g., perform an action based on isChecked
//                if (isChecked) {
//                    // Switch is ON
//                } else {
//                    // Switch is OFF
//                }}
        //Security
        binding.apply {
            security.setOnClickListener { mainactivity.replaceFragment(Security())}
            addSong.setOnClickListener { mainactivity.replaceFragment(Add_song()) }
            soundSpeed.setOnClickListener { mainactivity.replaceFragment(Sound_speed_and_time_limit())}
            infoNav.setOnClickListener { mainactivity.replaceFragment(Info()) }
            faq.setOnClickListener { mainactivity.replaceFragment(Frequently_Asked_question()) }
            feedback.setOnClickListener { mainactivity.replaceFragment(Feedback()) }
            exit.setOnClickListener { showExitDialog() }

            binding.editAccount.setOnClickListener {
                startActivity(Intent(requireContext(), EditAccountActivity::class.java))
            }

        }
    }
    private fun showExitDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Exit")
            .setMessage("Are you sure you want to exit?")
            .setPositiveButton("Yes") { _: DialogInterface?, _: Int ->
                requireActivity().finish()
            }
            .setNegativeButton("No") { dialog: DialogInterface?, _: Int ->
                dialog?.dismiss()
            }
            .show()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        // Clean up the binding instance
        _binding = null
    }
}