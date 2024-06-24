package com.dicoding.picodiploma.loginwithanimation.view.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.picodiploma.loginwithanimation.data.response.ListStoryItem

class storyviewmodel(private val storyRepository: StoryRepository) : ViewModel() {
    fun getStories(): LiveData<PagingData<ListStoryItem>> {
        return storyRepository.getStory().cachedIn(viewModelScope)
    }
}