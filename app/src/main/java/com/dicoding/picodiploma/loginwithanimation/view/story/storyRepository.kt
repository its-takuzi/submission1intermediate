package com.dicoding.picodiploma.loginwithanimation.view.story

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.picodiploma.loginwithanimation.data.response.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.ApiService
import com.dicoding.picodiploma.loginwithanimation.data.sharedpreference.sharedpreferencetoken


class StoryRepository(private val apiService: ApiService, private val token: String) {

    fun getStory(): LiveData<PagingData<ListStoryItem>> {
        return liveData {
            val pager = Pager(
                config = PagingConfig(
                    pageSize = 5
                ),
                pagingSourceFactory = {
                    PagingSource(apiService, token)
                }
            )
            emitSource(pager.liveData)
        }
    }

}