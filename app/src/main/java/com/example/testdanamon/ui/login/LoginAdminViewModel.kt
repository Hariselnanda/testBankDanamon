package com.example.testdanamon.ui.login

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
class LoginAdminViewModel @Inject constructor(private val userRepository: DbRepository) : ViewModel() {

    private val _userList = MutableLiveData<List<UserEntity>>()
    val userList: LiveData<List<UserEntity>> = _userList

    private val _deleteUserById = MutableLiveData<Int?>()
    val deleteUserById: MutableLiveData<Int?> = _deleteUserById

    private val _isPasswordValid = MutableLiveData<Boolean>()
    val isPasswordValid: LiveData<Boolean> = _isPasswordValid

    init {
        loadUsers()
    }

     fun loadUsers() {
        viewModelScope.launch {
            val users = userRepository.getallUser()
            _userList.value =users
        }
    }

    fun deleteUserById(userId: Int) {
            viewModelScope.launch {
                userRepository.deleteUserById(userId)
        }
    }

    fun removeUser(position: Int) {
        val currentList = _userList.value.orEmpty().toMutableList()
        if (position >= 0 && position < currentList.size) {
            currentList.removeAt(position)
            _userList.value = currentList
        }
    }


    fun verifyPassword(username: String, password: String) {
        viewModelScope.launch {
            val isValid = userRepository.checkUserAndPassword(username, password)
            _isPasswordValid.value = isValid
        }
    }



}