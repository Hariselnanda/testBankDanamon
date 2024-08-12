package com.example.testdanamon.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testdanamon.database.UserEntity
import com.example.testdanamon.repository.DbRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel@Inject constructor(private val userRepository: DbRepository) : ViewModel() {

    private val _registrationResult = MutableLiveData<Boolean>()
    val registrationResult: LiveData<Boolean> = _registrationResult

    private val _registeredUser = MutableLiveData<UserEntity>()
    val registeredUser: LiveData<UserEntity> = _registeredUser

    fun register(username: String, password: String, role: String, email: String) {
        viewModelScope.launch {
            val user =
                UserEntity(username = username, email = email, password = password, role = role)
            userRepository.registerUser(user)
            _registeredUser.postValue(user)
            _registrationResult.postValue(true)
        }
    }
}