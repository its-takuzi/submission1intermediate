package com.dicoding.picodiploma.loginwithanimation.view.story

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.loginwithanimation.adapter.storyadapter
import com.dicoding.picodiploma.loginwithanimation.data.response.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.data.sharedpreference.sharedpreferencetoken
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityStoryActifityBinding
import com.dicoding.picodiploma.loginwithanimation.view.helper.viewmodelfactory

class story_actifity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryActifityBinding

    private lateinit var sharedpreferencetoken: sharedpreferencetoken
    private var token :String? = null

    private lateinit var storyviewmodel: storyviewmodel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryActifityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()

        sharedpreferencetoken = sharedpreferencetoken(this)
        token = sharedpreferencetoken.getToken()
        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (!token.isNullOrEmpty()) {
                    finishAffinity()
                }
            }
        })

        val layoutManager = LinearLayoutManager(this)
        binding.rvStory.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this,layoutManager.orientation)
        binding.rvStory.addItemDecoration(itemDecoration)


        val factory = viewmodelfactory.getInstance(application)
        storyviewmodel = ViewModelProvider(this,factory)[storyviewmodel::class.java]


        storyviewmodel.story.observe(this){story ->
            setContent(story)
            sharedpreferencetoken
        }
        storyviewmodel.isloading.observe(this){
            showLoading(it)
        }

    }

    private fun showLoading(isloading: Boolean?) {
        if (isloading == true) {
            binding.progresBar.visibility = View.VISIBLE

        } else {
            binding.progresBar.visibility = View.GONE
        }
    }

    private fun setContent(story: List<ListStoryItem>?) {
        Log.d("HomeActivity", "Received ${story?.size} items from ViewModel")
        val adapter = storyadapter()
        adapter.submitList(story)
        binding.rvStory.adapter = adapter
        sharedpreferencetoken
        adapter.setOnClickCallBack(object : storyadapter.OnItemClickCallback{
            override fun onItemClicked(data: ListStoryItem) {
                Toast.makeText(this@story_actifity, "clicked", Toast.LENGTH_SHORT).show()
            }

        })
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