package com.example.sound_scape.activity.authentication

import User
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.sound_scape.R
import com.example.sound_scape.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.jakewharton.rxbinding2.widget.RxTextView

@SuppressLint ("CheckResult")
class SignUpActivity : AppCompatActivity(){
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth : FirebaseAuth

    val user = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()


        binding.signupBtn.isEnabled = false
        binding.signupBtn.backgroundTintList = ContextCompat.getColorStateList(this, R.color.grey)


        val emailStream = RxTextView.textChanges(binding.email)
            .skipInitialValue()
            .map { email ->
                !Patterns.EMAIL_ADDRESS.matcher(email).matches()
            }
        emailStream.subscribe{
            showEmailValidAlert(it)
        }

        val usernameStream = RxTextView.textChanges(binding.inputUsername)
            .skipInitialValue()
            .map { username ->
                username.length < 6
            }
        usernameStream.subscribe {
            showTextMinimalAlert(it , "Username")
        }

        val passwordStream = RxTextView.textChanges(binding.inputPassword)
            .skipInitialValue()
            .map { password ->
                password.length < 8
            }
        passwordStream.subscribe {
            showTextMinimalAlert(it , "Password")
        }

        val passwordConfirmStream = io.reactivex.Observable.merge(
            RxTextView.textChanges(binding.inputPassword)
                .skipInitialValue()
                .map { password ->
                    password.toString() != binding.confirmPassword.text.toString()
                },
            RxTextView.textChanges(binding.confirmPassword)
                .skipInitialValue()
                .map { confirmPassword ->
                    confirmPassword.toString() != binding.inputPassword.text.toString()
                })
        passwordConfirmStream.subscribe {
            showPasswordConfirmAlert(it)
        }

        val invalidFieldStream = io.reactivex.Observable.combineLatest(
            usernameStream,
            emailStream,
            passwordStream,
            passwordConfirmStream

        ) { emailInvalid: Boolean, usernameInvalid: Boolean, passwordInvalid: Boolean, confirmPasswordInvalid: Boolean ->
            !emailInvalid && !usernameInvalid && !passwordInvalid && !confirmPasswordInvalid
        }

        invalidFieldStream.subscribe{isValid ->
            if(isValid){
                binding.signupBtn.isEnabled = true
                binding.signupBtn.backgroundTintList = ContextCompat.getColorStateList(this ,
                    R.color.primary_color
                )

            }
            else {
                binding.signupBtn.isEnabled = false
                binding.signupBtn.backgroundTintList = ContextCompat.getColorStateList(this ,
                    R.color.grey
                )

            }
        }

        binding.signupBtn.setOnClickListener{
            val email =binding.email.text.toString().trim()
            val password = binding.inputPassword.text.toString().trim()
            val username = binding.inputUsername.text.toString().trim()
            registerUser(email, password, username)
        }
        binding.haveAccount.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
    private fun showTextMinimalAlert(isNotValid:Boolean, text:String){
        if (text == "Username")
            binding.inputUsername.error = if (isNotValid) "$text must be longer than 6 letters!" else null
        else if (text == "Password")
            binding.inputPassword.error = if (isNotValid) "$text must be longer than 8 letters!" else null
    }

    private fun showEmailValidAlert(isNotValid: Boolean){
        binding.email.error = if (isNotValid) "Email is invalid" else null
    }
    private fun showPasswordConfirmAlert (isNotValid: Boolean){
        binding.confirmPassword.error = if (isNotValid) "Password isn't the same" else null
    }

    private fun registerUser(email: String, password: String, username: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val userId = FirebaseAuth.getInstance().currentUser?.uid
                    val user = User(userId, email, username)

                    saveUserDataToDatabase(userId, user)

                    val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putBoolean("isNewUser", true)
                    editor.apply()

                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun saveUserDataToDatabase(userId: String?, user: User) {
        if (userId != null) {
            val databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId)
            databaseReference.setValue(user)
        } else {
            Toast.makeText(this, "Error saving user data to the database", Toast.LENGTH_SHORT).show()
        }
    }


}