package com.example.sound_scape.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sound_scape.activity.authentication.LoginActivity
import com.example.sound_scape.activity.authentication.SignUpActivity
import com.example.sound_scape.databinding.ActivityMainPageBinding

class MainPageActivity : AppCompatActivity(){
    private lateinit var  binding: ActivityMainPageBinding

    override fun onCreate(savedInstanceState : Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityMainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        binding.btnSignUp.setOnClickListener{
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }
}