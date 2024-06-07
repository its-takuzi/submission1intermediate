package com.dicoding.picodiploma.loginwithanimation.view.Detail_story

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityDetailStoryBinding

class Detail_story : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding

    companion object{
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_IMAGE = "extra_image"
        const val EXTRA_DESKRIPSI = "extra_deskripsi"
        const val EXTRA_DATE = "extra_date"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(EXTRA_USERNAME)
        val imageUrl = intent.getStringExtra(EXTRA_IMAGE)
        val desc = intent.getStringExtra(EXTRA_DESKRIPSI)
        val date = intent.getStringExtra(EXTRA_DATE)

        getstorydetail(username, imageUrl, desc, date)

        setupView()
    }

    private fun getstorydetail(username: String?, imageUrl: String?, desc: String?, date: String?) {
        Glide.with(this).load(imageUrl).into(binding.detailImage)
        binding.deskripsiUsername.text = username
        binding.deskripsiDetail.text = desc
        binding.uploadDate.text = date
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
}