package com.example.teste_per_app.activity.password

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sound_scape.databinding.ActivityChangePasswordBinding
import com.example.sound_scape.databinding.ActivityEditAccountBinding


class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangePasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener{
            startActivity(Intent(this , ActivityEditAccountBinding::class.java))
        }

        binding.btnSave.setOnClickListener{
            startActivity(Intent(this , ActivityEditAccountBinding::class.java))
        }
    }
}