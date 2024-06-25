package com.dicoding.picodiploma.loginwithanimation.view.story

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.adapter.storyadapter
import com.dicoding.picodiploma.loginwithanimation.data.response.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.data.sharedpreference.sharedpreferencetoken
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityStoryActifityBinding
import com.dicoding.picodiploma.loginwithanimation.view.Detail_story.Detail_story
import com.dicoding.picodiploma.loginwithanimation.view.helper.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.maps.MapsActivity
import com.dicoding.picodiploma.loginwithanimation.view.upload.Upload_story
import com.dicoding.picodiploma.loginwithanimation.view.welcome.WelcomeActivity

class story_actifity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryActifityBinding
    private lateinit var sharedpreferencetoken: sharedpreferencetoken
    private var token: String? = null
    private lateinit var adapter: storyadapter
    private lateinit var sstoryviewmodel: storyviewmodel

    private lateinit var uploadLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryActifityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()

        sharedpreferencetoken = sharedpreferencetoken(this)
        token = sharedpreferencetoken.getToken()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (!token.isNullOrEmpty()) {
                    finishAffinity()
                }
            }
        })

        val layoutManager = LinearLayoutManager(this)
        binding.rvStory.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvStory.addItemDecoration(itemDecoration)

        adapter = storyadapter { }
        binding.rvStory.adapter = adapter

        adapter.setOnClickCallBack(object : storyadapter.OnItemClickCallback {
            override fun onItemClicked(data: ListStoryItem) {
                val intent = Intent(this@story_actifity, Detail_story::class.java)
                intent.putExtra(Detail_story.EXTRA_USERNAME, data.name)
                intent.putExtra(Detail_story.EXTRA_IMAGE, data.photoUrl)
                intent.putExtra(Detail_story.EXTRA_DESKRIPSI, data.description)
                intent.putExtra(Detail_story.EXTRA_DATE, data.createdAt)
                startActivity(intent)
            }
        })

        val factory = ViewModelFactory.getInstance(application, token!!)
        sstoryviewmodel = ViewModelProvider(this, factory)[storyviewmodel::class.java]

        token?.let {
            sstoryviewmodel.getStories().observe(this) { pagingData ->
                adapter.submitData(lifecycle, pagingData)
            }
        }

        uploadLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                token?.let {
                    sstoryviewmodel.getStories().observe(this) { pagingData ->
                        adapter.submitData(lifecycle, pagingData)
                    }
                }
            }
        }

        binding.fabAdd.setOnClickListener {
            val intent = Intent(this@story_actifity, Upload_story::class.java)
            uploadLauncher.launch(intent)
        }

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.maps -> {
                    startActivity(Intent(this, MapsActivity::class.java))
                    true
                }
                R.id.logOut -> {
                    val intent = Intent(this@story_actifity, WelcomeActivity::class.java)
                    sharedpreferencetoken.clearData()
                    startActivity(intent)
                    finish()
                    true
                }
                else -> false
            }
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
}