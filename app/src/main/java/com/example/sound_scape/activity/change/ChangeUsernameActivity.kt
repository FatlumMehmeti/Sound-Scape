package com.example.sound_scape.activity.change

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.sound_scape.activity.EditAccountActivity
import com.example.sound_scape.databinding.ActivityChangeUsernameBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class ChangeUsernameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangeUsernameBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeUsernameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // Set the current username in the EditText and disable editing
        binding.oldName.setText(auth.currentUser?.displayName)
        binding.oldName.isEnabled = false


        binding.getBack.setOnClickListener {
            // Set the flag to indicate the source
            val intent = Intent(this, EditAccountActivity::class.java)
            intent.putExtra("fromChangeUsername", true)
            startActivity(intent)
            finish()       }

        binding.saveBtn.setOnClickListener {
            val newUsername = binding.newName.text.toString().trim()

            if (newUsername.isNotEmpty()) {
                val user = auth.currentUser

                // Update the user's display name in Firebase Authentication
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(newUsername)
                    .build()

                user?.updateProfile(profileUpdates)
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Username updated successfully in Firebase Authentication
                            startActivity(Intent(this, EditAccountActivity::class.java))
                        } else {
                            // Handle the error
                            Log.e("ChangeUsername", "Failed to update username", task.exception)
                            // You might want to show a toast or log the error
                        }
                    }
            } else {
                // Show an error message or handle the case when newUsername is empty
            }
        }
    }
}