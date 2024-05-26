package inter.storymemet.view.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import inter.storymemet.LoginResponse
import inter.storymemet.Repository
import inter.storymemet.StoryResponse
import inter.storymemet.UserModel
import kotlinx.coroutines.launch

class MapsViewModel(private val repository: Repository) : ViewModel()  {

    private val _mapResult = MutableLiveData<StoryResponse>()
    val mapResult: LiveData<StoryResponse> = _mapResult

    fun getAllStoriesWithLocation() {
        viewModelScope.launch {
            repository.getSession().collect() {
                _mapResult.value = repository.getAllStoriesWithLocationRepo(it.token)
            }
        }
    }
}