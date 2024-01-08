package com.example.teste_per_app.activity.authentication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import androidx.core.content.ContextCompat
import com.example.sound_scape.R
import com.example.sound_scape.databinding.ActivitySignUpBinding
import com.jakewharton.rxbinding2.widget.RxTextView

@SuppressLint ("CheckResult")
class SignUpActivity : AppCompatActivity(){
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)


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
            startActivity(Intent(this, LoginActivity::class.java))
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
}