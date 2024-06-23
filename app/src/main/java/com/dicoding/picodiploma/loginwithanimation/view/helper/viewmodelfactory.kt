package com.dicoding.picodiploma.loginwithanimation.view.helper

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.ApiConfig
import com.dicoding.picodiploma.loginwithanimation.view.maps.mapsViewModel
import com.dicoding.picodiploma.loginwithanimation.view.story.StoryRepository
import com.dicoding.picodiploma.loginwithanimation.view.story.storyviewmodel

class ViewModelFactory private constructor(private val application: Application, private val token: String) : ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(application: Application, token: String): ViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ViewModelFactory(application, token).also { INSTANCE = it }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(storyviewmodel::class.java)) {
            val apiService = ApiConfig.getApiService()
            val storyRepository = StoryRepository(apiService, token)
            return storyviewmodel(storyRepository) as T
        } else if (modelClass.isAssignableFrom(mapsViewModel::class.java)) {
            return mapsViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}