package inter.storymemet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.launch

class LoginViewModel(private val repository: Repository) : ViewModel() {

    private val _resultLogin = MutableLiveData<LoginResponse>()
    val resultLogin: LiveData<LoginResponse> = _resultLogin

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _resultLogin.value = repository.loginUser(email, password)
        }
    }
}