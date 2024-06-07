package inter.storymemet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository) : ViewModel() {

//    private val _stories = MutableLiveData<StoryResponse>()
//    val stories: LiveData<StoryResponse> = _stories

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

//    fun getStories() {
//        viewModelScope.launch {
//            repository.getSession().collect {
//                _stories.value = repository.getStoriesRepo(it.token)
//            }
//        }
//    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    val story: LiveData<PagingData<ListStoryItem>> = repository.getStoryPaging().cachedIn(viewModelScope)
}