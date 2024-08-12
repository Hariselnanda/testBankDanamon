package com.example.testdanamon.ui.update

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
class UpdateViewModel @Inject constructor(private val userRepository: DbRepository) : ViewModel() {

    private val _userById = MutableLiveData<UserEntity?>()
    val userById: MutableLiveData<UserEntity?> = _userById

    private val _allUsers = MutableLiveData<Boolean>()
    val allUsers: LiveData<Boolean> = _allUsers

    fun getUserById(userId: Int) {
        viewModelScope.launch {
            val user = userRepository.getUserById(userId)
            _userById.postValue(user)
        }
    }

    fun update(id : Int, username: String, password: String, role: String, email: String) {
        viewModelScope.launch {

            val sucesss =userRepository.updateUserById(id,username,email,role,password)
            _allUsers.postValue(true)
        }
    }
}