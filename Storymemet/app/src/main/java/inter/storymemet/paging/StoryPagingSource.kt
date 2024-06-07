package inter.storymemet.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import inter.storymemet.ApiConfig
import inter.storymemet.ApiService
import inter.storymemet.ListStoryItem
import inter.storymemet.Repository
import inter.storymemet.StoryResponse
import inter.storymemet.UserPreference
import kotlinx.coroutines.flow.first

class StoryPagingSource(private val repository: Repository, private val preferences: UserPreference) : PagingSource<Int, ListStoryItem>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val tokenUser = preferences.getSession().first().token
            if (tokenUser.isNotEmpty()) {
//                val responseData = apiService.dataStoriesPaging(tokenUser, page, params.loadSize)
                val responseData = repository.getStoriesRepo(tokenUser, page, params.loadSize)
                Log.d("dataPaging", "$responseData")

                LoadResult.Page(
                    data = responseData.listStory,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (responseData.listStory.isEmpty()) null else page + 1
                )
            } else {
                LoadResult.Error(Exception("Failed load story"))
            }
        } catch (exception: Exception) {
            Log.d("pagingError", exception.toString())
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}