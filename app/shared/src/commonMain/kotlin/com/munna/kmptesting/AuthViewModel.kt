package com.munna.kmptesting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository,
    private val sessionManager: SessionManager
) : ViewModel() {
    private val _message = MutableStateFlow("")
    val message = _message.asStateFlow()

    private val _isRegisterSuccess = MutableStateFlow(false)
    val isRegisterSuccess = _isRegisterSuccess.asStateFlow()

    private val _isLoginSuccess = MutableStateFlow(false)
    val isLoginSuccess = _isLoginSuccess.asStateFlow()

    private val _userName = MutableStateFlow(sessionManager.getUserName())
    val userName = _userName.asStateFlow()

    fun login(request: LoginRequest) {
        viewModelScope.launch {
            try {
                val response = repository.login(request)
                _message.value = response.message
                if (response.success) {
                    val name = response.userName ?: "User"
                    _userName.value = name
                    sessionManager.saveUserName(name)
                    _isLoginSuccess.value = true
                }
            } catch (e: Exception) {
                _message.value = "Error: ${e.message}"
            }
        }
    }

    fun resetLoginSuccess() {
        _isLoginSuccess.value = false
    }

    fun logout() {
        sessionManager.clear()
        _isLoginSuccess.value = false
        _userName.value = ""
        _message.value = ""
    }

    fun register(request: RegisterRequest) {
        viewModelScope.launch {
            try {
                val response = repository.register(request)
                _message.value = response.message
                if (response.success) {
                    _isRegisterSuccess.value = true
                }
            } catch (e: Exception) {
                _message.value = "Error: ${e.message}"
            }
        }
    }
    
    fun clearMessage() {
        _message.value = ""
    }

    fun resetRegisterSuccess() {
        _isRegisterSuccess.value = false
    }
}
