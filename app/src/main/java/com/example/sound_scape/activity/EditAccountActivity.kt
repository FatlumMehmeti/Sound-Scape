package com.example.sound_scape.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sound_scape.activity.change.ChangeUsernameActivity
import com.example.sound_scape.databinding.ActivityEditAccountBinding
import com.google.firebase.auth.FirebaseAuth

@Suppress("DEPRECATION")
class EditAccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditAccountBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.getBack.setOnClickListener {
            onBackPressed()       }


        binding.btnChangeName.setOnClickListener {
            startActivity(Intent(this, ChangeUsernameActivity::class.java))
        }

        binding.logOut.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, MainPageActivity::class.java))
        }


        fetchAndDisplayUserData()
    }



    override fun onResume() {
        super.onResume()
        fetchAndDisplayUserData()
    }

    private fun fetchAndDisplayUserData() {
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val userEmail = currentUser.email
            val username = currentUser.displayName

            binding.editTextEmail.setText(userEmail)
            binding.editTextUsername.setText(username)
        } else {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
            Log.e("EditAccountActivity", "User not authenticated")
        }
    }
}