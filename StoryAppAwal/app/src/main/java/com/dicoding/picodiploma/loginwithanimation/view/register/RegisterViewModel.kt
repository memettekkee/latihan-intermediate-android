package com.dicoding.picodiploma.loginwithanimation.view.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.response.RegisterResponse
import com.dicoding.picodiploma.loginwithanimation.repository_injection.Repository
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: Repository)  : ViewModel() {
    private val _resultRegister = MutableLiveData<RegisterResponse>()
    val resultRegister: LiveData<RegisterResponse> = _resultRegister

    fun registerUser(name: String, email: String, password: String) {
        viewModelScope.launch {
            _resultRegister.value = repository.registerUser(name, email, password)
        }
    }
}