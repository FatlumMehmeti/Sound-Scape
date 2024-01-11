package com.example.sound_scape.activity.change

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.sound_scape.R
import com.example.sound_scape.activity.authentication.LoginActivity
import com.example.sound_scape.databinding.ActivityResetPasswordBinding
import com.google.firebase.auth.FirebaseAuth
import com.jakewharton.rxbinding2.widget.RxTextView

class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResetPasswordBinding
    private lateinit var auth : FirebaseAuth
    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()


        val emailStream = RxTextView.textChanges(binding.resetEmail)
            .skipInitialValue()
            .map { email ->
                !Patterns.EMAIL_ADDRESS.matcher(email).matches()
            }
        emailStream.subscribe{
            showEmailValidAlert(it)
        }

        binding.resetSubmitButton.setOnClickListener {
            val email = binding.resetEmail.text.toString().trim()

            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(this){ reset ->
                    if (reset.isSuccessful){
                        Intent(this, LoginActivity::class.java).also {
                            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(it)
                            Toast.makeText(this, "Check email to reset password!", Toast.LENGTH_SHORT).show()
                        }

                    } else {
                        Toast.makeText(this, reset.exception?.message, Toast.LENGTH_SHORT).show()

                    }
                }
        }

        binding.backToLogin.setOnClickListener{
            startActivity(Intent(this,LoginActivity::class.java))
        }
    }


    private fun showEmailValidAlert(isNotValid: Boolean) {
        if (isNotValid){
            binding.resetEmail.error = "Email is not valid"
            binding.resetEmail.backgroundTintList = ContextCompat.getColorStateList(this ,
                R.color.grey)
        } else{
            binding.resetEmail.error = null
            binding.resetEmail.backgroundTintList = ContextCompat.getColorStateList(this ,
                R.color.primary_color
            )
        }

    }
}