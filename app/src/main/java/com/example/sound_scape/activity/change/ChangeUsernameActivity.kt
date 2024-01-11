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

        binding.oldName.setText(auth.currentUser?.displayName)
        binding.oldName.isEnabled = false


        binding.getBack.setOnClickListener {
            val intent = Intent(this, EditAccountActivity::class.java)
            intent.putExtra("fromChangeUsername", true)
            startActivity(intent)
            finish()       }

        binding.saveBtn.setOnClickListener {
            val newUsername = binding.newName.text.toString().trim()

            if (newUsername.isNotEmpty()) {
                val user = auth.currentUser

                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(newUsername)
                    .build()

                user?.updateProfile(profileUpdates)
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            startActivity(Intent(this, EditAccountActivity::class.java))
                        } else {
                            Log.e("ChangeUsername", "Failed to update username", task.exception)
                        }
                    }
            } else {
            }
        }
    }
}