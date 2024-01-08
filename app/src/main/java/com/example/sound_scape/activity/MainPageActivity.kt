package com.example.teste_per_app.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sound_scape.databinding.ActivityMainPageBinding
import com.example.teste_per_app.activity.authentication.LoginActivity
import com.example.teste_per_app.activity.authentication.SignUpActivity

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