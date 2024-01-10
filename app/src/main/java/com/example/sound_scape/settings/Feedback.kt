package com.example.teste_per_app.settings

import android.app.ProgressDialog
import android.net.Uri
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
import com.example.teste_per_app.settings.reporitoris_for_settings.AddSong
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

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

        // Initialize ProgressDialog
        progressDialog = ProgressDialog(context)
        progressDialog.setTitle("Uploading Files...")
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        progressDialog.max = 100
    }
    private fun addSongToDatabase() {
        // Retrieve data from UI fields
        val topicEditable = binding.topicFA.text
        val emailEditable = binding.emailFA.text
        val feedbackEditable = binding.feedbackMsgFA.text

        val topic = topicEditable?.toString() ?: ""
        val email = emailEditable?.toString() ?: ""
        val feedback = feedbackEditable?.toString() ?: ""

        // Get the download URL for the uploaded audio file

        // Check if image file is selected

        // If no image is selected, create an AddSong object without imageUrl
        val feddback = FeedbackD(topic, email, feedback)

        // Save the AddSong object to the Firebase database
        database.child(topic).setValue(feddback)
            .addOnSuccessListener {
                // Clear UI fields on successful save
                binding.topicFA.text?.clear()
                binding.emailFA.text?.clear()
                binding.feedbackMsgFA.text?.clear()

                Toast.makeText(context, "Successfully send", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to send", Toast.LENGTH_SHORT).show()
            }
    }
//    private fun addSongToDatabase() {
//        // Retrieve data from UI fields
//        val topic = binding.topicFA.text?.toString() ?: ""
//        val email = binding.emailFA.text?.toString() ?: ""
//        val feedback = binding.feedbackMsgFA.text?.toString() ?: ""
//
//
//
//
//        // Get the download URL for the uploaded audio file
//
//            // Check if image file is selected
//
//                // If no image is selected, create an AddSong object without imageUrl
//                val addSong = AddSong(topic,email,feedback)
//
//                // Save the AddSong object to the Firebase database
//                database.child(topic).setValue(addSong)
//                    .addOnSuccessListener {
//                        // Clear UI fields on successful save
//                        binding.topicFA.text.clear()
//                        binding.emailFA.text.clear()
//                        binding.feedbackMsgFA.text.clear()
//
//                        Toast.makeText(context, "Successfully saved", Toast.LENGTH_SHORT).show()
//                    }
//                    .addOnFailureListener {
//                        Toast.makeText(context, "Failed to save", Toast.LENGTH_SHORT).show()
//                    }
//            }
    override fun onDestroyView() {
        super.onDestroyView()
        // Clean up the binding instance
        _binding = null
    } }




