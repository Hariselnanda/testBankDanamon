package com.example.testdanamon

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testdanamon.repository.DbRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val userRepository: DbRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean> = _loginResult

    private val _userRole = MutableLiveData<String?>()
    val userRole: MutableLiveData<String?> = _userRole

    private val _userEmail = MutableLiveData<String?>()
    val userEmail: MutableLiveData<String?> = _userEmail

    fun login(username: String, password: String) {
        viewModelScope.launch {
            val user = userRepository.getUser(username, password)
            if (user != null) {
                _loginResult.value = true
                _userRole.value = user.role
                _userEmail.value = user.email
                Timber.d("Usernya ${user.email} ${user.password} ${user.role}")
            } else {
                _loginResult.value = false
                _userRole.value = null // hapus data role
                _userEmail.value = null // hapus data email
            }
        }
    }
}