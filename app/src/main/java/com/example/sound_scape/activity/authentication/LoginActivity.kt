package com.example.sound_scape.activity.authentication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.sound_scape.MainActivity
import com.example.sound_scape.R
import com.example.sound_scape.activity.change.ResetPasswordActivity
import com.example.sound_scape.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.jakewharton.rxbinding2.widget.RxTextView

@SuppressLint("CheckResult")
class LoginActivity : AppCompatActivity(){

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.loginBtn.isEnabled = false
        binding.loginBtn.backgroundTintList = ContextCompat.getColorStateList(this, R.color.grey)

        val usernameStream = RxTextView.textChanges(binding.inputUsername)
            .skipInitialValue()
            .map { username ->
                username.isEmpty()
            }
        usernameStream.subscribe {
            showTextMinimalAlert(it , "Username")
        }

        val passwordStream = RxTextView.textChanges(binding.inputPassword)
            .skipInitialValue()
            .map { password ->
                password.isEmpty()
            }
        passwordStream.subscribe {
            showTextMinimalAlert(it , "Password")
        }

        val invalidFieldStream = io.reactivex.Observable.combineLatest(
            usernameStream,
            passwordStream
        ) { usernameInvalid: Boolean, passwordInvalid: Boolean ->
            !usernameInvalid && !passwordInvalid
        }

        invalidFieldStream.subscribe{isValid ->
            if(isValid){
                binding.loginBtn.isEnabled = true
                binding.loginBtn.backgroundTintList = ContextCompat.getColorStateList(this ,
                    R.color.primary_color
                )

            }
            else {
                binding.loginBtn.isEnabled = false
                binding.loginBtn.backgroundTintList = ContextCompat.getColorStateList(this ,
                    R.color.grey
                )

            }
        }

        binding.loginBtn.setOnClickListener{
            val username =binding.inputUsername.text.toString().trim()
            val password = binding.inputPassword.text.toString().trim()
            loginUser(username, password)

        }
        binding.dontHaveAccount.setOnClickListener{
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding.forgotPass.setOnClickListener {
            startActivity(Intent(this, ResetPasswordActivity::class.java))
        }
    }

    private fun showTextMinimalAlert(isNotValid:Boolean, text:String){
        if (text == "Username/Email")
            binding.inputUsername.error = if (isNotValid) "$text cannot be empty!" else null
        else if (text == "Password")
            binding.inputPassword.error = if (isNotValid) "$text cannot be empty!" else null
    }

    private fun loginUser(username: String, password: String) {
        auth.signInWithEmailAndPassword(username, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
    }
}
