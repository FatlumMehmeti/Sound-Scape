package com.example.teste_per_app.settings

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.sound_scape.MainActivity
import com.example.sound_scape.R
import com.example.sound_scape.databinding.FeedbackBinding
import com.example.sound_scape.settings.reporitoris_for_settings.FeedbackD
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Feedback : Fragment(R.layout.feedback) {
    private var _binding: FeedbackBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference
    private lateinit var progressDialog: ProgressDialog

    lateinit var mainactivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FeedbackBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainactivity = activity as MainActivity
           binding.getBack.setOnClickListener {
               mainactivity.replaceFragment(Settings())
           }
        database = FirebaseDatabase.getInstance().getReference("Feedbacks")



        binding.sendFA.setOnClickListener {
            addSongToDatabase()
        }

        progressDialog = ProgressDialog(context)
        progressDialog.setTitle("Uploading Files...")
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        progressDialog.max = 100
    }
    private fun addSongToDatabase() {
        val topicEditable = binding.topicFA.text
        val emailEditable = binding.emailFA.text
        val feedbackEditable = binding.feedbackMsgFA.text

        val topic = topicEditable?.toString() ?: ""
        val email = emailEditable?.toString() ?: ""
        val feedback = feedbackEditable?.toString() ?: ""


        val feddback = FeedbackD(topic, email, feedback)


        database.child(topic).setValue(feddback)
            .addOnSuccessListener {

                binding.topicFA.text?.clear()
                binding.emailFA.text?.clear()
                binding.feedbackMsgFA.text?.clear()

                Toast.makeText(context, "Successfully send", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to send", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    } }




