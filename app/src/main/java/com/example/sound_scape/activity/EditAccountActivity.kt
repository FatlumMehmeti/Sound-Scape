package com.example.teste_per_app.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sound_scape.databinding.ActivityEditAccountBinding
import com.example.teste_per_app.activity.authentication.LoginActivity
import com.example.teste_per_app.activity.password.ChangePasswordActivity

class EditAccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backToLogin.setOnClickListener{
            startActivity(Intent(this , LoginActivity::class.java))

        }

        binding.btnChangePassword.setOnClickListener {
            startActivity(Intent(this, ChangePasswordActivity::class.java))
        }

        binding.logOut.setOnClickListener{
            startActivity(Intent(this, MainPageActivity::class.java))
        }

    }}