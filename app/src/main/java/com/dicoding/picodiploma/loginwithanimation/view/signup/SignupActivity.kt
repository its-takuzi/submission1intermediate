package com.dicoding.picodiploma.loginwithanimation.view.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.picodiploma.loginwithanimation.data.response.Register
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivitySignupBinding
import com.dicoding.picodiploma.loginwithanimation.view.login.LoginActivity

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var vIewModel: RegisterVIewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        vIewModel = ViewModelProvider(this).get(RegisterVIewModel::class.java)

        setupView()
        binding.emailEditText.validateInput(0)
        binding.passwordEditText.validateInput(1)
        binding.nameEditText.validateInput(2)
        playAnimation()
        validateregister()

    }

    private fun validateregister() {
        binding.signupButton.setOnClickListener{
            val email = binding.emailEditText
            val password = binding.passwordEditText
            val username = binding.nameEditText

            if (email.text.toString().isEmpty() || password.text.toString().isEmpty() || username.text.toString().isEmpty()){
                Toast.makeText(this, "pastikan username, email, dan password sudah terisi!", Toast.LENGTH_SHORT).show()
            }

            if ( email.isEmailValid && password.isPasswordValid && username.isUsernameValid){
                showLoading(true)
                    vIewModel.postRegister(Register(username.text.toString(), email.text.toString(), password.text.toString()))

                    vIewModel.registerresponse.observe(this){
                        Log.d("TAG", "Hasil Register : $it")
                        if(!it.error!!){
                            showLoading(false)
                            startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
                            finish()
                        }else{
                            showLoading(false)
                            Toast.makeText(this, "Register Failed maybe email already taken. Try again!", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }





    private fun showLoading(state : Boolean){
        if(state){
            binding.pbProgressBar.visibility = View.VISIBLE
        }else{
            binding.pbProgressBar.visibility = View.GONE
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val nameTextView =
            ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(100)
        val nameEditTextLayout =
            ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(100)


        AnimatorSet().apply {
            playSequentially(
                title,
                nameTextView,
                nameEditTextLayout,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                signup
            )
            startDelay = 100
        }.start()
    }
}


