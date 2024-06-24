package com.dicoding.picodiploma.loginwithanimation.ui.untility

import com.dicoding.picodiploma.loginwithanimation.data.response.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.data.response.StoryListResponse


object DataDummy {
    fun generateDummyStories(): StoryListResponse {
        val listStory = ArrayList<ListStoryItem>()
        for (i in 1..20) {
            val story = ListStoryItem(
                createdAt = "2024-06-22T22:22:22Z",
                description = "Description $i",
                id = "id_$i",
                name = "Name $i",
                photoUrl = "https://th.bing.com/th/id/OIP.wQrbFMCbvmYW8yxTlrLzsAAAAA?rs=1&pid=ImgDetMain"
            )
            listStory.add(story)
        }

        return StoryListResponse(
            error = false,
            message = "Stories fetched successfully",
            listStory = listStory
        )
    }
}