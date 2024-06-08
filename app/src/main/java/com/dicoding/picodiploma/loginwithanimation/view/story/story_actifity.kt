package com.dicoding.picodiploma.loginwithanimation.view.story

import android.content.Intent
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
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.adapter.storyadapter
import com.dicoding.picodiploma.loginwithanimation.data.response.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.data.sharedpreference.sharedpreferencetoken
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityStoryActifityBinding
import com.dicoding.picodiploma.loginwithanimation.view.Detail_story.Detail_story
import com.dicoding.picodiploma.loginwithanimation.view.helper.viewmodelfactory
import com.dicoding.picodiploma.loginwithanimation.view.upload.Upload_story
import com.dicoding.picodiploma.loginwithanimation.view.welcome.WelcomeActivity
import org.xml.sax.helpers.ParserAdapter

class story_actifity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryActifityBinding

    private lateinit var sharedpreferencetoken: sharedpreferencetoken
    private var token :String? = null
    private lateinit var adapter : storyadapter

    private lateinit var sstoryviewmodel: storyviewmodel

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
        sstoryviewmodel = ViewModelProvider(this,factory)[storyviewmodel::class.java]

       sstoryviewmodel.story.observe(this){story ->
            setContent(story)
            sharedpreferencetoken
        }
        sstoryviewmodel.isloading.observe(this){
            showLoading(it)
        }

        binding.fabAdd.setOnClickListener{
            startActivity(Intent(this@story_actifity, Upload_story::class.java))
        }

        binding.topAppBar.setOnMenuItemClickListener {menuItem->
            when (menuItem.itemId){
                R.id.logOut ->{
                    val intent = Intent(this@story_actifity , WelcomeActivity::class.java)
                    sharedpreferencetoken.clearData()
                    startActivity(intent)
                    finish()

                    true
                }else ->{
                false
            }
            }
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
                val intent = Intent(this@story_actifity, Detail_story::class.java)
                intent.putExtra(Detail_story.EXTRA_USERNAME, data.name)
                intent.putExtra(Detail_story.EXTRA_IMAGE, data.photoUrl)
                intent.putExtra(Detail_story.EXTRA_DESKRIPSI, data.description)
                intent.putExtra(Detail_story.EXTRA_DATE, data.createdAt)

                startActivity(intent)
            }
        })
    }
    override fun onResume() {
        super.onResume()
        sstoryviewmodel.showStory(token)
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