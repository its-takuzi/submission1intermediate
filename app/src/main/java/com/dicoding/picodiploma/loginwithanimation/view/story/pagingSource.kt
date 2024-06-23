
package com.dicoding.picodiploma.loginwithanimation.view.story
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bumptech.glide.load.HttpException
import com.dicoding.picodiploma.loginwithanimation.data.response.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.ApiService
import com.dicoding.picodiploma.loginwithanimation.data.sharedpreference.sharedpreferencetoken
import java.io.IOException


class PagingSource(private val apiService: ApiService,
                   private val token: String
) : PagingSource<Int, ListStoryItem>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val position = params.key ?: 1
            val response = apiService.getStories(token, position, params.loadSize)
            val stories = response.listStory

            LoadResult.Page(
                data = stories,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (stories.isEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
