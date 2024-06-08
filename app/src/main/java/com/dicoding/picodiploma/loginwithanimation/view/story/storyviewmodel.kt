package com.dicoding.picodiploma.loginwithanimation.view.story

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.loginwithanimation.data.response.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.data.response.StoryListResponse
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.ApiConfig
import com.dicoding.picodiploma.loginwithanimation.data.sharedpreference.sharedpreferencetoken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class storyviewmodel(application: Application) : ViewModel(){
    private val sharedpreferencetoken : sharedpreferencetoken = sharedpreferencetoken(application)
    private val _story = MutableLiveData<List<ListStoryItem>>()
    val story : LiveData<List<ListStoryItem>> = _story

    private val _isloading =MutableLiveData<Boolean>()
    val isloading : LiveData<Boolean> =_isloading

    companion object{
        const val TAG = "StoryActivity"
    }

    init {
        val token = sharedpreferencetoken.getToken()
        showStory(token)
    }

    fun showStory(token: String?) {
        _isloading.value = true
        val client = ApiConfig.getApiService().getStories("Bearer $token")
        client.enqueue(object : Callback<StoryListResponse> {
            override fun onResponse(call: Call<StoryListResponse>, response: Response<StoryListResponse>) {
                _isloading.value = false
                if (response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody != null){
                        _story.value = responseBody.listStory
                    }
                    else{
                        Log.e(TAG, "onfailure : ${response.message()}")
                    }
                }
            }
            override fun onFailure(call: Call<StoryListResponse>, t: Throwable) {
                Log.e(TAG, "gagal: ${t.message}" )
            }
        })
    }
}