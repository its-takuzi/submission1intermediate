package com.dicoding.picodiploma.loginwithanimation.view.helper

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.picodiploma.loginwithanimation.view.story.story_actifity
import com.dicoding.picodiploma.loginwithanimation.view.story.storyviewmodel
import com.google.android.ads.mediationtestsuite.viewmodels.ViewModelFactory

class viewmodelfactory private constructor(private val mApplication: Application) : ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var INSTANCE: viewmodelfactory? = null

        @JvmStatic
        fun getInstance(application: Application): viewmodelfactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = viewmodelfactory(application)
                }
            }
            return INSTANCE as viewmodelfactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(story_actifity::class.java)) {
            return storyviewmodel(mApplication) as T }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}