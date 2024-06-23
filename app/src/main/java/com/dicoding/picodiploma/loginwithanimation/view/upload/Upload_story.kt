package com.dicoding.picodiploma.loginwithanimation.view.upload

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.load.HttpException
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.ApiConfig
import com.dicoding.picodiploma.loginwithanimation.data.sharedpreference.sharedpreferencetoken
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityUploadStory2Binding
import com.dicoding.picodiploma.loginwithanimation.view.helper.getImageUri
import com.dicoding.picodiploma.loginwithanimation.view.helper.reduceFileImage
import com.dicoding.picodiploma.loginwithanimation.view.helper.uriToFile
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class Upload_story : AppCompatActivity() {

    private lateinit var binding: ActivityUploadStory2Binding
    private lateinit var sharedpreferencetoken: sharedpreferencetoken

    private var currentImageUri: Uri? = null

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadStory2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()

        sharedpreferencetoken = sharedpreferencetoken(this)
        binding.galleryButton.setOnClickListener { startGallery() }
        binding.cameraButton.setOnClickListener { startCamera() }
        binding.uploadButton.setOnClickListener { uploadImage() }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun uploadImage() {
        currentImageUri?.let { uri ->
            try {
                val imageFile = uriToFile(uri, this).reduceFileImage()
                Log.d("Image File", "Path: ${imageFile.path}")
                val description = binding.inputEditText.text.toString()

                showLoading(true)

                val requestBody = description.toRequestBody("text/plain".toMediaType())
                val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
                val multipartBody = MultipartBody.Part.createFormData(
                    "photo",
                    imageFile.name,
                    requestImageFile
                )

                val token = "${sharedpreferencetoken.getToken().toString()}"

                lifecycleScope.launch {
                    try {
                        val apiService = ApiConfig.getApiService()
                        val successResponse = apiService.uploadImage(token, multipartBody, requestBody)
                        successResponse.message?.let { showToast(it) }
                        showLoading(false)
                        val intent = Intent()
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    } catch (e: HttpException) {
                        Log.e("Upload Image", "HttpException: ${e.message}")
                        showToast(e.message.toString())
                        showLoading(false)
                    } catch (e: Exception) {
                        Log.e("Upload Image", "Exception: ${e.message}")
                        showToast(e.message.toString())
                        showLoading(false)
                    }
                }
            } catch (e: Exception) {
                Log.e("Upload Image", "File Error: ${e.message}")
                showToast(getString(R.string.eror))
                showLoading(false)
            }
        } ?: showToast(getString(R.string.empty_image_warning))
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "Uri: $it")
            binding.previewImageView.setImageURI(it)
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

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}