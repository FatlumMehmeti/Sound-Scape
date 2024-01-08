package com.example.teste_per_app.settings

import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.sound_scape.MainActivity
import com.example.sound_scape.R
import com.example.sound_scape.databinding.FragmentAddSongBinding
import com.example.teste_per_app.settings.reporitoris_for_settings.AddSong
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class Add_song : Fragment(R.layout.fragment_add_song) {
    private var _binding: FragmentAddSongBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference
    private var selectedImageUri: Uri? = null
    private var selectedAudioUri: Uri? = null
    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddSongBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize Firebase database
        database = FirebaseDatabase.getInstance().getReference("ADDED SONGS")

        binding.selectImageButton.setOnClickListener {
            selectImage()
        }

        binding.selectSong.setOnClickListener {
            selectAudio()
        }

        binding.addSongToDatabase.setOnClickListener {
            addSongToDatabase()
        }

        // Initialize ProgressDialog
        progressDialog = ProgressDialog(context)
        progressDialog.setTitle("Uploading Files...")
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        progressDialog.max = 100
    }

    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 100)
    }

    private fun selectAudio() {
        val intent = Intent()
        intent.type = "audio/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 200)
    }

    private fun addSongToDatabase() {
        // Retrieve data from UI fields
        val songName = binding.songTitleEditText.text.toString()
        val artistName = binding.artistNameEditText.text.toString()
        val albumName = binding.albumNameLabel.text.toString()
        val releaseYear = binding.releaseYearEditText.text.toString()
        val genre = binding.genreEditText.text.toString()

        // Check if image and audio files are selected
        if ( selectedAudioUri == null) {
            Toast.makeText(context, "Please select an audio file", Toast.LENGTH_SHORT).show()
            return
        }

        // Upload image and audio files to Firebase Storage
        val imageFileName = "images/${UUID.randomUUID()}"
        val audioFileName = "audio/${UUID.randomUUID()}"

        val imageStorageReference = FirebaseStorage.getInstance().getReference(imageFileName)
        val audioStorageReference = FirebaseStorage.getInstance().getReference(audioFileName)

        val imageUploadTask = imageStorageReference.putFile(selectedImageUri!!)
        val audioUploadTask = audioStorageReference.putFile(selectedAudioUri!!)

        progressDialog.show()

        // Add an OnProgressListener to track the upload progress
        imageUploadTask.addOnProgressListener { snapshot ->
            val progress = (100.0 * snapshot.bytesTransferred / snapshot.totalByteCount).toInt()
            progressDialog.progress = progress
        }

        // Wait for both tasks to complete
        Tasks.whenAll(imageUploadTask, audioUploadTask)
            .addOnSuccessListener {
                progressDialog.dismiss()

                // Get the download URLs for the uploaded files
                val imageUrl = imageStorageReference.downloadUrl.toString()
                val audioUrl = audioStorageReference.downloadUrl.toString()

                // Create an AddSong object with the retrieved data
                val addSong = AddSong(songName, artistName, albumName, releaseYear, genre, imageUrl, audioUrl)

                // Save the AddSong object to the Firebase database
                database.child(songName).setValue(addSong)
                    .addOnSuccessListener {
                        // Clear UI fields on successful save
                        binding.songTitleEditText.text.clear()
                        binding.artistNameEditText.text.clear()
                        binding.albumNameLabel.text.clear()
                        binding.releaseYearEditText.text.clear()
                        binding.genreEditText.text.clear()
                        Toast.makeText(context, "Successfully saved", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Failed to save", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener {
                progressDialog.dismiss()
                Toast.makeText(context, "Failed to upload files", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && data != null && data.data != null) {
            when (requestCode) {
                100 -> { // Image selection
                    selectedImageUri = data.data
                    Toast.makeText(context, "Image selected", Toast.LENGTH_SHORT).show()
                }

                200 -> { // Audio selection
                    selectedAudioUri = data.data
                    Toast.makeText(context, "Audio selected", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }}
/*  private lateinit var binding: ActivityMainBinding
    private lateinit var database: DatabaseReference
    private var selectedImageUri: Uri? = null
    private var selectedAudioUri: Uri? = null
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase database
        database = FirebaseDatabase.getInstance().getReference("songs")

        binding.selectImageButton.setOnClickListener {
            selectImage()
        }

        binding.selectSong.setOnClickListener {
            selectAudio()
        }

        binding.addSongToDatabase.setOnClickListener {
            addSongToDatabase()
        }

        // Initialize ProgressDialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Uploading Files...")
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        progressDialog.max = 100
    }

    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 100)
    }

    private fun selectAudio() {
        val intent = Intent()
        intent.type = "audio/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 200)
    }

    private fun addSongToDatabase() {
        // Retrieve data from UI fields
        val songName = binding.songTitleEditText.text.toString()
        val artistName = binding.artistNameEditText.text.toString()
        val albumName = binding.albumNameLabel.text.toString()
        val releaseYear = binding.releaseYearEditText.text.toString()
        val genre = binding.genreEditText.text.toString()

        // Check if image and audio files are selected
        if ( selectedAudioUri == null) {
            Toast.makeText(this, "Please select an audio file", Toast.LENGTH_SHORT).show()
            return
        }

        // Upload image and audio files to Firebase Storage
        val imageFileName = "images/${UUID.randomUUID()}"
        val audioFileName = "audio/${UUID.randomUUID()}"

        val imageStorageReference = FirebaseStorage.getInstance().getReference(imageFileName)
        val audioStorageReference = FirebaseStorage.getInstance().getReference(audioFileName)

        val imageUploadTask = imageStorageReference.putFile(selectedImageUri!!)
        val audioUploadTask = audioStorageReference.putFile(selectedAudioUri!!)

        progressDialog.show()

        // Add an OnProgressListener to track the upload progress
        imageUploadTask.addOnProgressListener { snapshot ->
            val progress = (100.0 * snapshot.bytesTransferred / snapshot.totalByteCount).toInt()
            progressDialog.progress = progress
        }

        // Wait for both tasks to complete
        Tasks.whenAll(imageUploadTask, audioUploadTask)
            .addOnSuccessListener {
                progressDialog.dismiss()

                // Get the download URLs for the uploaded files
                val imageUrl = imageStorageReference.downloadUrl.toString()
                val audioUrl = audioStorageReference.downloadUrl.toString()

                // Create an AddSong object with the retrieved data
                val addSong = AddSong(songName, artistName, albumName, releaseYear, genre, imageUrl, audioUrl)

                // Save the AddSong object to the Firebase database
                database.child(songName).setValue(addSong)
                    .addOnSuccessListener {
                        // Clear UI fields on successful save
                        binding.songTitleEditText.text.clear()
                        binding.artistNameEditText.text.clear()
                        binding.albumNameLabel.text.clear()
                        binding.releaseYearEditText.text.clear()
                        binding.genreEditText.text.clear()
                        Toast.makeText(this, "Successfully saved", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to save", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener {
                progressDialog.dismiss()
                Toast.makeText(this, "Failed to upload files", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && data != null && data.data != null) {
            when (requestCode) {
                100 -> { // Image selection
                    selectedImageUri = data.data
                    Toast.makeText(this, "Image selected", Toast.LENGTH_SHORT).show()
                }
                200 -> { // Audio selection
                    selectedAudioUri = data.data
                    Toast.makeText(this, "Audio selected", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }*/